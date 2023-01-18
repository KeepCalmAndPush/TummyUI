/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

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
    //[Current, Target, Delta] x8
    private int[][] values = new int[PROPERTIES_COUNT][3];

//    private int x;
//    private int xDelta;
//    private int y;
//    private int yDelta;
//    private int width;
//    private int widthDelta;
//    private int height;
//    private int heightDelta;
//
//    private int color;
//    private int colorDelta;
//    private int backgroundColor;
//    private int backgroundColorDelta;
//    private int borderColor;
//    private int borderColorDelta;
//    private int cornerRadius;
//    private int cornerRadiusDelta;
//
//    //TODO отказаться от таргетной фигни в пользу вычитания и добавления на последнем цикле оставшейся разницы
//    private int xTarget;
//    private int yTarget;
//    private int widthTarget;
//    private int heightTarget;
//
//    private int colorTarget;
//    private int backgroundColorTarget;
//    private int borderColorTarget;
//    private int cornerRadiusTarget;

    public CGAnimation(int durationMillis) {
        this.cyclesCount = durationMillis / CG.FRAME_MILLIS;
    }

    private void setValues(int type) {
        this.values[0][type] = drawable.x();
        this.values[1][type] = drawable.y();
        this.values[2][type] = drawable.width();
        this.values[3][type] = drawable.height();

        this.values[4][type] = drawable.color();
        this.values[5][type] = drawable.backgroundColor();
        this.values[6][type] = drawable.borderColor();
        this.values[7][type] = drawable.cornerRadius();
    }

    private void setDeltas() {
        for (int i = 0; i < this.values.length; i++) {
            int[] vector = this.values[i];
            if (i == 4 || i ==5 || i == 6) {
                //TODO  дельты цвета надо вычислять по-другому
                //вообще мб уйти от хранимых дельт и в каждом кадре вычислять новое значение
                vector[2] = (vector[1] - vector[0]) * (1000 / cyclesCount);
            } else {
                vector[2] = (vector[1] - vector[0]) * 1000 / cyclesCount;
            }
        }
    }

    protected void setupAndBegin() {
        this.setValues(0); //Originals
        this.animations(drawable);
        this.setValues(1); //Targets
        this.setDeltas();
        
        this.animateNextFrame();
    }

    public void animateNextFrame() {
        if (this.isFinished()) {
            return;
        }

        for (int i = 0; i < this.values.length; i++) {
            int[] vector = this.values[i];
            int delta = vector[2];
            if (delta == 0) {
                continue;
            }
            int originalValue = vector[0];
            int targetValue = vector[1];
            int nextValue = targetValue;

            if (this.currentCycle != this.cyclesCount) {
                delta = (delta * this.currentCycle) / 1000;

                if (i == 4 || i ==5 || i == 6) {
                    //TODO  дельты цвета надо вычислять по-другому
                    nextValue = CGColor.addDelta(originalValue, delta);
                } else {
                   nextValue = originalValue + delta;
                }
            }

            if (i == 0) { drawable.x(nextValue); continue; }
            if (i == 1) { drawable.y(nextValue); continue; }
            if (i == 2) { drawable.width(nextValue); continue; }
            if (i == 3) { drawable.height(nextValue); continue; }

            //TODO КОЛОРЫ СЛОЖНЕЕ! ВИДАТЬ НАДО ПО КАЖДОЙ КОМПОНЕНТЕ ДВИГАТЬСЯ!
            if (i == 4) { drawable.color(nextValue); continue; }
            if (i == 5) { drawable.backgroundColor(nextValue); continue; }
            if (i == 6) { drawable.borderColor(nextValue); continue; }
            
            if (i == 7) { drawable.cornerRadius(nextValue); continue; }
        }
        
        

        this.currentCycle++;
        if (this.isFinished()) {
            this.completion(this);
        }
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
