/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.tummyui.graphics.views.CGDrawable;

/**
 *
 * @author Администратор
 */
public class CGAnimation implements Runnable {
    private boolean isFinished = true;
    
    private CGDrawable drawableToAnimate;

    private int currentColor;
    private int currentBackgroundColor;
    private int currentBorderColor;
    private CGFrame currentFrame;
    private CGPoint currentContentOffset;
    private CGInsets currentContentInset;
    private int currentCornerRadius;
    private int currentFlexibility; //вот тут надо как-то эскалировать до суперконтейнера: чтобы анимировался он???

    public CGAnimation(CGDrawable drawable) {
        this.drawableToAnimate = drawable;
    }

    public void animateChanges(int durationMillis) {

    }

    public void run() {
        while (!isFinished) {
            this.animateNextFrame();
        }
    }

    protected void animateNextFrame() {

    }

    public boolean isFinished() {
        return isFinished;
    }
    
    public void abort() {
        this.isFinished = false;
    }
}
