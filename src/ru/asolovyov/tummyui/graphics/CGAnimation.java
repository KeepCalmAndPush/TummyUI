/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.graphics.views.CGDrawable;

/**
 *
 * @author Администратор
 */
public abstract class CGAnimation {
    protected abstract void animations(CGDrawable drawable);
    protected void completion(CGAnimation animation) {  };

    private CGDrawable drawable;

    private int cyclesCount = 0;
    private int currentCycle = 0;

    private int x;
    private int xDelta;
    private int y;
    private int yDelta;
    private int width;
    private int widthDelta;
    private int height;
    private int heightDelta;
    
    private int color;
    private int colorDelta;
    private int backgroundColor;
    private int backgroundColorDelta;
    private int borderColor;
    private int borderColorDelta;
    private int cornerRadius;
    private int cornerRadiusDelta;

    //TODO отказаться от таргетной фигни в пользу вычитания и добавления на последнем цикле оставшейся разницы
    private int xTarget;
    private int yTarget;
    private int widthTarget;
    private int heightTarget;

    private int colorTarget;
    private int backgroundColorTarget;
    private int borderColorTarget;
    private int cornerRadiusTarget;

    public CGAnimation(int durationMillis) {
        this.cyclesCount = durationMillis / CG.FRAME_MILLIS;
    }

    protected void setupAndBegin() {
        this.x = drawable.x();
        this.y = drawable.y();
        this.width = drawable.width();
        this.height = drawable.height();

        this.color = drawable.color();
        this.backgroundColor = drawable.backgroundColor();
        this.borderColor = drawable.borderColor();
        this.cornerRadius = drawable.cornerRadius();

        S.println("BG COLOR ORIG: " + Integer.toHexString(drawable.backgroundColor()));

        this.animations(drawable);

        this.xTarget = drawable.x();
        this.yTarget = drawable.y();
        this.widthTarget = drawable.width();
        this.heightTarget = drawable.height();

        this.colorTarget = drawable.color();
        this.backgroundColorTarget = drawable.backgroundColor();
        this.borderColorTarget = drawable.borderColor();
        this.cornerRadiusTarget = drawable.cornerRadius();

        S.println("BG COLOR TARGET: " + Integer.toHexString(backgroundColorTarget));

        this.xDelta = (this.xTarget - this.x) * 1000 / cyclesCount;
        this.yDelta = (this.yTarget - this.y) * 1000 / cyclesCount;
        this.widthDelta = (this.widthTarget - this.width) * 1000  / cyclesCount;
        this.heightDelta = (this.heightTarget - this.height) * 1000 / cyclesCount;

        this.colorDelta = (this.colorTarget - this.color) * 1000  / cyclesCount;
        this.backgroundColorDelta = (this.backgroundColorTarget - this.backgroundColor) * 1000  / cyclesCount;
        this.borderColorDelta = (this.borderColorTarget - this.borderColor) * 1000  / cyclesCount;
        this.cornerRadiusDelta = (this.cornerRadiusTarget - this.cornerRadius) * 1000  / cyclesCount;
        
        this.animateNextFrame();
    }

    public void animateNextFrame() {
        if (this.isFinished()) {
            return;
        }
        
        if (xDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().x(this.xTarget);
            } else {
                getDrawable().x(this.x + (this.xDelta * this.currentCycle) / 1000);
            }
        }
        if (yDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().y(this.yTarget);
            } else {
                getDrawable().y(this.y + (this.yDelta * this.currentCycle) / 1000);
            }
        }
        if (widthDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().width(this.widthTarget);
            } else {
                getDrawable().width(this.width + (this.widthDelta * this.currentCycle) / 1000);
            }
        }
        if (heightDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().height(this.heightTarget);
            } else {
                getDrawable().height(this.height + (this.heightDelta * this.currentCycle) / 1000);
            }
        }

        if (colorDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().color(this.colorTarget);
            } else {
                getDrawable().color(this.color + (this.colorDelta * this.currentCycle) / 1000);
            }
        }
        //TODO КОЛОРЫ СЛОЖНЕЕ! ВИДАТЬ НАДО ПО КАЖДОЙ КОМПОНЕНТЕ ДВИГАТЬСЯ!
        if (backgroundColorDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().backgroundColor(backgroundColorTarget);
                
            } else {
                getDrawable().backgroundColor(this.backgroundColor + (this.backgroundColorDelta * this.currentCycle) / 1000);
            }
        }
        if (borderColorDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().borderColor(this.borderColorTarget);
            } else {
                getDrawable().borderColor(this.borderColor + (this.borderColorDelta * this.currentCycle) / 1000);
            }
        }
        if (this.cornerRadiusDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().cornerRadius(this.cornerRadiusTarget);
            } else {
                getDrawable().cornerRadius(this.cornerRadius + (this.cornerRadiusDelta * this.currentCycle) / 1000);
            }
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
