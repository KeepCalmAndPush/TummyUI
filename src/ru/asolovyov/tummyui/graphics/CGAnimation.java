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
    private CGSize cornerRadius;
    private CGSize cornerRadiusDelta;

    private int xTarget;
    private int yTarget;
    private int widthTarget;
    private int heightTarget;

    private int colorTarget;
    private int backgroundColorTarget;
    private int borderColorTarget;

    public CGAnimation(int durationMillis) {
        this.cyclesCount = durationMillis / CG.FRAME_MILLIS;
    }

    protected void setupAndBegin() {
        this.xTarget = this.x = drawable.x();
        this.yTarget = this.y = drawable.y();
        this.widthTarget = this.width = drawable.width();
        this.heightTarget = this.height = drawable.height();

        this.colorTarget = this.color = drawable.color();
        this.backgroundColorTarget = this.backgroundColor = drawable.backgroundColor();
        this.borderColorTarget = this.borderColor = drawable.borderColor();

        this.animations(drawable);

        this.xDelta = (this.xTarget - this.x) / cyclesCount;
        this.yDelta = (this.yTarget - this.y) / cyclesCount;
        this.widthDelta = (this.widthTarget - this.width) / cyclesCount;
        this.heightDelta = (this.heightTarget - this.height) / cyclesCount;

        this.colorDelta = (this.colorTarget - this.color) / cyclesCount;
        this.backgroundColorDelta = (this.backgroundColorTarget - this.backgroundColor) / cyclesCount;
        this.borderColorDelta = (this.borderColorTarget - this.borderColor) / cyclesCount;

        this.animateNextFrame();
    }

    public void animateNextFrame() {
        if (this.isFinished()) {
            return;
        }

        this.xTarget = getDrawable().x();
        this.yTarget = getDrawable().y();
        this.widthTarget = getDrawable().width();
        this.heightTarget = getDrawable().height();

        this.colorTarget = getDrawable().color();
        this.backgroundColorTarget = getDrawable().backgroundColor();
        this.borderColorTarget = getDrawable().borderColor();
        
        if (xDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().x(this.xTarget);
            } else {
                getDrawable().x(this.x + this.xDelta * this.currentCycle);
            }
        }
        if (yDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().y(this.yTarget);
            } else {
                getDrawable().y(this.y + this.yDelta * this.currentCycle);
            }
        }
        if (widthDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().width(this.widthTarget);
            } else {
                getDrawable().width(this.width + this.widthDelta * this.currentCycle);
            }
        }
        if (heightDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().height(this.heightTarget);
            } else {
                getDrawable().height(this.height + this.heightDelta * this.currentCycle);
            }
        }

        if (colorDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().color(this.colorTarget);
            } else {
                getDrawable().color(this.color + this.colorDelta * this.currentCycle);
            }
        }
        if (backgroundColorDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().backgroundColor(this.backgroundColorTarget);
            } else {
                getDrawable().backgroundColor(this.backgroundColor + this.backgroundColorDelta * this.currentCycle);
            }
        }
        if (borderColorDelta != 0) {
            if (this.currentCycle == cyclesCount) {
                getDrawable().borderColor(this.borderColorTarget);
            } else {
                getDrawable().borderColor(this.borderColorDelta + this.borderColorDelta * this.currentCycle);
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
        this.setupAndBegin();
    }
}
