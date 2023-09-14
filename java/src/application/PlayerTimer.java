package application;

import java.time.LocalDateTime;
import processing.core.PApplet;

public class PlayerTimer extends Thread{
    int timeRemaining;
    int currentTime;
    int timeConstraint;
    LocalDateTime time;
    boolean running;
    boolean threadRunning;
    boolean updateTime;
    Board board;
    PApplet p;
    float x;
    float y;
    int boxWidth;
    int boxHeight;

    public PlayerTimer(int timeConstraint, boolean running, PApplet p, Board board, float y){
        this.timeConstraint = timeConstraint;
        this.timeRemaining = timeConstraint;
        this.time = LocalDateTime.now();
        this.running = running;
        this.threadRunning = true;
        this.currentTime = timeRemaining;
        this.updateTime = true;
        this.board = board;
        this.p = p;
        this.x = board.getBorder() + (board.getSquareW() * 8) + 20;
        this.y = y;
        this.boxWidth = 150;
        this.boxHeight = 100;
        render();
    }

    public void run(){
        while(threadRunning){
            if(running){
                if(currentTime != timeRemaining){
                    updateTime = true;
                    currentTime = timeRemaining;
                } else{
                    if(timeRemaining != timeConstraint){
                        updateTime = false;
                    }
                }
                timeRemaining--;
            }

            if(isTimeLimitReached() || Rules.getInstance().isCheckMate()){
                setThreadRunning(false);
                PlaySound.getInstance(p).addSound(SpecialFlags.TIME_OUT);
            } else{
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isThreadRunning(){
        return threadRunning;
    }

    public void setThreadRunning(boolean threadRunning){
        this.threadRunning = threadRunning;
    }

    public boolean isTimeLimitReached(){
        return currentTime == 0;
    }

    public void pauseTime(){
        this.running = false;
    }

    public void resumeTime(){
        this.running = true;
    }

    public synchronized String getTimeRemaining(){
        return currentTime / 60 + ":" + currentTime % 60;
    }

    public void renderTextBox(){
        // Draw the timer rectangles
        p.stroke(255);
        p.fill(0);
        p.rect(x, y, boxWidth, boxHeight);
    }

    public void updatePlayersTime(){
        String time = getTimeRemaining();
        if(isTimeLimitReached()){
            time = "0";
        }
        p.textAlign(PApplet.CENTER, PApplet.CENTER); 
        p.textSize(24); 
        p.fill(255);
        p.text(time, x, y, boxWidth, boxHeight);
        p.textAlign(PApplet.LEFT, PApplet.BASELINE);
    }

    public void render(){
        renderTextBox();
        updatePlayersTime();
    }

}
    