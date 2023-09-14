package application;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class PlaySound extends Thread {
    PApplet p;
    Minim minim;
    String soundURL;
    AudioPlayer player;
    boolean threadRunning;
    Queue<SpecialFlags> soundQueue;
    static PlaySound instance;

    private PlaySound(PApplet p){
        this.p = p;
        this.minim = new Minim(p);
        this.soundURL = null;
        this.threadRunning = true;
        this.soundQueue = new LinkedList<>();
        this.player = minim.loadFile(getURL(null));
    }

    public static PlaySound getInstance(PApplet p){
        if(instance == null){   
            instance = new PlaySound(p);
        }

        return instance;
    }

    public void run(){
        while(threadRunning){
            synchronized(this){;
                if(!soundQueue.isEmpty()){
                    play(soundQueue.poll());
                }
            }


            try{
                Thread.sleep(200);
            }catch(Exception e){    
                e.printStackTrace();
            }
        }
    }

    public synchronized void addSound(SpecialFlags flag){
        soundQueue.add(flag);
    }

    public void killThread(){
        threadRunning = false;
    }

    private void play(SpecialFlags flag){    
        soundURL = getURL(flag);
        player = minim.loadFile(soundURL);
        player.play();
    }

    private String getURL(SpecialFlags flag) {
        String filePath = "/Users/Workspaces/Chess/sounds/";
        if(flag == null) return filePath + "move.mp3/";

        return filePath + Map.of(
            SpecialFlags.CAPTURE, "capture.mp3/",
            SpecialFlags.CHECK, "check.mp3/",
            SpecialFlags.CHECK_MATE, "notify.mp3/",
            SpecialFlags.KING_SIDE_CASTLING, "castle.mp3/",
            SpecialFlags.QUEEN_SIDE_CASTLING, "castle.mp3/",
            SpecialFlags.PROMOTION, "promotion.mp3",
            SpecialFlags.TIME_OUT, "notify.mp3/"

        ).get(flag);
    }

}
