package application;

import processing.core.PApplet;

public class UpdatePlayersTime {
    PApplet p;
    Board board;
    PlayerTimer whiteTimer;
    PlayerTimer blackTimer;
    int boxWidth;
    int boxHeight;
    float x;
    float bY;
    float wY;

    public UpdatePlayersTime(PApplet p, Board board, PlayerTimer blackTimer, PlayerTimer whiteTimer){
        this.p = p;
        this.board = board;
        this.whiteTimer = whiteTimer;
        this.blackTimer = blackTimer;
        this.boxWidth = 150;
        this.boxHeight = 100;
        this.x = board.getBorder() + (board.getSquareW() * 8) + 20;
        this.bY = board.getBorder();
        this.wY = bY + (board.getSquareW() * 7);
    }

    public void renderTextBox(){
        // Draw the timer rectangles
        p.stroke(255);
        p.fill(0);
        p.rect(x, wY, boxWidth, boxHeight);
        p.rect(x, bY, boxWidth, boxHeight);
    }
    
    public void updatePlayersTime(){
        p.textAlign(PApplet.CENTER, PApplet.CENTER); 
        p.textSize(24); 
        p.fill(255);

        // Calculate the text position for the black timer
        float textX1 = x;
        float textY1 = bY;
        p.text(blackTimer.getTimeRemaining(), textX1, textY1, boxWidth, boxHeight);
        
        // Calculate the text position for the white timer
        float textX2 = x;
        float textY2 = wY;
        p.text(whiteTimer.getTimeRemaining(), textX2, textY2, boxWidth, boxHeight);
        p.textAlign(PApplet.LEFT, PApplet.BASELINE);
    }

    public void render(){
        renderTextBox();
        updatePlayersTime();
    }

}
