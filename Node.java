// Michael Trinh
// helper class to 8-Puzzle Project

/**
 *
 * @author Michael Trinh
 */
public class Node {
    private final Integer[][] goalState = {{0,1,2},{3,4,5},{6,7,8}};
    Integer[][] state;
    int pathCost;
    int heuristic;
    int estimatedCostToGoal;
    int estimatedCost;
    int depth;
    
    public Node() {
    }

    public Node(Integer[][] state, int pathCost, int heuristic, int depth) {
        this.state = state;
        this.pathCost = pathCost;
        this.heuristic = heuristic;
        this.depth = depth;
        this.estimatedCostToGoal = calculateHeuristic(state, heuristic);
        this.estimatedCost = pathCost + estimatedCostToGoal;
    }
    
    private int calculateHeuristic(Integer[][] state, int heuristic) {
        switch(heuristic){
            case 1:
                return heuristic1(state, goalState);
            case 2:
                return heuristic2(state, goalState);
        }
        return 10;
    }
    
    // h1 = the number of misplaced tiles
    public int heuristic1(Integer[][] state, Integer[][] goalState){
        int result = 0;
        for(int i=0; i<state.length;i++){
            for(int j=0; j<state.length;j++){
                if(!(state[i][j].equals(goalState[i][j]))) result++;
            }
        }
        return result;
    }
    
    // h2 = the sum of the distances of the tiles from their goal positions
    public int heuristic2(Integer[][] state, Integer[][] goalState){
        int result = 0;
        for(int i=0; i<state.length;i++){
            for(int j=0; j<state.length;j++){
                result += Math.abs(getManhattanDistance(state, state[i][j]) - 
                        getManhattanDistance(goalState, state[i][j]));
            }
        }
        return result;
    }
    
    public int getManhattanDistance(Integer[][] state, int index){
        for(int i=0; i<state.length;i++){
            for(int j=0; j<state.length;j++){
                if(state[i][j] == index) return (i+j);
            }
        }
        return 10;
    }
}
