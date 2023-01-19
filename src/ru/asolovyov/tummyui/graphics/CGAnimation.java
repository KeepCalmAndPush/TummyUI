/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.subjects.CurrentValueSubject;
import ru.asolovyov.tummyui.graphics.views.CGDrawable;

/**
 *
 * @author Администратор
 */
public abstract class CGAnimation {
    protected abstract void animations(CGDrawable drawable);
    protected void completion(CGAnimation animation) {  };

    private CGDrawable drawable;

    private static final int PROPERTIES_COUNT = 8;

    private int cyclesCount = 0;
    private int currentCycle = 0;
    // [Current, Target] x8
    private int[][] values = new int[PROPERTIES_COUNT][2];

    public CGAnimation(int durationMillis) {
        this.cyclesCount = durationMillis / CG.FRAME_MILLIS;
    }

    private void setValues(int type) {
        this.values[0][type] = drawable.x();
        this.values[1][type] = drawable.y();
        this.values[2][type] = drawable.width();
        this.values[3][type] = drawable.height();
        this.values[4][type] = drawable.cornerRadius();

        this.values[5][type] = drawable.color();
        this.values[6][type] = drawable.backgroundColor();
        this.values[7][type] = drawable.borderColor();
    }

    protected void setupAndBegin() {
        this.setValues(0); //Originals
        this.animations(drawable);
        this.setValues(1); //Targets
        
        this.animateNextFrame();
    }

    public void animateNextFrame() {
        if (this.isFinished()) {
            return;
        }

        for (int i = 0; i < this.values.length; i++) {
            int[] vector = this.values[i];
            int originalValue = vector[0];
            int targetValue = vector[1];

            int currentFrameValue = this.makeCurrentFrameValue(originalValue, targetValue);
            if (currentFrameValue == Integer.MIN_VALUE) {
                continue;
            }
            
            if (i == 0) { drawable.x(currentFrameValue); continue; }
            if (i == 1) { drawable.y(currentFrameValue); continue; }
            if (i == 2) { drawable.width(currentFrameValue); continue; }
            if (i == 3) { drawable.height(currentFrameValue); continue; }
            if (i == 4) { drawable.cornerRadius(currentFrameValue); continue; }

            //COLORS
            int r = makeCurrentFrameValue(CGColor.red(originalValue), CGColor.red(targetValue));
            int g = makeCurrentFrameValue(CGColor.green(originalValue), CGColor.green(targetValue));
            int b = makeCurrentFrameValue(CGColor.blue(originalValue), CGColor.blue(targetValue));
            currentFrameValue = (r << 16) + (g << 8) + b;

            if (i == 6) {
                S.println("r g b " + r + " " + g + " " + b);
                S.println("CFV: " + currentFrameValue);
            }

            if (i == 5) { drawable.color(currentFrameValue); continue; }
            if (i == 6) { drawable.backgroundColor(currentFrameValue); continue; }
            if (i == 7) { drawable.borderColor(currentFrameValue); continue; }
        }
        
        this.currentCycle++;
        if (this.isFinished()) {
            this.completion(this);
        }
    }

    private int makeCurrentFrameValue(int source, int target) {
        int delta = target - source;
        if (delta == 0) {
            return Integer.MIN_VALUE;
        }
        
        int cycleDelta = 0;

        int multiplier = 1;
        while (cycleDelta == 0 && delta * multiplier <= Integer.MAX_VALUE) {
            multiplier *= 10;
            cycleDelta = delta * multiplier / this.cyclesCount;
        }

        delta = (cycleDelta * this.currentCycle) / multiplier;
        if (delta == 0) {
            return Integer.MIN_VALUE;
        }

        int value = source + delta;
        return value;
    }

    public boolean isFinished() {
        return this.currentCycle > this.cyclesCount;
    }
    
    public void abort() {
        this.currentCycle = this.cyclesCount;
        this.animateNextFrame();
    }

    /**
     * @return the drawable
     */
    public CGDrawable getDrawable() {
        return drawable;
    }

    /**
     * @param drawable the drawable to set
     */
    public void setDrawable(CGDrawable drawable) {
        this.drawable = drawable;
        CGDisplayLink.ticks.next().sink(new Sink() {
            protected void onValue(Object value) {
                setupAndBegin();
            }
        });
    }
}
