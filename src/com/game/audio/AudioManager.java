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
            InputStream audioSrc = getClass().getResourceAsStream("/res/audio/" + fileName);
            if (audioSrc == null) {
                System.out.println("Error: Audio resource not found: /res/audio/" + fileName);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioSrc)
            );

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
