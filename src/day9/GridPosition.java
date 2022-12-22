package day9;

public record GridPosition(int x, int y) {

    public enum Dir{
        X,Y
    }

    public int distanceFrom(GridPosition other){
        return Math.abs(other.x - x) + Math.abs(other.y - y);
    }

    public boolean mustMove(GridPosition other){
        // must move if distance is 2 or more
        // two positions to the right
        return distanceFrom(other) > 1;
    }

    public boolean isOffsetDiagonally(GridPosition other){
        return ((other.x - x)!=0) && ((other.y - y) != 0);
    }

    public GridPosition getMove(GridPosition other){
        int xOffset = other.x - x;
        int yOffset = other.y - y;
        int xMove = 0;
        int yMove = 0;

        if(Math.abs(xOffset) > 1){
            xMove = (int) (1 * Math.signum(xOffset));
            if(isOffsetDiagonally(other)) yMove = (int) (1 * Math.signum(yOffset));
        } else if(Math.abs(yOffset) > 1){
            yMove = (int) (1 * Math.signum(yOffset));
            if(isOffsetDiagonally(other)) xMove = (int) (1 * Math.signum(xOffset));
        }
        return new GridPosition(xMove, yMove);

    }

    public GridPosition moveBy(GridPosition move){
        return new GridPosition(x+move.x, y+move.y);
    }


}
