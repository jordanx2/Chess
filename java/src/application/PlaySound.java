package application;

import java.util.Map;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class PlaySound {
    PApplet p;
    Minim minim;
    String soundURL;
    AudioPlayer player;

    public PlaySound(PApplet p){
        this.p = p;
        this.minim = new Minim(p);
        this.soundURL = null;
        
    }

    public void play(SpecialFlags flag){    
        soundURL = getURL(flag);
        player = minim.loadFile(soundURL);
        player.play();
    }

    private String getURL(SpecialFlags flag) {
        String filePath = "/Users/Workspaces/Chess/sounds/";
        if(flag == null) return filePath + "move.mp3/";

        return  Map.of(
            SpecialFlags.CAPTURE, filePath + "capture.mp3/",
            SpecialFlags.CHECK, filePath + "check.mp3/",
            SpecialFlags.CHECK_MATE, filePath + "notify.mp3/",
            SpecialFlags.KING_SIDE_CASTLING, filePath + "castle.mp3/",
            SpecialFlags.QUEEN_SIDE_CASTLING, filePath + "castle.mp3/",
            SpecialFlags.PROMOTION, filePath + "promotion.mp3",
            SpecialFlags.TIME_OUT, filePath + "notify.mp3/"

        ).get(flag);
    }

}
