import java.util.ArrayList;
public class Piece {
    public boolean player1;
    public String rep;

    public Piece(boolean player1, String rep) {
        super();
        this.player1 = player1;
        this.rep = rep.toUpperCase();
        if (player1) {
            this.rep = rep.toLowerCase();
        }
    }

    public void setPlayer(boolean player1) {
        this.player1 = player1;
        if (player1) {
            this.rep = rep.toLowerCase();
        }
        else {
            this.rep = rep.toUpperCase();
        }
    }

    public boolean getPlayer() {
        return player1;
    }

    public String getRep() {
        return rep;
    }

    public boolean isValid(int startX, int startY, int endX, int endY) {
        if(startX == endX && startY == endY)
            return false; 
        if(startX < 0 || startX > 2 || endX < 0 || endX > 2 || startY < 0 || startY > 3 || endY < 0 || endY > 3)
            return false;
        return true;
    }

    public int getVal() {
        return 0;
    }

    public ArrayList<Point> getAllPositions(Integer x, Integer y) {
        ArrayList<Point> result = new ArrayList<Point>();
        return result;
    } 
}