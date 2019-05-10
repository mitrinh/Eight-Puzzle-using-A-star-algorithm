// Michael Trinh
// helper class to generate 8-Puzzles

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Michael Trinh
 */
public class generateEightPuzzle {
    public generateEightPuzzle(){}
    
    public List<Integer[][]> generate8Puzzle(){
        List<Integer[][]> result = new ArrayList<>();
        System.out.print("Type '1' or '2' -> 1) Generate random 8-Puzzle or 2) Input an 8-Puzzle: ");
        try (Scanner input = new Scanner(System.in)) {
            int response = input.nextInt();
            // generate random puzzles
            switch (response) {
                case 1:
                    System.out.print("How many random puzzles do you want generated? ");
                    Scanner nextInput = new Scanner(System.in); 
                    int numOfPuzzles = nextInput.nextInt();
                    for (int i = 0; i < numOfPuzzles; i++)
                        result.add(randomizePuzzle());
                    break;
                case 2:
                    Integer[][] tempPuzzle = inputPuzzle();
                    if(tempPuzzle != null)
                        result.add(inputPuzzle());
                    else {
                        System.out.println("Unsolvable Puzzle entered, try again!");
                        inputPuzzle();
                    }
                    break;
                default:
                    return null;
            }
        }
        return result;
    }

    private Integer[][] randomizePuzzle() {
        Integer[][] puzzle = new Integer[3][3];
        List<Integer> randomNums = new ArrayList<>();
        for (int i = 0; i <= 8; i++) randomNums.add(i);
        for (int i = 0; i < puzzle.length; i++) {
            for(int j = 0; j < 3; j++) {
                Collections.shuffle(randomNums);
                puzzle[i][j] = randomNums.get(0);
                randomNums.remove((int)0);
            }
        }
        return puzzle ;
    }

    private Integer[][] inputPuzzle() {
        Integer[][] puzzle = new Integer[3][3];
        System.out.print("Input a specific 8-Puzzle configuration: ");
        try (Scanner input = new Scanner(System.in)) {
            String[] inputArray = input.nextLine().split(" ");
            // used to convert string to int
            int element;
            int count = 0;
            for(int i=0;i<puzzle.length;i++){
                for(int j=0;j<puzzle.length;j++){ 
                    element = Integer.parseInt(inputArray[count]);
                    count++;
                    // checks if 8-puzzle is valid
                    if(contains(puzzle,element) || inputArray.length != 9 || element < 0 || element > 8) {
                        puzzle = null;
                        return puzzle;
                    }
                    else
                        puzzle[i][j] = element;
                }
            }
        }
        return puzzle;
    }
    
    private boolean contains(Integer[][] puzzle,Integer element){
        for(int i=0;i<puzzle.length;i++){
           for(int j=0;j<puzzle.length;j++){ 
               if(puzzle[i][j].equals(element))
                   return true;
           }
        }
        return false;
    }
}
