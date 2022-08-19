/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.bindings.Frame;
import ru.asolovyov.tummyui.bindings.Point;
import ru.asolovyov.tummyui.bindings.Size;

/**
 *
 * @author Администратор
 */
public abstract class CGSomeDrawable implements CGDrawable {
    

    public CGSomeDrawable() {
    }
    // TODO сделать один фрейм общим, и при присваивании нового просто сливать в старый новые значения
    // старый фрейм все слушают
    // то же и с остальными
    protected Frame frameBinding = new Frame(CGFrame.zero());
    
    protected Int widthBinding = new Int(0);
    protected Int heightBinding = new Int(0);
    protected Int resizingMaskBinding = new Int(CGFrame.FLEXIBLE_ALL);

    protected Int color = new Int(0xFFFFFF);
    protected Bool isVisible = new Bool(true);

    protected Point offsetBinding = new Point(CGPoint.zero());
    protected Size intrinsicContentSizeBinding = new Size(CGSize.zero());

    private CGCanvas canvas;

    public void draw(Graphics g) {
        g.setColor(this.getColor());
    }

    public CGDrawable color(int colorHex) {
        return this.color(new Int(colorHex));
    }

    public CGDrawable setFrame(int x, int y, int width, int height) {
        return this.setFrame(new Frame(new CGFrame(x, y, width, height)));
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
    
    public CGDrawable setFrame(Frame frame) {
        this.frameBinding = frame;
        this.frameBinding.sink(new Sink() {
            protected void onValue(Object value) {
                intrinsicContentSizeBinding.setCGSize(frameBinding.getCGFrame().getSize());
                needsRelayout(getCGFrame());
            }
        });
        return this;
    }

    public CGDrawable setFrame(CGFrame frame) {
        this.frameBinding.setCGFrame(frame);
        return this;
    }


    public void needsRedraw() {
        this.needsRelayout(this.getCGFrame());
    }

    public void needsRelayout(CGFrame frame) {
        if (this.getCanvas() != null) {
            this.getCanvas().repaint(frame);
        }
    }

    public Frame getFrame() {
        return this.frameBinding;
    }

    public CGFrame getCGFrame() {
        return this.frameBinding.getCGFrame();
    }

    protected int getColor() {
        if (color != null) {
           return color.getInt();
        }
        return 0x00000000;
    }

    public CGDrawable setOffset(Point offset) {
        this.offsetBinding = offset;
        this.offsetBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });
        return this;
    }
    
    public CGDrawable setOffset(int x, int y) {
        this.offsetBinding.setCGPoint(new CGPoint(x, y));
        return this;
    }

    public Point getOffset() {
        //TODO сделать специфические ЮИ биндинги - для сайза, фрейма итп
        return this.offsetBinding;
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
        CGFrame frame = getCGFrame();
        frame.width = width;
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
        CGFrame frame = getCGFrame();
        frame.height = height;
        this.setFrame(frame);

        return this;
    }

    public Int resizingMask() {
        return this.resizingMaskBinding;
    }

    public CGDrawable resizingMask(Int mask) {
        this.resizingMaskBinding = mask;
        this.resizingMaskBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });

        return this;
    }

    public CGDrawable resizingMask(int mask) {
        this.resizingMaskBinding.setInt(mask);
        return this;
    }

    public CGDrawable isVisible(boolean isVisible) {
        return this.isVisible(new Bool(isVisible));
    }

    public CGDrawable isVisible(Bool isVisible) {
        this.isVisible = isVisible;
        this.isVisible.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });
        return this;
    }

    public Bool isVisible() {
        return this.isVisible;
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

    public Size intrinsicContentSize() {
        return this.intrinsicContentSizeBinding;
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