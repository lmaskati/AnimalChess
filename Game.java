import java.util.Scanner;

public class Game {

    private static Board newB = new Board();
    private static Boolean gameOn = true;
    
    public Game() {
    }
    public static void main(String args[]) {
        //init players 
        Human player1 = new Human(true);
        Human player2 = new Human(false);
        
        Scanner scanner = new Scanner(System.in);
        while (gameOn) {

            if (!newB.isOn) {
                break;
            }
            
            //player1's turn
            newB.displayBoard(player1, player2);
            System.out.println("Player 1, it's your turn!");
            player1.nextMove(newB, scanner);
            //player2's turn
            newB.displayBoard(player1, player2);
            System.out.println("Player 2's it's your turn!");
            player2.nextMove(newB, scanner);
        }
        scanner.close();
        
    }

    // public static void singeMove(Player curPlayer, Scanner scanner) {

    //     //scanner to read user input

    //         //get user input
    //         String s = scanner.nextLine();

    //         while (true) {

    //             while (s.length() != 5) {
    //                 System.out.println("Please format your move correctly.");
    //                 s = scanner.nextLine();
    //             }

    //             String piece = Character.toString(s.charAt(0));
    //             String startX = Character.toString(s.charAt(1));
    //             String startY = Character.toString(s.charAt(2));
    //             String endX = Character.toString(s.charAt(3));
    //             String endY = Character.toString(s.charAt(4));

    //             String pieces = "gGcCeElL";
    //             String xVals = "abc";
    //             String yVals = "1234";

    //             //check if it is an extra piece
    //             if ((pieces.contains(piece)) && xVals.contains(endX) && yVals.contains(endY)
    //             && (startX.equals("-")) && (startX.equals("-"))) {
    //                 if (newB.moveExtra(curPlayer, piece, convertX(endX), convertY(endY))) {
    //                     break;
    //                 }
    //                 else {
    //                     s = scanner.nextLine();
    //                 }
    //             }
    //             //check if it a piece on the board
    //             if ((pieces.contains(piece)) && xVals.contains(startX) && xVals.contains(endX)
    //             && yVals.contains(startY) && yVals.contains(endY)) {
    //                 if(newB.makeMove(curPlayer, piece, convertX(startX), convertY(startY), convertX(endX), convertY(endY))) {
    //                     break;
    //                 }
    //                 else {
    //                     s = scanner.nextLine();
    //                 }
    //             }

    //             else {
    //                 System.out.println("Please choose a valid piece type, row and column.");
    //                 s = scanner.nextLine();
    //             }
    //     }
    // }

    // public static int convertX(String xVal) {
    //     if (xVal.equals("a")) {
    //         return 0;
    //     }
    //     if (xVal.equals("b")) {
    //         return 1;
    //     }
    //     if (xVal.equals("c")) {
    //         return 2;
    //     }
    //     return -1;
    // }

    // public static int convertY(String yVal) {
    //     if (yVal.equals("1")) {
    //         return 0;
    //     }
    //     if (yVal.equals("2")) {
    //         return 1;
    //     }
    //     if (yVal.equals("3")) {
    //         return 2;
    //     }
    //     if (yVal.equals("4")) {
    //         return 3;
    //     }
    //     return -1;
    // }

}