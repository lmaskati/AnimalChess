import java.util.ArrayList;

public class Chicken extends Piece{

    public Chicken(boolean player1, String rep) {
        super(player1, rep);
    }

    @Override
    public boolean isValid(int fromX, int fromY, int toX, int toY) {
        if(super.isValid(fromX, fromY, toX, toY) == false)
            return false;
        if(player2 && (toY == fromY - 1) && (toX == fromX)){
            return true;
        }
        if(player1 && (toY == fromY + 1) && (toX == fromX)){
            return true;
        }
        return false;
    }
    public void setRep(String rep) {
        this.rep = rep;
    }

    @Override
    public int getVal() {
        return 1;
    }

    @Override
    public ArrayList<Point> getAllPositions(Integer x, Integer y) {
        ArrayList<Point> result = new ArrayList<Point>();
        if (player1 && (y+1 < 4)) {
            Point addPoint = new Point(x, y+1);
            result.add(addPoint);
        }
        if (player2 && (y-1 >= 0)) {
            Point addPoint = new Point(x, y-1);
            result.add(addPoint);
        }
        return result;
    }

}