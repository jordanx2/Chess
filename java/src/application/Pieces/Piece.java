package application.Pieces;

import application.Rules;
import application.Square;

public abstract class Piece {
    private float size;
    private PieceType pieceName;
    private String pieceUnicode;
    private String color;
    private String imgPath;
    private boolean[] moves;
    private int step;
    private BoundsCheck check;
    private Movement movement;
    Rules rules;

    public Piece(PieceType pieceName, String pieceUnicode, String color, String imgPath) {
        this.pieceName = pieceName;
        this.pieceUnicode = pieceUnicode;
        this.color = color;
        this.imgPath = imgPath;
        this.size = 85;
        this.moves = new boolean[64];

        // With black pawns we increment, with white pawns we decrement
        this.step = (color.equals("WHITE")) ? -1 : 1;
        this.check = new BoundsCheck();
        this.movement = new Movement();
        this.rules = Rules.getInstance();
    }

    public abstract boolean[] possibleMoves(Square[] board, Square square);

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getPieceUnicode() {
        return pieceUnicode;
    }

    public void setPieceUnicode(String pieceUnicode) {
        this.pieceUnicode = pieceUnicode;
    }

    public PieceType getPieceName() {
        return pieceName;
    }

    public void setPieceName(PieceType pieceName) {
        this.pieceName = pieceName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean[] getMoves() {
        return moves;
    }

    public void setMoves(boolean[] moves) {
        this.moves = moves;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public BoundsCheck getCheck() {
        return check;
    }

    public void setCheck(BoundsCheck check) {
        this.check = check;
    }
    
    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }
    

    @Override
    public String toString() {
        return "Piece [size=" + size + ", pieceName=" + pieceName + ", pieceUnicode=" + pieceUnicode + ", color="
                + color + ", imgPath=" + imgPath + "]";
    }
    
}
