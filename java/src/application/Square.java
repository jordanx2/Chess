package application;

import application.Pieces.Piece;

public class Square {
    private String id;
    private int color;
    private final int originalColor;
    private Piece piece;
    private float x;
    private float y;
    private boolean isSelected;
    private boolean enpassantSquare;

    public Square(String id, int color, float x, float y, Piece piece) {
        this.id = id;
        this.color = color;
        this.originalColor = color;
        this.x = x;
        this.y = y;
        this.piece = piece;
        this.isSelected = false;
        this.enpassantSquare = false;
    }

    public boolean[] retrievePossibleMoves(Square[] squares){
        return this.piece.possibleMoves(squares, this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getOriginalColor() {
        return originalColor;
    }

    @Override
    public String toString() {
        return "Square [id=" + id + ", color=" + color + ", piece=" + piece + ", x=" + x + ", y=" + y + "]";
    }

    public boolean isEnpassantSquare() {
        return enpassantSquare;
    }

    public void setEnpassantSquare(boolean enpassantSquare) {
        this.enpassantSquare = enpassantSquare;
    }

}
