import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AI extends Player{

    public AI(boolean player1){
        super(player1);
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

    //next move method
        //create state
        //for each state in genState(state)
            //getVal of state
        //res = state w highest value

        //set board to state.board
        //update AI extra layers 

    //getVal methdod (returns a num)

    public void genStates(State state, Boolean isP1) {
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
                            //PRINT!!
                            printStuff(grid);
                            State curState = new State(grid, p1extra, p2extra);
                            curState.valState();
                            //revert
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
                            State newState = new State(grid, p1extra, p2extra);
                            newState.valState();
                            printStuff(grid);
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
                            newState.valState();
                            printStuff(grid);
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
    }

    public void printStuff(Square[][] grid) {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 3; x ++) {
                System.out.print(grid[y][x].getPieceRep()+ " ");
            }
            System.out.print('\n');
        }
        System.out.print('\n');
    }
}
