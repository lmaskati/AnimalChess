import java.util.*;

public class Board {
    
    public Square[][] grid;
    private int col = 3;
    private int row = 4;
    public boolean isOn;

    public Board() {
        this.grid = new Square[row][col];
        this.isOn = true;
        initBoard();
        setBoard();
    }

    public void initBoard() {
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x ++) {
                grid[y][x] = new Square(x, y, new Piece(true, "."));
            }
        }
    }

    public void setBoard() {
        grid[0][0] = new Square(0, 0, new Giraffe(true, "g"));
        grid[0][1] = new Square(1, 0, new Lion(true, "l"));
        grid[0][2] = new Square(2, 0, new Elephant(true, "e"));
        grid[1][1] = new Square(1, 1, new Chicken(true, "c"));
        grid[2][1] = new Square(1, 2, new Chicken(false, "c"));
        grid[3][0] = new Square(0, 3, new Giraffe(false, "g"));
        grid[3][1] = new Square(1, 3, new Lion(false, "l"));
        grid[3][2] = new Square(2, 3, new Elephant(false, "e"));
    }

    public void displayBoard(Player p1, Player p2) {
         
        int num = 1;
        System.out.println("    a b c\n");
        
        for (int y = 0; y < row; y++) {
            for (int x = -1; x < col; x ++) {
                if (x == -1) {
                    System.out.print(num + "   ");
                    num++;
                }
                else {
                    System.out.print(grid[y][x].getPieceRep()+ " ");
                }
            }
            System.out.print('\n');
        }

        System.out.print('\n');
        System.out.print("Player 1's extra pieces: ");

        p1.getPieces().forEach((key,value) -> System.out.print(key + "  "));
 
        System.out.print('\n');
        System.out.print("Player 2's pieces: ");

        p2.getPieces().forEach((key,value) -> System.out.print(key + " "));
        System.out.print('\n');
    }
}
