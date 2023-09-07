package application;

import java.util.Map;
import application.Pieces.*;
import processing.core.PApplet;
import processing.core.PImage;

public class PiecesCaptured {
    private Piece[] blackPiecesCaptured;
    private Piece[] whitePiecesCaptured;
    private int numBlackPieceCaptured;
    private int numWhitePieceCaptured;
    private float boardLeftCorner;
    private PApplet p;
    private float sectionHeight;
    private int numSquares;
    private float squareW;
    private static PiecesCaptured instance;
    private int blackScore;
    private int whiteScore;
    private float blackLastImgX;
    private float whiteLastImgX;

    private PiecesCaptured(Board board, PApplet p){
        int bufferSize = 24;
        this.boardLeftCorner = board.getBorder();
        this.p = p;
        this.sectionHeight = 30;
        this.numSquares = 8;
        this.squareW = board.getSquareW();
        this.blackPiecesCaptured = new Piece[bufferSize];
        this.whitePiecesCaptured = new Piece[bufferSize];
        this.numBlackPieceCaptured = 0;
        this.numWhitePieceCaptured = 0;
        this.blackScore = 0;
        this.whiteScore = 0;
        this.blackLastImgX = 0;
        this.whiteLastImgX = 0;
    }

    public static PiecesCaptured getInstance(Board board, PApplet p){
        if(instance == null){
            instance = new PiecesCaptured(board, p);
        }

        return instance;
    }

    public void drawCaptureSections(){
        p.stroke(255);
        p.rect(boardLeftCorner, boardLeftCorner, squareW * numSquares, -sectionHeight);
        p.rect(boardLeftCorner, boardLeftCorner * (numSquares + 1), squareW * numSquares, sectionHeight);
        p.noStroke();

        renderPiecesCapture();
    }

    private void renderPiecesCapture(){
        float whiteStartY = boardLeftCorner - sectionHeight;
        float blackStartY = boardLeftCorner * (numSquares + 1);

        if(blackPiecesCaptured[0] == null && whitePiecesCaptured[0] == null) return;

        if(blackPiecesCaptured[0] != null){
            blackLastImgX = renderPieces(blackPiecesCaptured, blackStartY);
            blackScore = determineScore(blackPiecesCaptured);
        }
        
        if(whitePiecesCaptured[0] != null){
            whiteLastImgX = renderPieces(whitePiecesCaptured, whiteStartY);
            whiteScore = determineScore(whitePiecesCaptured);
        }

        if(whiteScore > blackScore){
            p.text("+" + (whiteScore - blackScore), whiteLastImgX + 25, boardLeftCorner - 5);
        } 
        
        else if(blackScore > whiteScore){
            p.text("+" + (blackScore - whiteScore), blackLastImgX + 25, blackStartY + sectionHeight - 5);
        }
        
    }

    private float renderPieces(Piece[] pieces, float startinPosY){
        PImage img = null;
        int imgSize = 25;
        p.textSize(imgSize);
        float lastImgX = 0;

        loop: for(int i = 0; i < pieces.length; i++){
            Piece piece = pieces[i];
            if(piece == null){
                break loop;
            }

            img = p.loadImage(determineSymbol(piece));
            p.image(img, boardLeftCorner + (i * imgSize), startinPosY, sectionHeight, sectionHeight);
            lastImgX = boardLeftCorner + (i * imgSize);
        }

        return lastImgX;
    }

    private String determineSymbol(Piece piece){
        boolean isWhite = piece.getColor().matches("WHITE") ? true : false;

        Map<Class<? extends Piece>, String> symbolMap = Map.of(
            Queen.class, isWhite ? ChessSymbols.WHITE_CHESS_QUEEN_IMG: ChessSymbols.BLACK_CHESS_QUEEN_IMG,
            Rook.class, isWhite ? ChessSymbols.WHITE_CHESS_ROOK_IMG : ChessSymbols.BLACK_CHESS_ROOK_IMG,
            Pawn.class, isWhite ? ChessSymbols.WHITE_CHESS_PAWN_IMG : ChessSymbols.BLACK_CHESS_PAWN_IMG,
            Bishop.class, isWhite ? ChessSymbols.WHITE_CHESS_BISHOP_IMG : ChessSymbols.BLACK_CHESS_BISHOP_IMG,
            Knight.class, isWhite ? ChessSymbols.WHITE_CHESS_KNIGHT_IMG : ChessSymbols.BLACK_CHESS_KNIGHT_IMG
        );

        return symbolMap.get(piece.getClass());
    }

    private int determinePieceScore(Piece p){
        Map<Class<? extends Piece>, Integer> scoreMap = Map.of(
            Queen.class, 9,
            Rook.class, 5,
            Pawn.class, 1,
            Bishop.class, 3,
            Knight.class, 3
        );

        return scoreMap.get(p.getClass());
    }

    private int determineScore(Piece[] pieces){
        p.fill(255);
        int score = 0;
        for(Piece piece : pieces){
            if(piece == null){
                return score; 
            }

            score += determinePieceScore(piece);
        }
        
        return score;
    }

    public void addPieceCaptured(Piece piece){
        if(piece.getColor().toLowerCase().matches("white")){
            whitePiecesCaptured[numWhitePieceCaptured] = piece;
            numWhitePieceCaptured++;
        }else{
            blackPiecesCaptured[numBlackPieceCaptured] = piece;
            numBlackPieceCaptured++;
        }
    }
}
