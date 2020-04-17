import java.util.ArrayList;
public class Giraffe extends Piece{

    public Giraffe(boolean player1, String rep) {
        super(player1, rep);
    }

    @Override
    public boolean isValid(int fromX, int fromY, int toX, int toY) {
        if(super.isValid(fromX, fromY, toX, toY) == false)
            return false;
        if((toX == fromX && (((fromY - 1) <= toY) || (toY <= (fromY + 1))))){
            return true;
        }
        if((toY == fromY && (((fromX - 1) <= toX) || (toX <= (fromX + 1))))){
            return true;
        }
        return false;
    }
    public void setRep(String rep) {
        this.rep = rep;
    }

    @Override
    public int getVal() {
        return 3;
    }

    public ArrayList<Point> getAllPositions(Integer x, Integer y) {
        ArrayList<Point> result = new ArrayList<Point>();
        if (y+1 < 4) {
            Point addPoint = new Point(x, y+1);
            result.add(addPoint);
        }
        if (x+1 < 3) {
            Point addPoint = new Point(x+1, y);
            result.add(addPoint);
        }
        if (y-1 >= 0) {
            Point addPoint = new Point(x, y-1);
            result.add(addPoint);
        }
        if (x-1 >= 0) {
            Point addPoint = new Point(x-1, y);
            result.add(addPoint);
        }
        return result;
    }
}