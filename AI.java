import java.util.ArrayList;
import java.util.HashMap;

public class AIPlayer extends Player{

    public AIPlayer(boolean player1){
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


    //genStates method (back tracking)
    public ArrayList<State> genStates(State state, Boolean isP1) {
        //iget values
        Square[][] curGrid = state.grid;
        HashMap<String, Integer> p1extra = state.extraPiecesP1;
        HashMap<String, Integer> p2extra = state.extraPiecesP2;

        ArrayList<State> finalList = new ArrayList<State>();

        //iterate through grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                //if starting position is blank
                if (grid[i][j].getPieceRep().equals(".")) {
                    //if isP1:
                        //iterate through P1's stuff
                        //make pieceRep a piece
                        //place piece on grid
                        //rem pieceRep from new piece1list
                        //add grid, p1, p2 to state
                        //add pieceRep to p1
                        //set grid spot to default
                    //else
                        //iterate through P2's stuff
                        //make pieceRep a piece
                        //place piece on grid
                        //rem pieceRep from new p2
                        //add grid, p1, p2 to state
                        //add pieceRep to p2
                        //set grid spot to default
                }
                //if starting position is player's piece
                else if (grid[i][j].getPiece.player1 == isP1) {
                    Piece curPiece = grid[i][j].getPiece();
                    ArrayList<Point> allPos = curPiece.getAllPositions(j, i);
                    //iterate through Array List
                    for (int k = 0; k < allPos.size(); k++) {
                        Point curPos = allPos.get(k);
                        Piece endPiece = grid[curPos.y][curPos.x];
                        //if ending position is blank
                        if (endPiece.getRep().equals(".")) {
                            //new grid
                            Square[][] newGrid = grid; 
                            newGrid[curPos.y][curPos.x] = curPiece;
                            newGrid[i][j] = curPiece;
                            State newState = State(newGrid, p1extra, p2extra);
                            finalList.add(newState);
                        }
                        //if ending position is a piece of a diff player

                    } 
                        //elif player1 is diff:
                            //grid[end][end] = curPiece
                            //grid[now] = def piece
                            //change player of endPiece
                            //newL1
                            //newL2
                            //if end piece is P1 
                                //add to newL1
                            //else 
                                //add to newL2
                            //ADD STATE
                            //change player of endpiece
                            //grid[end][end] = endPiece
                            //grid[now] = curPiece
                }
            }
        }
    }
}