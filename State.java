import java.util.HashMap;

public class State{
    public Square[][] grid;
    public HashMap<String, Integer> extraPiecesP1;
    public HashMap<String, Integer> extraPiecesP2;

    public State(Square[][] grid, HashMap<String, Integer> extraPiecesP1,
    HashMap<String, Integer> extraPiecesP2) {
        this.extraPiecesP1 = extraPiecesP1;
        this.extraPiecesP2 = extraPiecesP2;
        this.grid = grid;
    }

    public int getPosVal(Integer pos, Boolean p1) {
        if (p1) {
            return pos;
        }
        else {
            return (3 - pos);
        }
    }

    public int repVal(String rep) {
        String lowerRep = rep.toLowerCase();
        if (lowerRep.equals("c")) {
            return 1;
       }
        else if (lowerRep.equals("g")) {
            return 3;
        }
        else if (lowerRep.equals("e")) {
            return 2;
        }
        else if (lowerRep.equals("l")) {
            return 100000;
        }
        else {
            return 0;
        }
    }

    public void valState() {
        int score1 = 0;
        int score2 = 0;
        //iterate through grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                //get piece
                if (!(grid[i][j].getPieceRep().equals("."))) {
                    Piece curPiece = grid[i][j].getPiece();
                    if (curPiece.player1) {
                        //add piece val
                        score1 += curPiece.getVal();
                        //add pos val
                        int v1 = getPosVal(i, true);
                        score1 += v1;
                    }
                    else {  
                        //add vals
                        score2 += curPiece.getVal();
                        score2 += getPosVal(i, false);
                    }
                }
            }
        }
        //look at extra pieces
        for (HashMap.Entry<String, Integer> entry : extraPiecesP1.entrySet()) {
            score1 += repVal(entry.getKey()) * entry.getValue();
		}
        for (HashMap.Entry<String, Integer> entry : extraPiecesP2.entrySet()) {
            score2 += repVal(entry.getKey()) * entry.getValue();
		}
        System.out.println(score1);
        System.out.println(score2);
    }
}