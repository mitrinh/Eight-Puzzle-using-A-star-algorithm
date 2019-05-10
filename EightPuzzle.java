/*
 * Michael Trinh
 * CS 4200: Artificial Intelligence
 * Professor Atanasio
 * Project 1: 8-Puzzle
 * Description: Use A* search to solve the 8-puzzle problem using
 *  two different heuristic functions: h1 = num of misplaced tiles, 
 *  h2 = sum of distances of the tiles from their goal positions. 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public final class EightPuzzle {
    private final Integer[][] goalState = {{0,1,2},{3,4,5},{6,7,8}};
    private final List<Integer[][]> puzzleList;
    private final int puzzleLength = goalState.length;
    puzzlePrinter puzzPrintObject = new puzzlePrinter();
    public EightPuzzle() {
        generateEightPuzzle puzzGenObject = new generateEightPuzzle();
        puzzleList = puzzGenObject.generate8Puzzle();
        /* print generated 8-puzzles */
        System.out.println("Puzzles: ");
        puzzPrintObject.printPuzzles(puzzleList);
        List<List<Integer>> searchCosts1 = new ArrayList<>();
        List<List<Integer>> searchCosts2 = new ArrayList<>();
        List<Integer> avgSearchCosts1;
        List<Integer> avgSearchCosts2;
        // solve each puzzle using A* algorithm
        for(Integer[][] puzzle:puzzleList){
            aStarSearch(puzzle,1,searchCosts1);
            aStarSearch(puzzle,2,searchCosts2);
        }
        avgSearchCosts1 = calculateAvgSearchCosts(searchCosts1);
        avgSearchCosts2 = calculateAvgSearchCosts(searchCosts2);
        /* print avg search costs for each heuristic */
        System.out.println("Heuristic 1: ");
        printAvgSearchCosts(avgSearchCosts1);
        System.out.println("Heuristic 2: ");
        printAvgSearchCosts(avgSearchCosts2);
    }
    
    public Node aStarSearch(Integer[][] initialState, int heuristic, 
            List<List<Integer>> searchCosts){
        Node node = new Node(initialState,0,heuristic,1);
        Queue<Node> frontier = new PriorityQueue<>((Node n1, Node n2) -> 
                (n1.estimatedCost-n2.estimatedCost));
        frontier.add(node);
        List<Integer[][]> stateFrontier = new ArrayList<>();
        stateFrontier.add(node.state);
        List<Integer> estimatedCostFrontier = new ArrayList<>();
        estimatedCostFrontier.add(node.estimatedCost);
        List<Integer[][]> explored = new ArrayList<>();
        List<String> actions;
        int searchCost = 0;
        List<Integer> searchCostList = new ArrayList<>();
        /* start loop */
        while(!frontier.isEmpty()){
            node = frontier.remove();
            if(node.depth%2 == 0 && node.depth >= 2 && node.depth <= 24)
                searchCostList.add(searchCost, (node.depth/2)-1);
            if(goalState == node.state) {
                searchCosts.add(searchCostList);
                return node;
            }
            explored.add(node.state);
            actions = getActions(node.state);
            searchCost = addChildrenToFrontier(actions, node, heuristic, stateFrontier, 
                    estimatedCostFrontier, frontier, explored, searchCost);
        }
        return null;
    }
    
    private int addChildrenToFrontier(List<String> actions, Node node, 
            int heuristic, List<Integer[][]> stateFrontier, List<Integer> estimatedCostFrontier,
            Queue<Node> frontier, List<Integer[][]> explored, int searchCost) {
        Node child;
        for(String action : actions) {
            child = nodeWithAction(node, action, heuristic);
            searchCost++;
            if((!stateFrontier.contains(child.state)) && (!explored.contains(child.state))) {
                frontier.add(child);
                stateFrontier.add(child.state);
                estimatedCostFrontier.add(child.estimatedCost);
            }
            else if(stateFrontier.contains(child.state) && 
                    child.estimatedCost < estimatedCostFrontier.get(stateFrontier.indexOf(child.state))){
                // replace node in frontier with child if child costs less
                frontier.remove(node);
                stateFrontier.remove(node.state);
                estimatedCostFrontier.remove(node.estimatedCost);
                frontier.add(child);
                stateFrontier.add(child.state);
                estimatedCostFrontier.add(child.estimatedCost);
            }
        }
        return searchCost;
    }
    
    private Node nodeWithAction(Node node, String action, int heuristic){
        Node result = new Node(node.state,node.pathCost,heuristic,node.depth);
        Integer[][] state = result.state;
        Position blankPosition = getPosition(state, 0);
        int index;
        Position actionPosition = new Position();
        switch(action){
            case "up":
                index = blankPosition.heightIndex-1;
                actionPosition = new Position(blankPosition.widthIndex, index);
                break;
            case "left":
                index = blankPosition.widthIndex-1;
                actionPosition = new Position(index, blankPosition.heightIndex);
                break;
            case "right":
                index = blankPosition.widthIndex+1;
                actionPosition = new Position(index, blankPosition.heightIndex);
                break;
            case "down":
                index = blankPosition.heightIndex+1;
                actionPosition = new Position(blankPosition.widthIndex, index);
                break;
        }
        swap(result.state, blankPosition, actionPosition);
        result.pathCost++;
        return result;
    }
    
    private List<String> getActions(Integer[][] state){
        List<String> actions = new ArrayList<>();
        int index;
        Position blankPosition = getPosition(state,0);
        index = blankPosition.heightIndex-1;
        if(index >= 0) actions.add("up");
        index = blankPosition.widthIndex-1;
        if(index >= 0) actions.add("left");
        index = blankPosition.widthIndex+1;
        if(index < puzzleLength) actions.add("right");
        index = blankPosition.heightIndex+1;
        if(index < puzzleLength) actions.add("down");
        return actions;
    }
    
    private Position getPosition(Integer[][] state, int element){
        for(int i=0; i<puzzleLength;i++){
            for(int j=0; j<puzzleLength;j++){
                if(state[i][j] == element) return new Position(i,j);
            }
        }
        return null;
    }
    
    private void swap(Integer[][] state, Position p1, Position p2){
        int temp = state[p1.widthIndex][p1.heightIndex];
        state[p1.widthIndex][p1.heightIndex] = state[p2.widthIndex][p2.heightIndex];
        state[p2.widthIndex][p2.heightIndex] = temp;
    }
    
    private List<Integer> calculateAvgSearchCosts(List<List<Integer>> searchCosts){
        List<Integer> avgSearchCosts = new ArrayList<>();
        List<Integer> numOfSearchCosts = new ArrayList<>();
        // get the sum and amount of search costs for each puzzle
        for(int i=0; i<searchCosts.size(); i++){
            for(int j=0; j<searchCosts.get(i).size(); j++){
                if(i==0){
                    avgSearchCosts.add(searchCosts.get(i).get(j), j);
                    numOfSearchCosts.add(0, j);
                }
                else{
                    avgSearchCosts.set(j,searchCosts.get(i).get(j) + avgSearchCosts.get(j));
                    numOfSearchCosts.set(j,numOfSearchCosts.get(j)+1);
                }
            }
        }
        // divide each search cost to get average
        for(int i = 0; i<avgSearchCosts.size();i++){
            avgSearchCosts.set(i, avgSearchCosts.get(i)/numOfSearchCosts.get(i));
        }
        return avgSearchCosts;
    }
    
    private void printAvgSearchCosts(List<Integer> avgSearchCosts){
        for(Integer searchCost:avgSearchCosts){
            System.out.println(searchCost);
        }
        System.out.println("");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Michael Trinh's 8-Puzzle");
        EightPuzzle eightPuzzle = new EightPuzzle(); 
    }
}
