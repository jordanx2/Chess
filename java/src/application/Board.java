package application;

import java.util.ArrayList;
import java.util.Arrays;
import application.Pieces.*;
import processing.core.PApplet;
import processing.core.PImage;

public class Board{
    PApplet p;
    PImage pieceImg;
    private float squareW;
    private float border;
    private Square[] squares = new Square[64];
    private boolean[] potentialMoves = new boolean[64];
    private boolean whiteStart;
    private float half;
    private ArrayList<String> movesMade;
    private int moveCounterLog;
    Square lastEnpassantSquare;
    int enpassantStep = 0;
    String movex;
    Rules rules;
    int buffSize = 2;
    String[] buffer;
    int pointer = 0;
    PromotionBlock promotionBlock = PromotionBlock.getInstance();
    

    public Board(PApplet p, boolean whiteStart){
        this.p = p;
        this.border = 90f;
        this.whiteStart = whiteStart;
        float scale = 0.72f;

        this.squareW = (p.width / 8) * scale;
        this.half = squareW / 2;
        this.movesMade = new ArrayList<>();
        this.moveCounterLog = 1;
        movex = String.valueOf(moveCounterLog + ". ");
        lastEnpassantSquare = null;
        rules = Rules.getInstance();

        buffer = new String[buffSize];
        buffer[0] = String.valueOf(moveCounterLog);
        
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int c;
                if ((i + j) % 2 == 0) {
                    c = p.color(218, 217, 181);
                } else {
                    c = p.color(128, 163, 82);
                }
                
                if(whiteStart){
                    squares[j + (i * 8)] = new Square((toChar(i + 1) + String.valueOf(8 - j)).toLowerCase(), c, i * squareW + border, j * squareW + border, null);
                } else{
                    squares[j + (i * 8)] = new Square((toChar(i + 1) + String.valueOf(j + 1)).toLowerCase(), c, i * squareW + border, j * squareW + border, null);
                }
                Piece calcPiece = calculatePiece(squares[j + (i * 8)]);

                if(calcPiece != null){
                    calcPiece.setSize(squareW);
                    squares[j + (i * 8)].setPiece(calcPiece);
                }
                

            }
        }
    }

    public void applyMove(int moveIndex, int previousIndex){
        SpecialFlags flag1 = null;

        // Flag2 is for when there is a capture as well as a check for example
        SpecialFlags flag2 = null;
        Piece p = squares[previousIndex].getPiece();

        // Piece taken via enpassant
        if(squares[moveIndex].isEnpassantSquare() && p instanceof Pawn){
            int i = Arrays.asList(squares).indexOf(lastEnpassantSquare);
            squares[i + enpassantStep].setPiece(null);
            squares[moveIndex].setEnpassantSquare(false);
            flag1 = SpecialFlags.CAPTURE;
        }else{
            if(lastEnpassantSquare != null){
                lastEnpassantSquare.setEnpassantSquare(false);
                lastEnpassantSquare = null;
            }
        }

        // Check if a capture has been made
        if(squares[moveIndex].getPiece() != null)
            flag1 = SpecialFlags.CAPTURE;
            flag2 = SpecialFlags.CAPTURE;

        if(p instanceof Pawn){
            // Check to see if the pawn moved two squares
            if(Math.abs(moveIndex - previousIndex) == 2){
                lastEnpassantSquare = squares[previousIndex + p.getStep()];
                enpassantStep = p.getStep();
                lastEnpassantSquare.setEnpassantSquare(true);
            }

            if(((Pawn) p).isPawnPromotion(moveIndex) && !PromotionBlock.pawnPromotionOptions){
                PromotionBlock.pawnPromotionOptions = true;
                promotionBlock.blockSpawnX = this.p.mouseX;
                promotionBlock.blockSpawnY = this.p.mouseY;
                promotionBlock.prevPromoteSquare = previousIndex;
                promotionBlock.newPromoteSquare = moveIndex;
                PromotionBlock.pieceTeam = p.getColor();
                promotionBlock.setDisplayImages();
                /*
                    Returns in order to display the promotion block
                    i.e., allow the player to select a promotion piece
                */ 
                return;
            }

            if(PromotionBlock.promotionAchieved){
                flag2 = SpecialFlags.PROMOTION;
                p = PromotionBlock.pawnPromotionPiece;
                promotionBlock.reset();
            }

            if(p instanceof Pawn){
                ((Pawn) p).setStartingPos(false);
            }


        }

        if(p instanceof King){
            /*
             * Check KING SIDE CASTLING
             * 8 indicates 1 row over, 16 indicates two rows over etc
             * The square index is the move selected by the player 
             * 
             * If one of the potential moves is a castling indicating move
             * then it is executed
             */
            
            int kingIndex = previousIndex;
            if((kingIndex + 16) == moveIndex){
                flag1 = SpecialFlags.KING_SIDE_CASTLING;
                squares[kingIndex + 8].setPiece(squares[kingIndex + 24].getPiece());
                squares[kingIndex + 24].setPiece(null);
            }

            // Check QUEEN SIDE CASTLING
            if((kingIndex - 16) == moveIndex){
                flag1 = SpecialFlags.QUEEN_SIDE_CASTLING;
                squares[kingIndex - 8].setPiece(squares[kingIndex - 32].getPiece());
                squares[kingIndex - 32].setPiece(null);
            }

            ((King) p).setKingMoved(true);

        }

        if(p instanceof Rook && !((Rook) p).isHasRookMoved()){
            ((Rook) p).setHasRookMoved(true);
        }

        squares[previousIndex].setPiece(null);
        squares[moveIndex].setPiece(p);

        // Check to see if the move that is being made is a CHECK move
        if(rules.isInCheck(squares, moveIndex, -1)){
            if(rules.isCheckMate(squares, moveIndex)){
                flag1 = SpecialFlags.CHECK_MATE;
                System.out.println("CHECKMATE");
            } else{
                flag1 = SpecialFlags.CHECK;
            }
        }
        registerMove(previousIndex, moveIndex, flag1, flag2, true);
        
    }

    public void plotPotentialMoves(Square square){
        potentialMoves = rules.parseSafeMoves(squares, square, square.retrievePossibleMoves(squares));
        plotMoves();
    }

    private void plotMoves(){
        for(int i = 0; i < potentialMoves.length; i++){
            renderPotentialMoves(i);
        }
    }

    // RENDERING FUNCTIONS
    public void renderPotentialMoves(int index){
        if(potentialMoves[index] && squares[index].getPiece() == null){
            p.fill(100, 100, 100);
            p.circle(squares[index].getX() + half, squares[index].getY() + half, 25);
        }

        if(potentialMoves[index] && squares[index].getPiece() != null){
            // Color indicating that a king is in check
            if(squares[index].getPiece().getPieceName() == PieceType.KING){
                squares[index].setColor(p.color(255, 77, 77));
            } else{
                // Color indicating that a piece can be taken
                squares[index].setColor(p.color(217, 140, 217));
            }
        }
    }

    private char toChar(int u){  
        return (char)(u + 64);
    }

    private void renderPieces(){
        for(Square s : squares){
            Piece eachPiece = s.getPiece();
            if(eachPiece != null){
                pieceImg = p.loadImage(eachPiece.getImgPath());
                p.image(pieceImg, s.getX(), s.getY(), eachPiece.getSize(), eachPiece.getSize());
            }
        }
    }

    public void renderBoard(){
        p.background(0);
        for(int i = 0; i < 8; i++){
            p.stroke(255);
            float x = i * squareW + border;

            p.noStroke();
            for(int j = 0; j < 8; j++){
                Square s = squares[j + (i * 8)];
                float y = j * squareW + border;
                p.fill(s.getColor());
                p.square(x, y, squareW);
                renderPotentialMoves(j + (i * 8));
                p.fill(0);

                //testing
                // p.text(j + (i * 8), x + half, y + half);
            }
        }

        renderNumbering2();

        renderPieces();
    }
    
    private void renderNumbering2(){
        p.textSize(12);

        float bottomLetteringY = squareW * 8 + (border - 2);
        for(int i = 0; i < 8; i++){
            float x = i * squareW + border + 2;
            p.text(String.valueOf(toChar(i + 1)).toLowerCase(), x, bottomLetteringY);
        }

        float leftNumberingX = squareW * 7 + border + squareW - 10;
        for(int i = 0; i < 8; i++){
            p.text(i + 1, leftNumberingX, bottomLetteringY - (squareW * i) - squareW + 15);
        }
    }

    private void registerMove(int prevSquare, int newSquare, SpecialFlags flag, SpecialFlags flag2,  boolean whiteToMove){
        String prevPieceName = "";
        String movePieceName = "";
        movex = "";

        String prevSquareName = squares[prevSquare].getId();
        String moveSquareName = squares[newSquare].getId();

        // Check to see if there are a piece on either of the two squares
        if(squares[newSquare].getPiece() != null && squares[newSquare].getPiece().getPieceName() != PieceType.PAWN){
            movePieceName = String.valueOf(squares[newSquare].getPiece().getPieceName());
        }

        if(squares[prevSquare].getPiece() != null && squares[prevSquare].getPiece().getPieceName() != PieceType.PAWN){
            prevPieceName = String.valueOf(squares[prevSquare].getPiece().getPieceName());
        }


        if(prevPieceName != "" && !prevPieceName.matches("PAWN")){
            prevPieceName = (
                String.valueOf(squares[prevSquare].getPiece().getPieceName()).matches("KNIGHT") ? "N" 
                : String.valueOf(Character.toUpperCase(prevPieceName.charAt(0)))                          
            );
        } else{
            if(!prevPieceName.matches("PAWN")){
                prevPieceName += prevSquareName;
            }

        }

        if(movePieceName != "" && !movePieceName.matches("PAWN")){ 
            movePieceName = (
                movePieceName.matches("KNIGHT") ? "N" 
                : String.valueOf(Character.toUpperCase(movePieceName.charAt(0)))                          
            );

        }else{
            if(!movePieceName.matches("PAWN")){
                movePieceName += moveSquareName;
            }
        }

        if(flag != null){
            switch(flag){
                case KING_SIDE_CASTLING:
                    movex = "O-O";
                    break;

                case QUEEN_SIDE_CASTLING:
                    movex = "O-O-O";
                    break;

                case PROMOTION:
                    break;

                case CAPTURE:
                    // Example capture log - Nxd5
                    movex = prevPieceName.charAt(0) + "x" + moveSquareName;
                    break;

                case CHECK: 
                    if(flag2 == SpecialFlags.CAPTURE){
                        movex = prevPieceName.charAt(0) + "x" + moveSquareName + "+";
                    } else{
                        movex = prevPieceName.charAt(0) + moveSquareName + "+";
                    }
                    break;

                case CHECK_MATE:
                    if(flag2 == SpecialFlags.CAPTURE){
                        movex = prevPieceName.charAt(0) + "x" + moveSquareName + "#";
                    } else{
                        movex = prevPieceName.charAt(0) + moveSquareName + "#";
                    }
                    break;
            }

        }else{
            if(squares[prevSquare].getPiece() instanceof Pawn){
                // If its just a pawn move just register the move square i.e., d4, d5 etc
                movex = moveSquareName;
            } else{
                // If its a non pawn move concatenate the piece name and the move square i.e., Nd5
                movex = prevPieceName + moveSquareName;
            }
            
        } 

        if(pointer != 0 && (pointer % buffSize) == 0){
            movesMade.add(String.valueOf(moveCounterLog) + buffer[0] + " " + buffer[1]);
            moveCounterLog++;
            buffer[1] = null;
        }

        buffer[pointer % buffSize] = movex;
        pointer++;

        // printBuffer();

    }

    public void printBuffer(){
        System.out.print("\n" + moveCounterLog + ". ");
        for(String move : buffer){
            if(move != null){
                System.out.print(move + " ");
            }
        }
    } 

    private Piece calculatePiece(Square s){
        if(s == null) return null;
        char[] id = s.getId().toCharArray();
        String squareName = s.getId().toUpperCase();
        if(id[1] == '2'){
            return new Pawn(PieceType.PAWN, ChessSymbols.WHITE_CHESS_PAWN, "WHITE", ChessSymbols.WHITE_CHESS_PAWN_IMG);
        }

        if(id[1] == '7'){
            return new Pawn(PieceType.PAWN, ChessSymbols.BLACK_CHESS_PAWN, "BLACK", ChessSymbols.BLACK_CHESS_PAWN_IMG);
        }

        if(squareName.equals("E1")){
            return new King(PieceType.KING, ChessSymbols.WHITE_CHESS_KING, "WHITE", ChessSymbols.WHITE_CHESS_KING_IMG);
        }

        if(squareName.equals("D1")){
            return new Queen(PieceType.QUEEN, ChessSymbols.WHITE_CHESS_QUEEN, "WHITE", ChessSymbols.WHITE_CHESS_QUEEN_IMG);
        }

        if((squareName.equals("C1")) || squareName.equals("F1")){
            return new Bishop(PieceType.BISHOP, ChessSymbols.WHITE_CHESS_BISHOP, "WHITE", ChessSymbols.WHITE_CHESS_BISHOP_IMG);
        }

        if(squareName.equals("B1") || squareName.equals("G1")){
            return new Knight(PieceType.KNIGHT, ChessSymbols.WHITE_CHESS_KNIGHT, "WHITE", ChessSymbols.WHITE_CHESS_KNIGHT_IMG);
        }

        if(squareName.equals("A1") || squareName.equals("H1")){
            return new Rook(PieceType.ROOK, ChessSymbols.WHITE_CHESS_ROOK, "WHITE", ChessSymbols.WHITE_CHESS_ROOK_IMG);
        }

        if(squareName.equals("E8")){
            return new King(PieceType.KING, ChessSymbols.BLACK_CHESS_KING, "BLACK", ChessSymbols.BLACK_CHESS_KING_IMG);
        }

        if(squareName.equals("D8")){
            return new Queen(PieceType.QUEEN, ChessSymbols.BLACK_CHESS_QUEEN, "BLACK", ChessSymbols.BLACK_CHESS_QUEEN_IMG);
        }

        if(squareName.equals("C8") || squareName.equals("F8")){
            return new Bishop(PieceType.BISHOP, ChessSymbols.BLACK_CHESS_BISHOP, "BLACK", ChessSymbols.BLACK_CHESS_BISHOP_IMG);
        }

        if(squareName.equals("B8") ||squareName.equals("G8")){
            return new Knight(PieceType.KNIGHT, ChessSymbols.BLACK_CHESS_KNIGHT, "BLACK", ChessSymbols.BLACK_CHESS_KNIGHT_IMG);
        }

        if(squareName.equals("A8") || squareName.equals("H8")){
            return new Rook(PieceType.ROOK, ChessSymbols.BLACK_CHESS_ROOK, "BLACK", ChessSymbols.BLACK_CHESS_ROOK_IMG);
        }
        
        return null;
    }


    public float getSquareW() {
        return squareW;
    }

    public void setSquareW(float squareW) {
        this.squareW = squareW;
    }

    public float getBorder() {
        return border;
    }

    public void setBorder(float border) {
        this.border = border;
    }

    public Square[] getSquares() {
        return squares;
    }

    public void setSquares(Square[] squares) {
        this.squares = squares;
    }

    public boolean[] getPotentialMoves() {
        return potentialMoves;
    }

    public void setPotentialMoves(boolean[] potentialMoves) {
        this.potentialMoves = potentialMoves;
    }

    public float getHalf() {
        return half;
    }

    public void setHalf(float half) {
        this.half = half;
    }

    public ArrayList<String> getMovesMade() {
        return movesMade;
    }

    public void setMovesMade(ArrayList<String> movesMade) {
        this.movesMade = movesMade;
    }    

    public boolean isWhiteStart() {
        return whiteStart;
    }

    public void setWhiteStart(boolean whiteStart) {
        this.whiteStart = whiteStart;
    }
    
}
