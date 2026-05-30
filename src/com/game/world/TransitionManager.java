package com.game.world;

import java.awt.*;

//=============================
// TRANSITION MANAGE
//=============================
public class TransitionManager {

    private boolean isTransitioning = false;
    private TransitionType currentTransition = TransitionType.NONE;
    private float alpha = 0.0f; // 0.0 = TRANSPARENT, 1.0 = OPAQUE
    private float fadeSpeed = 0.05f; // SPEED OF FADE (HIGHER = FASTER)

    private Runnable onTransitionComplete = null;

    public enum TransitionType {
        NONE,
        FADE_OUT, // FADE TO BLACK
        FADE_IN    // FADE FROM BLACK
    }

    //=============================
    // START A FADE OUT TRANSITION
    //=============================
    public void startFadeOut(Runnable onComplete) {
        isTransitioning = true;
        currentTransition = TransitionType.FADE_OUT;
        alpha = 0.0f;
        onTransitionComplete = onComplete;
    }

    //=============================
    // START A FADE IN TRANSITION
    //=============================
    public void startFadeIn(Runnable onComplete) {
        isTransitioning = true;
        currentTransition = TransitionType.FADE_IN;
        alpha = 1.0f;
        onTransitionComplete = onComplete;
    }

    //======================================
    // START A COMPLETE FADE OUT-IN CYCLE
    //======================================
    public void startFadeOutIn(Runnable onMidpoint) {
        startFadeOut(() -> {
            // WHEN FADE OUT COMPLETES, EXECUTE MIDPOINT ACTION AND FADE BACK IN
            if (onMidpoint != null) {
                onMidpoint.run();
            }
            startFadeIn(null);
        });
    }

    //=============================
    // UPDATE THE TRANSITION STATE
    //=============================
    public void update() {
        if (!isTransitioning) {
            return;
        }

        switch (currentTransition) {
            case FADE_OUT:
                alpha += fadeSpeed;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    completeTransition();
                }
                break;

            case FADE_IN:
                alpha -= fadeSpeed;
                if (alpha <= 0.0f) {
                    alpha = 0.0f;
                    completeTransition();
                }
                break;

            case NONE:
            default:
                break;
        }
    }

    //=============================
    // DRAW THE TRANSITION OVERLAY
    //=============================
    public void draw(Graphics2D g2, int screenWidth, int screenHeight) {
        if (isTransitioning || alpha > 0.0f) {
            // Create a semi-transparent black overlay
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);

            // RESET COMPOSITE TO DEFAULT
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    // COMPLETE THE CURRENT TRANSITION
    private void completeTransition() {
        isTransitioning = false;
        currentTransition = TransitionType.NONE;

        if (onTransitionComplete != null) {
            Runnable callback = onTransitionComplete;
            onTransitionComplete = null;
            callback.run();
        }
    }

    //  CHECK IF A TRANSITION IS CURRENTLY ACTIVE
    public boolean isTransitioning() {
        return isTransitioning;
    }

    public void stopTransition() {
        isTransitioning = false;
        currentTransition = TransitionType.NONE;
        alpha = 0.0f;
        onTransitionComplete = null;
    }

    //=======================
    // GETTERS AND SETTERS
    //=======================
    public float getAlpha() {
        return alpha;
    }

    public void setFadeSpeed(float speed) {
        this.fadeSpeed = Math.max(0.01f, Math.min(0.2f, speed));
    }
}
