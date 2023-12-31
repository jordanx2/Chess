package application;

import application.Pieces.Bishop;
import application.Pieces.Knight;
import application.Pieces.Piece;
import application.Pieces.PieceType;
import application.Pieces.Queen;
import application.Pieces.Rook;
import processing.core.PApplet;

public class PromotionBlock {
    class PromotionSquare{
        float promotionBoxX;
        float promotionBoxY;

        public PromotionSquare(){
            this.promotionBoxX = 0;
            this.promotionBoxY = 0;
        }

    } // End inner class

    PieceType[] types;
    String[] displayImages;

    // Represents if the current promotion display block is currently on display
    static boolean promotionDisplayed;

    // Boolean indicating whether or not to display the promotion block
    static boolean pawnPromotionOptions;

    // Will ever only be a single instance of this class
    private static PromotionBlock promotionInstance;

    PromotionSquare[] promotionSquares;

    // X,Y values of where the origin of the promotion block will spawn
    float blockSpawnX;
    float blockSpawnY;
    int prevPromoteSquare;
    int newPromoteSquare;

    // The piece that the pawn is promoting to
    static Piece pawnPromotionPiece;

    static boolean promotionAchieved;

    static String pieceTeam;

    private PromotionBlock(){
        this.types = new PieceType[]{PieceType.QUEEN, PieceType.KNIGHT, PieceType.ROOK, PieceType.BISHOP};

        promotionDisplayed = false;
        pawnPromotionOptions = false;
        this.promotionSquares = new PromotionSquare[4];
        for(int i = 0; i < promotionSquares.length; i++){
            promotionSquares[i] = new PromotionSquare();
        }
        this.blockSpawnX = 0;
        this.blockSpawnY = 0;
        this.prevPromoteSquare = -1;
        this.newPromoteSquare = -1;
        pawnPromotionPiece = null;
        promotionAchieved = false;
        pieceTeam = null;

    }

    public void setDisplayImages(){
        if(pieceTeam.matches("WHITE")){
            this.displayImages = new String[]{
                ChessSymbols.WHITE_CHESS_QUEEN_IMG, 
                ChessSymbols.WHITE_CHESS_KNIGHT_IMG, 
                ChessSymbols.WHITE_CHESS_ROOK_IMG,
                ChessSymbols.WHITE_CHESS_BISHOP_IMG
            };  
        } else{
            this.displayImages = new String[]{
                ChessSymbols.BLACK_CHESS_QUEEN_IMG, 
                ChessSymbols.BLACK_CHESS_KNIGHT_IMG, 
                ChessSymbols.BLACK_CHESS_ROOK_IMG,
                ChessSymbols.BLACK_CHESS_BISHOP_IMG
            };  
        }
    }

    public static PromotionBlock getInstance(){
        if(promotionInstance == null){
            promotionInstance = new PromotionBlock();
        }

        return promotionInstance;
    }

    public void reset(){ promotionInstance = new PromotionBlock();}

    public static void registerPawnPromotion(PieceType promotion, int previousSquare, int promotionSquare, Square[] board){
        Piece promotionPiece = null;
        String symbol;
        String pieceImg;

        switch(promotion){
            case BISHOP:
                symbol = pieceTeam.matches("WHITE") ? ChessSymbols.WHITE_CHESS_BISHOP : ChessSymbols.BLACK_CHESS_BISHOP;
                pieceImg = pieceTeam.matches("WHITE") ? ChessSymbols.WHITE_CHESS_BISHOP_IMG : ChessSymbols.BLACK_CHESS_BISHOP_IMG;
                promotionPiece = new Bishop(promotion, symbol, pieceTeam, pieceImg);
                break;
            case KNIGHT:
                symbol = pieceTeam.matches("WHITE") ? ChessSymbols.WHITE_CHESS_KNIGHT : ChessSymbols.BLACK_CHESS_KNIGHT;
                pieceImg = pieceTeam.matches("WHITE") ? ChessSymbols.WHITE_CHESS_KNIGHT_IMG : ChessSymbols.BLACK_CHESS_KNIGHT_IMG;
                promotionPiece = new Knight(promotion, symbol, pieceTeam, pieceImg);
                break;
            case QUEEN:
                symbol = pieceTeam.matches("WHITE") ? ChessSymbols.WHITE_CHESS_QUEEN : ChessSymbols.BLACK_CHESS_QUEEN;
                pieceImg = pieceTeam.matches("WHITE") ? ChessSymbols.WHITE_CHESS_QUEEN_IMG : ChessSymbols.BLACK_CHESS_QUEEN_IMG;
                promotionPiece = new Queen(promotion, symbol, pieceTeam, pieceImg);
                break;
            case ROOK:
                symbol = pieceTeam.matches("WHITE") ? ChessSymbols.WHITE_CHESS_ROOK : ChessSymbols.BLACK_CHESS_ROOK;
                pieceImg = pieceTeam.matches("WHITE") ? ChessSymbols.WHITE_CHESS_ROOK_IMG : ChessSymbols.BLACK_CHESS_ROOK_IMG;
                promotionPiece = new Rook(promotion, symbol, pieceTeam, pieceImg);
                break;
            default:
                break;

        }

        pawnPromotionPiece = promotionPiece;
        promotionAchieved = true;
    }

    public boolean checkPromotionSelected(Square[] boardSquares, float half){
        for(int i = 0; i < promotionSquares.length; i++){
            float x = promotionSquares[i].promotionBoxX;
            float y = promotionSquares[i].promotionBoxY;

            if(PApplet.dist(x + half, y + half, blockSpawnX, blockSpawnY) < 40){
                registerPawnPromotion(types[i], prevPromoteSquare, newPromoteSquare, boardSquares);
                return true;

            } 
        }

        return false;
    }

    public void applyPromotion(Board board){ 
        pawnPromotionPiece.setSize(board.getSquareW());
        board.applyMove(newPromoteSquare, prevPromoteSquare);
    }

    public float verticleIncrement(int i, float boxW){
        if(pieceTeam.matches("WHITE")){
            return blockSpawnY + (i * boxW);

        }
        
        return blockSpawnY - (i * boxW);
    }

}
