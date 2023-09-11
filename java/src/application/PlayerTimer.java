package application;

import java.time.LocalDateTime;

public class PlayerTimer extends Thread{
    int timeRemaining;
    long previousTime;
    LocalDateTime time;
    boolean running;
    boolean threadRunning;

    public PlayerTimer(int timeConstraint, boolean running){
        this.timeRemaining = timeConstraint;
        this.time = LocalDateTime.now();
        this.previousTime = System.currentTimeMillis();
        this.running = running;
        this.threadRunning = true;
    }

    public void run(){
        while(threadRunning){
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(running){
                timeRemaining--;
            }
        }
    }

    public void pauseTime(){
        this.running = false;
    }

    public void resumeTime(){
        this.running = true;
    }

    public synchronized String getTimeRemaining(){
        return timeRemaining / 60 + ":" + timeRemaining % 60;
    }

}
    