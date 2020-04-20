import java.util.Scanner;
import java.util.Random;


public class Game {

    private static Board newB = new Board();
    private static Boolean gameOn = true;
    
    public Game() {
    }
    public static void main(String args[]) {
        //init players 
        //Human player1 = new Human(true);
        Player player1;
        Player player2;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Animal Chess!");

        System.out.println("If you don't have two players, you can play Animal Chess against the computer.");
        System.out.println("Would you like to play against the computer? (Y/N)");

        String s = scanner.nextLine();
        if (s.equals("Y")) {
            System.out.println("Would you like your difficulty setting to be easy (1), medium (2) or hard (3)? (1/2/3)?");
            s = scanner.nextLine();

            Random rd = new Random(); 
           // Boolean boolRand = rd.nextBoolean();
           Boolean boolRand = true;
            if (boolRand) {
                System.out.println("You have randomly been selected to be Player 2.");
                player1 = new AI(true, false);
                //set difficulty
                player1.setLevel(s);
                player2 = new Human(false, true);
            }
            else {
                System.out.println("You have randomly been selected to be Player 1.");
                player1 = new Human(true, true);
                player2 = new AI(false, false);
                player2.setLevel(s);
            }

        }
        else {
            player1 = new Human(true, true);
            player2 = new Human(false, true);
        }

        System.out.println("Would you like to read the instructions for this game? (Y/N)");
        s = scanner.nextLine();

        if (s.equals("Y"))
            printRules(newB, player1, player2);

        System.out.println('\n');
        
        System.out.println("Press any key to continue.\n");
        s = scanner.nextLine();

        while (gameOn) {

            if (!newB.isOn) {
                break;
            }
            
            //player1's turn
            //newB.displayBoard(player1, player2);
            //System.out.println("Player 1, it's your turn!");
            if (player1.human) {
                newB.displayBoard(player1, player2);
            }

            player1.nextMove(newB, scanner, player1.getPieces(), player2.getPieces());
            
            if (!newB.isOn) {
                break;
            }
            //player2's turn
            //newB.displayBoard(player1, player2);
           // System.out.println("Player 2's it's your turn!");
           if (player2.human) {
            
            newB.displayBoard(player1, player2);
        }
            player2.nextMove(newB, scanner, player1.getPieces(), player2.getPieces());
        }
        scanner.close();
        
    }

    public static void printRules(Board board, Player player1, Player player2) {
        System.out.println("INSTRUCTIONS: \n");
        System.out.println("- Each of the two players has four pieces already placed on the board:");
        System.out.println("- An elephant (e), a lion (l), a giraffe (g), and a chick (c)\n");
        System.out.println("- Player 1's pieces are denoted using lowercase, while Player 2's pieces are denoted using uppercase.\n");
        
        System.out.println("- The game board is arranged as below. Player 1's pieces are facing the bottom of the board; Player 2's pieces are facing the top of the board:\n");
        board.displayBoard(player1, player2);
        System.out.println('\n');
        
        System.out.println("- Players take turns moving a single piece:");
        System.out.println("  1. A chick moves one space forwards.");
        System.out.println("  2. An elephant moves one space in any diagonal direction (4 possibilities).");
        System.out.println("  3. A giraffe moves one space in any orthogonal direction (4 possibilities).");
        System.out.println("  4. A lion moves one space in any direction, either orthogonal or diagonal (8 possibilities).\n");
    
        System.out.println("- A player cannot move a piece onto one of their own pieces.");
        System.out.println("- But they may move onto one of their opponent’s pieces, in which case the opponent’s piece is captured.");
        System.out.println("- The capturing player takes the piece as one of their 'extra pieces'.");
        System.out.println("- On a future turn, a player may place an extra piece onto any empty space on the board as one of their own pieces.\n");
    
        System.out.println("- A player wins by capturing their opponent’s lion.\n");

        System.out.println("- Move a piece using the format 'ga2a3'. The order is: piece, starting column, starting row, ending column, ending row.\n");

        System.out.println("- If the piece is an extra piece, use the format 'g--c3', where the starting column and starting row are represented by '-'.");


    }
}
