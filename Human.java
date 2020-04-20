import java.util.HashMap;
import java.util.Scanner;

public class Human extends Player{

    

    public Human(boolean player1, boolean human){
        super(player1, human);
    }

    @Override
    public void nextMove(Board board, Scanner scanner, HashMap<String, Integer> extra1, HashMap<String, Integer> extra2) {
        if (player1)
            System.out.println("Player 1, it's your turn!");
        else
            System.out.println("Player 2, it's your turn!");

        System.out.println("Type 'help' anytime if you forgot how the pieces move.");

        String s = scanner.nextLine();

        while (true) {
            if (s.equals("help")) {
                System.out.println('\n');
                displayMoves();
                s = scanner.nextLine();
                System.out.println('\n');
            }
            while (s.length() != 5) {
                System.out.println("Please format your move correctly.");
                s = scanner.nextLine();
            }

            String piece = Character.toString(s.charAt(0));
            String startX = Character.toString(s.charAt(1));
            String startY = Character.toString(s.charAt(2));
            String endX = Character.toString(s.charAt(3));
            String endY = Character.toString(s.charAt(4));

            String pieces = "gGcCeElL";
            String xVals = "abc";
            String yVals = "1234";

        //check if it is an extra piece
            if ((pieces.contains(piece)) && xVals.contains(endX) && yVals.contains(endY)
            && (startX.equals("-")) && (startX.equals("-"))) {
                if (moveExtra(board, piece, convertX(endX), convertY(endY))) {
                    break;
                }
                else {
                    s = scanner.nextLine();
                }
            }
         //   check if it a piece on the board
            if ((pieces.contains(piece)) && xVals.contains(startX) && xVals.contains(endX)
            && yVals.contains(startY) && yVals.contains(endY)) {
                if(makeMove(board, piece, convertX(startX), convertY(startY), convertX(endX), convertY(endY))) {
                    break;
                }
                else {
                    s = scanner.nextLine();
                }
            }

                else {
                    System.out.println("Please choose a valid piece type, row and column.");
                    s = scanner.nextLine();
                }
        }
    }
        public boolean moveExtra(Board board, String rep, int endX, int endY) {
        if (extraPieces.containsKey(rep)) {

            Piece curPiece = pieceFromRep(rep);
            Piece endPiece = board.grid[endY][endX].getPiece();
            if (!endPiece.rep.equals(".")) {
                System.out.println("An extra piece can only be placed on an empty space.");
                return false;
            }
            else {
                board.grid[endY][endX].setPiece(curPiece);
            }
            int num = getPieces().get(rep);
            if (num > 1) {
                getPieces().replace(rep, num - 1); 
            }
            else {
                getPieces().remove(rep);
            }
            return true;
        }
        else {
            System.out.println("Sorry this piece is not one of your extra pieces.");
            return false;
        }
    }

    public static int convertX(String xVal) {
        if (xVal.equals("a")) {
            return 0;
        }
        if (xVal.equals("b")) {
            return 1;
        }
        if (xVal.equals("c")) {
            return 2;
        }
        return -1;
    }

    public static int convertY(String yVal) {
        if (yVal.equals("1")) {
            return 0;
        }
        if (yVal.equals("2")) {
            return 1;
        }
        if (yVal.equals("3")) {
            return 2;
        }
        if (yVal.equals("4")) {
            return 3;
        }
        return -1;
    }
    

    public boolean makeMove(Board board, String rep, int startX, int startY, int endX, int endY) {
        //check piece rep on grid[startY][startX]
        Piece curPiece = board.grid[startY][startX].getPiece();
        String curRep = curPiece.rep;
        //if piece rep doesn't math rep return false
        if (curRep.equals(rep) == false) {
            System.out.println("The piece you specified is neither an extra piece nor on the start position you specified.");
            return false;
        }

        if (curPiece.player1 != player1) {
            System.out.println("Sorry, you can only move your own piece. Please try again.");
            return false;
        }
        if (curPiece.isValid(startX, startY, endX, endY) == false) {
            System.out.println("Sorry, that was an invalid move. Please try again.");
            return false;
        }

        //check the piece at end position
        if (checkEndPiece(board, endX, endY, curPiece)) {
            //set piece on start pos to be default piece 
            Piece defPiece = new Piece(true, ".");
            board.grid[startY][startX].setPiece(defPiece);
            return true;
        }
        return false;
    }

    public boolean checkEndPiece(Board board, int endX, int endY, Piece curPiece) {
        Piece endPiece = board.grid[endY][endX].getPiece();
        if (!endPiece.rep.equals(".")) {
            
            if (endPiece.player1 == player1) {
                System.out.println("You can't kill your own piece. Please try again.");
                return false;
            }
            else {
                //add end piece to players extra piece
                endPiece.setPlayer(!endPiece.player1);
                addPiece(endPiece.rep);
            }
        }

        //set grid[endY][endX] to piece on start pos
        board.grid[endY][endX].setPiece(curPiece);
        //check for winning move
        if (endPiece.rep.equals("L") || endPiece.rep.equals("l")) {
            board.isOn = false;
            if (player1) {
                System.out.println("Player 1 wins!");
            }
            else {
                System.out.println("Player 2 wins!");
            }
        }
        //return true
        return true;
    }
}
