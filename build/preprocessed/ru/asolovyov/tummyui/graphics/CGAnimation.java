/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.graphics.views.CGArc;
import ru.asolovyov.tummyui.graphics.views.CGDrawable;

/**
 *
 * @author Администратор
 */
public abstract class CGAnimation {
    public static final int SIMPLE = 0;
    public static final int LOOP = 1;
    public static final int AUTOREVERSE = 2;

    private static final int PROPERTIES_COUNT = 9;

    protected abstract void animations(CGDrawable drawable);
    protected void completion(CGAnimation animation) {  };

    protected CGDrawable drawable;
    private int type = SIMPLE;

    private int cyclesCount = 0;
    private int currentCycle = 0;
    // [Current, Target] * PROPERTIES_COUNT
    private int[][] values = new int[PROPERTIES_COUNT][2];
    
    public CGAnimation(int durationMillis) {
        this(durationMillis, SIMPLE);
    }

    public CGAnimation(int durationMillis, int type) {
        this.type = type;
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

        this.values[8][type] = 0;
        if (drawable instanceof CGArc) {
            this.values[8][type] = ((CGArc)drawable).startAngle();
        }
    }

    protected void setupAndBegin() {
        this.setValues(0); //Originals
        this.animations(drawable);
        this.setValues(1); //Targets
        
        this.animateNextFrame();
    }

    public void animateNextFrame() {
//        //S.println(this + " GONNA ANIMATE!");
        if (this.isFinished()) {
//            //S.println(this + " 1 FINISHED!");
            return;
        }

        for (int i = 0; i < this.values.length; i++) {
//            //S.println("VALUE");
            
            int[] vector = this.values[i];
            int originalValue = vector[0];
            int targetValue = vector[1];

            int currentFrameValue = this.makeCurrentFrameValue(originalValue, targetValue);
            if (currentFrameValue == CG.NULL) {
                continue;
            }
            
            if (i == 0) { drawable.x(currentFrameValue); continue; }
            if (i == 1) { drawable.y(currentFrameValue); continue; }
            if (i == 2) { drawable.width(currentFrameValue); continue; }
            if (i == 3) { drawable.height(currentFrameValue); continue; }
            if (i == 4) { drawable.cornerRadius(currentFrameValue); continue; }

            if (i == 8 && drawable instanceof CGArc) {
//                //S.println("ARC DELTA " + currentFrameValue + " OR " + originalValue + " TGT " + targetValue + " CC " + currentCycle + " AC " + cyclesCount);
                ((CGArc)drawable).startAngle(currentFrameValue);
            }

            //COLORS
            int r = makeCurrentFrameValue(CGColor.red(originalValue), CGColor.red(targetValue));
            int g = makeCurrentFrameValue(CGColor.green(originalValue), CGColor.green(targetValue));
            int b = makeCurrentFrameValue(CGColor.blue(originalValue), CGColor.blue(targetValue));
            int currentFrameValueColor = (r << 16) + (g << 8) + b;

            if (i == 5) { drawable.color(currentFrameValueColor); continue; }
            if (i == 6) { drawable.backgroundColor(currentFrameValueColor); continue; }
            if (i == 7) { drawable.borderColor(currentFrameValueColor); continue; }
        }
        
        this.currentCycle++;
        
        if (this.isFinished()) {
//            //S.println(this + " 2 FINISHED!");
            
            this.completion(this);
            
            if (this.type == LOOP) {
                this.currentCycle = 0;
            }
            else if(this.type == AUTOREVERSE) {
                this.currentCycle = 0;
                this.reverseValues();
            } else {
                return;
            }
            
            animateNextFrame();
        }
    }

    private void reverseValues() {
        for (int i = 0; i < this.values.length; i++) {
            int tmp = this.values[i][0];
            this.values[i][0] = this.values[i][1];
            this.values[i][1] = tmp;
        }
    }

    private int makeCurrentFrameValue(int source, int target) {
        int delta = target - source;
        if (delta == 0) {
            return CG.NULL;
        }
        
        int cycleDelta = delta / cyclesCount;

        int multiplier = 1;
        while (cycleDelta == 0 && delta * multiplier <= Integer.MAX_VALUE) {
            multiplier *= 2;
            cycleDelta = delta * multiplier / this.cyclesCount;
        }

        delta = (cycleDelta * this.currentCycle) / multiplier;
        if (Math.abs(delta) == 0) {
            return CG.NULL;
        }

        int value = source + delta;
        return value;
    }

    public boolean isFinished() {
        return this.currentCycle > this.cyclesCount;
    }

    public void restart() {
//        //S.println(this + " RESTART!");
        for (int i = 0; i < values.length; i++) {
            int[] is = values[i];
            for (int j = 0; j < is.length; j++) {
                //S.print(" " + is[j]);
            }
//            //S.println("");
        }
        this.currentCycle = 0;
        this.animateNextFrame();
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
