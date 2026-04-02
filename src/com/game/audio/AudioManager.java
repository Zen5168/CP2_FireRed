package com.game.audio;

import java.io.*;
import javax.sound.sampled.*;
import java.util.concurrent.*;

public class AudioManager {

    private Clip currentClip;

    public void playForDuration(String fileName, long startMicroseconds, long durationMillis) {
        playWithLoop(fileName, startMicroseconds);

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            stopCurrent();
        }, durationMillis, TimeUnit.MILLISECONDS);
    }

    public void playWithLoop(String fileName, long startMicroseconds) {
        stopCurrent(); // STOP MUSIC
        try {
            File soundFile = new File("src/com/game//audio/" + fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);
            currentClip.setMicrosecondPosition(startMicroseconds);
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);
            currentClip.start();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void stopCurrent() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
        }
    }
}
