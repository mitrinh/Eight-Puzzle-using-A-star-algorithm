// Michael Trinh
// helper class to print 8-Puzzles

import java.util.List;

/**
 *
 * @author Michael Trinh
 */
public class puzzlePrinter {
    
    public puzzlePrinter(){};
    
    public void printPuzzle(Integer[][] puzzle){
        for(int i=0; i<puzzle.length;i++){
            for(int j=0; j<puzzle.length;j++){
                System.out.print(puzzle[i][j] + " ");
            }
        }
        System.out.println("");
    }
    
    public void printPuzzles(List<Integer[][]> puzzleList){
        for(Integer[][] puzzle:puzzleList){
            printPuzzle(puzzle);
        }
    }
}
