/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class CGStack extends CGSomeDrawable {
    public static abstract class DrawableFactory {
        public abstract CGDrawable itemFor(Object viewModel);
    }

    public final static int AXIS_HORIZONTAL = 0;
    public final static int AXIS_VERTICAL = 1;
    public final static int AXIS_Z = 2;

    protected Arr drawables;
    protected Int alignment;
    protected Int axis;

    public CGStack(Int axis, Int alignment, Arr drawables) {
        super();
        this.axis = axis;
        this.alignment = alignment;
        this.drawables = drawables;

        this.axis.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout();
            }
        });

        this.alignment.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout();
            }
        });

        this.drawables.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout();
            }
        });
    }

    public Int axis() {
        return axis;
    }

    public void draw(Graphics g) {
        super.draw(g);

        if (this.axis.getInt() == AXIS_HORIZONTAL) {
            this.hDraw(g);
        } else if (this.axis.getInt() == AXIS_VERTICAL) {
            this.vDraw(g);
        } else if (this.axis.getInt() == AXIS_Z) {
            this.zDraw(g);
        }
    }

    private void hDraw(Graphics g) {

    }

    int nextTop = 0;

    private void vDraw(Graphics g) {
        final Graphics graphics = g;
        nextTop = getFrame().y;
        
        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGDrawable drawable = (CGDrawable)element;
                drawable.getFrame().y = nextTop;
                drawable.draw(graphics);
                nextTop += drawable.getFrame().height;
            }
        });
    }

    private void zDraw(Graphics g) {

    }
}