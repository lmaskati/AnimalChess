import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Scanner;

public class AI extends Player{

    public int bestValue;
    public int xStart;
    public int yStart;
    public int xEnd;
    public int yEnd;
    public String finalRep;
    public int level;

    public AI(boolean player1, boolean human){
        super(player1, human);
        this.bestValue = 0;
        this.xStart = -1;
        this.yStart = -1;
        this.xEnd = -1;
        this.yEnd = -1;
        this.finalRep = ".";
        this.level = 1;
    }

    public HashMap<String, Integer> addingPiece(HashMap<String, Integer> pExtra, String killedPieceRep){
        Integer count = pExtra.get(killedPieceRep);
        if (count != null) {
            pExtra.put(killedPieceRep, count + 1);
        }
        else {
            pExtra.put(killedPieceRep, 1);
        }
        return pExtra;
    }

    public HashMap<String, Integer> remPiece(HashMap<String, Integer> pExtra, String rep) {
        int num = pExtra.get(rep);
            if (num > 1) {
                pExtra.replace(rep, num - 1); 
            }
            else {
                pExtra.remove(rep);
            }
            return pExtra;
    }

    @Override
    public void setLevel(String level) {
        if (level.equals("3")) {
            this.level = 3;
        }
        else if (level.equals("2")) {
            this.level = 2;
        }
        else {
            this.level = 1;
        }
    }

    @Override
    public void nextMove(Board board, Scanner scanner, HashMap<String, Integer> extra1, HashMap<String, Integer> extra2) {
        
        State testState = new State(board.grid, extra1, extra2);
        int best = genStates(level, testState, player1);

        System.out.println(xStart);
        System.out.println(yStart);
        System.out.println(xEnd);
        System.out.println(yEnd);
        System.out.println(finalRep);
        if (xStart == -1 && yStart == -1) {
            //remove finalRep from extra pieces
            extraPieces = remPiece(extraPieces, finalRep);
            //make finalRep a piece put it on board end
            board.grid[yEnd][xEnd].setPiece(pieceFromRep(finalRep));
            return;
        }
        //get end piece
        Piece endPiece = board.grid[yEnd][xEnd].getPiece();

        if (endPiece.rep.equals(".")) {    
            Piece defPiece = new Piece(true, ".");
            board.grid[yStart][xStart].setPiece(defPiece);
            board.grid[yEnd][xEnd].setPiece(pieceFromRep(finalRep));
        }
        else {
            endPiece.setPlayer(player1);
            addPiece(endPiece.rep);
            Piece defPiece = new Piece(true, ".");
            board.grid[yStart][xStart].setPiece(defPiece);
            board.grid[yEnd][xEnd].setPiece(pieceFromRep(finalRep));
        }

        if (endPiece.rep.equals("L") || endPiece.rep.equals("l")) {
            board.isOn = false;
            if (player1) {
                System.out.println("Player 1 wins!");
            }
            else {
                System.out.println("Player 2 wins!");
            }
        }
    }

    public Integer genStates(Integer depth, State state, Boolean isP1) {

        int bestValue;
        if (isP1 == player1) 
            bestValue = Integer.MIN_VALUE;
        else
            bestValue = Integer.MAX_VALUE;

        if (depth == 0) {
            return state.valState();
        }
        Square[][] grid = state.grid;
        HashMap<String, Integer> p1extra = state.extraPiecesP1;
        HashMap<String, Integer> p2extra = state.extraPiecesP2;
        
        //iterate through grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                //if starting position is blank
                if (grid[i][j].getPieceRep().equals(".")) {
                    //if isP1:
                    if (isP1) {
                        //iterate through P1's stuff
                        Set<String> keySet = p1extra.keySet(); 
                        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);

                        for (int ind = 0; ind < listOfKeys.size(); ind++) {
                            //make pieceRep a piece
                            String pieceRep = listOfKeys.get(ind);
                            Piece newPiece = pieceFromRep(pieceRep);
                            //place piece on grid
                            grid[i][j].setPiece(newPiece);
                            p1extra = remPiece(p1extra, pieceRep);
                            State curState = new State(grid, p1extra, p2extra);

                            int val = genStates(depth-1, curState, !isP1);
                            if (isP1 == player1) {
                                if ((val > bestValue) && depth == level) {
                                    xStart = -1;
                                    yStart = -1;
                                    xEnd = j;
                                    yEnd = i;
                                    finalRep = pieceRep;

                                }
                                bestValue = Math.max(val, bestValue);
                            }
                            else {
                                bestValue = Math.min(val, bestValue);
                            }

                            p1extra = addingPiece(p1extra, pieceRep);
                            Piece defPiece = new Piece(true, ".");
                            grid[i][j].setPiece(defPiece);
                        }
                    }
                    else {
                        //repeat for p2
                        Set<String> keySet = p2extra.keySet(); 
                        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);

                        for (int ind = 0; ind < listOfKeys.size(); ind++) {
                            String pieceRep = listOfKeys.get(ind);
                            Piece newPiece = pieceFromRep(pieceRep);
                            grid[i][j].setPiece(newPiece);
                            p2extra = remPiece(p2extra, pieceRep);
                            //do whatever with state here!!!!!!!!
                            State curState = new State(grid, p1extra, p2extra);
                            int val = genStates(depth-1, curState, isP1);
                            if (isP1 == player1) {
                                if ((val > bestValue) && depth == level) {
                                    xStart = -1;
                                    yStart = -1;
                                    xEnd = j;
                                    yEnd = i;
                                    finalRep = pieceRep;
                                }
                                bestValue = Math.max(val, bestValue);
                            }
                            else {
                                bestValue = Math.min(val, bestValue);
                            }
                            p2extra = addingPiece(p2extra, pieceRep);
                            Piece defPiece = new Piece(true, ".");
                            grid[i][j].setPiece(defPiece);
                        }
                    }
                }
                //if starting position is player's piece
                else if (grid[i][j].getPiece().player1 == isP1) {
                    Piece curPiece = grid[i][j].getPiece();
                    ArrayList<Point> allPos = curPiece.getAllPositions(j, i);
                    //iterate through Array List
                    for (int k = 0; k < allPos.size(); k++) {
                        Point curPos = allPos.get(k);
                        Piece endPiece = grid[curPos.y][curPos.x].getPiece();
                        //if ending position is blank
                        if (endPiece.getRep().equals(".")) {
                            //new grid
                            grid[curPos.y][curPos.x].setPiece(curPiece);
                            Piece defPiece = new Piece(true, ".");
                            grid[i][j].setPiece(defPiece);
                            //newState.valState();
                            State curState = new State(grid, p1extra, p2extra);
                          //!!!!!!!  genStates(depth-1, newState, !isP1);
                            int val = genStates(depth-1, curState, !isP1);
                            if (isP1 == player1) {
                                if ((val > bestValue) && depth == level) {
                                    xStart = j;
                                    yStart = i;
                                    xEnd = curPos.x;
                                    yEnd = curPos.y;
                                    finalRep = curPiece.rep;
                                }
                                bestValue = Math.max(val, bestValue);
                            }
                            else {
                                bestValue = Math.min(val, bestValue);
                            }
                            
                            grid[i][j].setPiece(curPiece);
                            grid[curPos.y][curPos.x].setPiece(endPiece);
                        }
                        //if ending position is a piece of a diff player
                        else if (endPiece.player1 != isP1) {
                            grid[curPos.y][curPos.x].setPiece(curPiece);
                            Piece defPiece = new Piece(true, ".");
                            grid[i][j].setPiece(defPiece);
                            endPiece.setPlayer(isP1);
                            //if end piece is P1 add to newExtra1
                            if (endPiece.player1)
                                addingPiece(p1extra, endPiece.getRep());
                            else 
                                addingPiece(p2extra, endPiece.getRep());
                            //ADD STATE
                            State newState = new State(grid, p1extra, p2extra);
                            //newState.valState();
                            int val = genStates(depth-1, newState, !isP1);
                            if (isP1 == player1) {
                                if ((val > bestValue) && depth == level) {
                                    xStart = j;
                                    yStart = i;
                                    xEnd = curPos.x;
                                    yEnd = curPos.y;
                                    finalRep = curPiece.rep;
                            
                                }
                                bestValue = Math.max(val, bestValue);
                            }
                            else {
                                bestValue = Math.min(val, bestValue);
                            }

                            //change player of endpiece
                            if (endPiece.player1)
                                remPiece(p1extra, endPiece.getRep());
                            else 
                                remPiece(p2extra, endPiece.getRep());
                            endPiece.setPlayer(!isP1);
                            grid[curPos.y][curPos.x].setPiece(endPiece);
                            grid[i][j].setPiece(curPiece);
                        }
                    } 
                }
            }
        }
        this.bestValue = bestValue;
        return bestValue;
    }

}
