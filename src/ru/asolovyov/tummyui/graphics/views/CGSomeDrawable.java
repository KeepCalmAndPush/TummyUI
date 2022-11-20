/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.bindings.Frame;
import ru.asolovyov.tummyui.bindings.Insets;
import ru.asolovyov.tummyui.bindings.Point;
import ru.asolovyov.tummyui.bindings.Size;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;
import ru.asolovyov.tummyui.graphics.CGPoint;
import ru.asolovyov.tummyui.graphics.CGSize;

/**
 *
 * @author Администратор
 */
public abstract class CGSomeDrawable implements CGDrawable {

    public CGSomeDrawable() {
        super();
    }

    protected Frame frameBinding = new Frame(new CGFrame(0, 0, CG.VALUE_NOT_SET, CG.VALUE_NOT_SET));
    protected Point originBinding = new Point(CGPoint.zero());
    
    protected Int widthBinding = new Int(CG.VALUE_NOT_SET);
    protected Int heightBinding = new Int(CG.VALUE_NOT_SET);

    protected Size intrinsicContentSizeBinding = new Size(new CGSize(CG.VALUE_NOT_SET, CG.VALUE_NOT_SET));

    protected Int resizingMaskBinding = new Int(CGFrame.FLEXIBLE_ALL);

    protected Int color = new Int(CG.VALUE_NOT_SET);
    protected Int backgroundColor = new Int(CG.VALUE_NOT_SET);

    protected Int borderColor = new Int(CG.VALUE_NOT_SET);
    protected Int strokeStyle = new Int(Graphics.SOLID);

    protected Point contentOffsetBinding = new Point(CGPoint.zero());
    protected Insets contentInsetBinding = new Insets(CGInsets.zero());
    protected Size cornerRadiusBinding = new Size(CGSize.zero());

    protected Bool isVisible = new Bool(true);
    private CGCanvas canvas;

    private KeyboardHandler keyboardHandler;
    private GeometryReader geometryReader;

    public void draw(Graphics g) {
        CGFrame frame = intrinsicAwareFrame();
        if (frame == null) {
            return;
        }

        int backgroundColor = this.getBackgroundColor();
        if (backgroundColor != CG.VALUE_NOT_SET) {
            g.setColor(backgroundColor);
            g.fillRoundRect(
                    frame.x,
                    frame.y,
                    frame.width,
                    frame.height,
                    getCornerRadius().width,
                    getCornerRadius().height);
        }

        int borderColor = this.getBorderColor();
        if (borderColor != CG.VALUE_NOT_SET) {
            g.setStrokeStyle(this.getStrokeStyle());
            g.setColor(borderColor);
            g.drawRoundRect(
                    frame.x,
                    frame.y,
                    frame.width,
                    frame.height,
                    getCornerRadius().width,
                    getCornerRadius().height);
        }
    }

    public CGDrawable setFrame(int x, int y, int width, int height) {
        return this.setFrame(new Frame(new CGFrame(x, y, width, height)));
    }

    public CGDrawable setFrame(Frame frame) {
        this.frameBinding = frame;
        this.frameBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                CGFrame frame = (CGFrame)value;
                widthBinding.setInt(frame.width);
                heightBinding.setInt(frame.height);

                originBinding.setCGPoint(new CGPoint(frame.x, frame.y));

                updateIntrinsicContentSize();

                needsRelayout(frame);
            }
        });
        return this;
    }

    public CGDrawable setFrame(CGFrame frame) {
        this.frameBinding.setCGFrame(frame);
        return this;
    }

    public CGDrawable backgroundColor(int colorHex) {
        return this.backgroundColor(new Int(colorHex));
    }

    public CGDrawable backgroundColor(Int backgroundColorHex) {
        this.backgroundColor = backgroundColorHex;
        this.backgroundColor.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable color(int colorHex) {
        return this.color(new Int(colorHex));
    }

    public CGDrawable color(Int backgroundColorHex) {
        this.color = backgroundColorHex;
        this.color.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public void needsRedraw() {
        this.needsRelayout(this.getCGFrame().copy());
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
        return this.frameBinding.getCGFrame().copy();
    }

    public CGFrame intrinsicAwareFrame() {
        CGFrame frame = this.getCGFrame().copy();
        CGSize size = this.intrinsicContentSize().getCGSize();
        CGInsets insets = this.contentInsetBinding.getCGInsets();

        frame.width = size.width + insets.left + insets.right;
        frame.height = size.height + insets.top + insets.bottom;
        
        return frame;
    }

    public CGDrawable setCanvas(CGCanvas canvas) {
        this.canvas = canvas;
        this.setFrame(frameBinding);
        this.setOrigin(originBinding);
        this.setContentInset(contentInsetBinding);
        this.width(widthBinding);
        this.height(heightBinding);
        this.startHandlingKeyboard();
        canvas.needsRepaint().setBool(true);
        return this;
    }

    public CGCanvas getCanvas() {
        return canvas;
    }

    protected int getColor() {
        return color.getInt();
    }

    protected int getBackgroundColor() {
        return backgroundColor.getInt();
    }

    protected int getBorderColor() {
        return borderColor.getInt();
    }

    public CGDrawable setOrigin(Point origin) {
        this.originBinding = origin;
        
        this.originBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                CGPoint origin = (CGPoint)value;
                CGFrame frame = getCGFrame().copy();
                frame.x = origin.x;
                frame.y = origin.y;

                setFrame(frame);
            }
        });
        return this;
    }

    public CGDrawable width(Int width) {
        this.widthBinding = width;
        this.widthBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                CGFrame frame = getCGFrame().copy();
                frame.width = ((Integer)value).intValue();
                frameBinding.setCGFrame(frame);
            }
        });
        return this;
    }

    public CGDrawable width(int width) {
        return width(new Int(width));
    }

    public CGDrawable height(Int height) {
        this.heightBinding = height;
        this.heightBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
//                height(heightBinding.getInt());
                CGFrame frame = getCGFrame().copy();
                frame.height = ((Integer)value).intValue();
                frameBinding.setCGFrame(frame);
            }
        });
        return this;
    }

    public CGDrawable height(int height) {
        return height(new Int(height));
    }

    public int getWidth() {
        return this.widthBinding.getInt();
    }
    
    public int getHeight() {
        return this.heightBinding.getInt();
    }

    public Int resizingMask() {
        return this.resizingMaskBinding;
    }

    public CGDrawable resizingMask(Int mask) {
        this.resizingMaskBinding = mask;
        this.resizingMaskBinding.removeDuplicates().sink(new Sink() {
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

    public CGDrawable sizeToFit() {
        return this;
    }

    public Size intrinsicContentSize() {
        return this.intrinsicContentSizeBinding;
    }

    protected void updateIntrinsicContentSize() {
        this.intrinsicContentSizeBinding.setCGSize(this.frameBinding.getCGFrame().getCGSize().copy());
    }

    public CGDrawable readGeometry(GeometryReader reader) {
        this.geometryReader = reader;
        return this;
    }

    public GeometryReader getGeometryReader() {
        return geometryReader;
    }
    
    public CGDrawable handleKeyboard(KeyboardHandler handler) {
        this.keyboardHandler = handler;
        this.startHandlingKeyboard();
        return this;
    }

    public KeyboardHandler getKeyboardHandler() {
        return this.keyboardHandler;
    }

    private void startHandlingKeyboard() {
        if (this.keyboardHandler == null || this.canvas == null) {
            return;
        }

        this.canvas.getKeyPressed().sink(new Sink() {
            protected void onValue(Object value) {
                keyboardHandler.keyPressed(
                        CGSomeDrawable.this,
                        CGSomeDrawable.this.canvas.getKeyPressed().getInt());
            }
        });

        this.canvas.getKeyReleased().sink(new Sink() {
            protected void onValue(Object value) {
                keyboardHandler.keyReleased(
                        CGSomeDrawable.this,
                        CGSomeDrawable.this.canvas.getKeyReleased().getInt());
            }
        });

        this.canvas.getKeyRepeated().sink(new Sink() {
            protected void onValue(Object value) {
                keyboardHandler.keyRepeated(
                        CGSomeDrawable.this,
                        CGSomeDrawable.this.canvas.getKeyRepeated().getInt());
            }
        });
    }

    public CGDrawable setOrigin(int x, int y) {
        this.originBinding.setCGPoint(new CGPoint(x, y));
        return this;
    }

    public Point getOrigin() {
        return this.originBinding;
    }

    public CGDrawable setContentOffset(Point offset) {
        this.contentOffsetBinding = offset;
        this.contentOffsetBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame().copy());
            }
        });
        return this;
    }

    public CGDrawable setContentOffset(int x, int y) {
        this.contentOffsetBinding.setCGPoint(new CGPoint(x, y));
        return this;
    }

    public CGDrawable setContentInset(Insets inset) {
        this.contentInsetBinding = inset;
        this.contentInsetBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame().copy());
            }
        });
        return this;
    }

    public CGDrawable setContentInset(int top, int left, int bottom, int right) {
        this.contentInsetBinding.setCGInsets(new CGInsets(top, left, bottom, right));
        return this;
    }

    public Point getContentOffset() {
        return this.contentOffsetBinding;
    }

    public Insets getContentInset() {
        return this.contentInsetBinding;
    }

    public CGDrawable stroke(int strokeStyle) {
        return this.stroke(new Int(strokeStyle));
    }

    public CGDrawable stroke(Int strokeStyle) {
        this.strokeStyle = strokeStyle;
        this.strokeStyle.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    protected int getStrokeStyle() {
        return strokeStyle.getInt();
    }

    public CGDrawable cornerRaduis(CGSize cornerRadius) {
        return this.cornerRaduis(new Size(cornerRadius));
    }

    public CGDrawable cornerRaduis(Size cornerRadiusBinding) {
        this.cornerRadiusBinding = cornerRadiusBinding;
        this.cornerRadiusBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGSize getCornerRadius() {
        if (this.cornerRadiusBinding != null) {
            return this.cornerRadiusBinding.getCGSize().copy();
        }
        return CGSize.zero();
    }

    public CGDrawable borderColor(int borderColorHex) {
        return this.borderColor(new Int(borderColorHex));
    }

    public CGDrawable borderColor(Int borderColorHex) {
        this.borderColor = borderColorHex;
        this.borderColor.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public void animate(int durationMillis, Runnable animations) {
        final int backgroundColor = this.getBackgroundColor();
        final int strokeColor = this.getBorderColor();

        final CGFrame frame = this.getCGFrame().copy();
        final CGPoint origin = this.getOrigin().getCGPoint().copy();

        final CGPoint contentOffset = this.getContentOffset().getCGPoint().copy();
        final CGInsets contentInset = this.getContentInset().getCGInsets().copy();
        final CGSize cornerRadius = this.getCornerRadius().copy();

        animations.run();

        // TODO посчитать дельты!
    }
}
