/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public abstract class CGSomeDrawable implements CGDrawable {
    protected Obj frameBinding = new Obj(CGFrame.automatic());

    public CGSomeDrawable() {
    }
    // TODO сделать один фрейм общим, и при присваивании нового просто сливать в старый новые значения
    // старый фрейм все слушают
    // то же и с остальными
    protected Obj sizeBinding = new Obj(CGSize.zero());
    protected Int widthBinding = new Int(CGFrame.AUTOMATIC_DIMENSION);
    protected Int heightBinding = new Int(CGFrame.AUTOMATIC_DIMENSION);

    protected Int color = new Int(0xFFFFFF);
    protected Bool isVisible = new Bool(true);

    protected Obj offsetBinding = new Obj(CGPoint.zero());

    private CGCanvas canvas;

    public void draw(Graphics g) {
        g.setColor(this.getColor());
    }

    public CGDrawable color(int colorHex) {
        return this.color(new Int(colorHex));
    }

    public CGDrawable setFrame(int x, int y, int width, int height) {
        return this.setFrame(new Obj(new CGFrame(x, y, width, height)));
    }

    public CGDrawable color(Int colorHex) {
        this.color = colorHex;
        this.color.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable setFrame(CGFrame frame) {
        return this.setFrame(new Obj(frame));
    }

    public CGDrawable setFrame(Obj frame) {
        this.frameBinding = frame;
        this.frameBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getFrame());
            }
        });
        return this;
    }

    public void needsRedraw() {
        this.needsRelayout(this.getFrame());
    }

    public void needsRelayout(CGFrame frame) {
        if (this.getCanvas() != null) {
            this.getCanvas().repaint(frame);
        }
    }

    public CGFrame getFrame() {
        return (CGFrame) frameBinding.getObject();
    }

    protected int getColor() {
        if (color != null) {
           return color.getInt();
        }
        return 0x00000000;
    }

    public CGDrawable setSize(Obj size) {
        this.sizeBinding = size;
        this.sizeBinding.sink(new Sink() {
            protected void onValue(Object value) {
                CGSize size = (CGSize)value;
                setSize(size);
            }
        });
        return this;
    }

    public CGDrawable setSize(CGSize size) {
        return this.setSize(size.width, size.height);
    }

    public CGDrawable setSize(int width, int height) {
        CGFrame frame = getFrame();
        frame.width = width;
        frame.height = height;

        this.setFrame(frame);

        return this;
    }

    public CGDrawable setOffset(Obj offset) {
        this.offsetBinding = offset;
        this.offsetBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getFrame());
            }
        });
        return this;
    }

    public CGDrawable setOffset(CGPoint offset) {
        return this.setOffset(new Obj(offset));
    }
    
    public CGDrawable setOffset(int x, int y) {
        return this.setOffset(new Obj(new CGPoint(x, y)));
    }

    public CGPoint getOffset() {
        //TODO сделать специфические ЮИ биндинги - для сайза, фрейма итп
        return (CGPoint)this.offsetBinding.getObject();
    }

    public CGDrawable width(Int width) {
        this.widthBinding = width;
        this.widthBinding.sink(new Sink() {
            protected void onValue(Object value) {
                width(widthBinding.getInt());
            }
        });
        return this;
    }

    public CGDrawable width(int width) {
        CGFrame frame = getFrame();
        this.setFrame(frame);

        return this;
    }

    public CGDrawable height(Int height) {
        this.heightBinding = height;
        this.heightBinding.sink(new Sink() {
            protected void onValue(Object value) {
                height(heightBinding.getInt());
            }
        });
        return this;
    }

    public CGDrawable height(int height) {
        CGFrame frame = getFrame();
        frame.height = height;
        this.setFrame(frame);

        return this;
    }

    public CGDrawable isVisible(boolean isVisible) {
        return this.isVisible(new Bool(isVisible));
    }

    public CGDrawable isVisible(Bool isVisible) {
        this.isVisible = isVisible;
        this.isVisible.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getFrame());
            }
        });
        return this;
    }

    public boolean isVisible() {
        if (this.isVisible != null) {
            return this.isVisible.getBool();
        }
        return true;
    }

    public CGDrawable setCanvas(CGCanvas canvas) {
        this.canvas = canvas;
        canvas.needsRepaint().setBool(true);

        return this;
    }
    
    public CGCanvas getCanvas() {
        return canvas;
    }

    public CGDrawable sizeToFit() {
        return this;
    }

    public CGSize intrinsicContentSize() {
        return getFrame().getSize();
    }

    private GeometryReader geometryReader;
    public CGDrawable readGeometry(GeometryReader reader) {
        this.geometryReader = reader;
        return this;
    }
    
    public GeometryReader getGeometryReader() {
        return geometryReader;
    }
}