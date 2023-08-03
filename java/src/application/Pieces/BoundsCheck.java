package application.Pieces;

public class BoundsCheck {

    public BoundsCheck(){}

    public boolean checkBounds(int squareIndex, int moveIndex, int length){
        int kingWrap = Math.abs((squareIndex % 8) - (moveIndex % 8));
        if(moveIndex >= 0 && moveIndex < length && kingWrap < 3 ){
            return true;
        }

        return false;
    }
    
}
