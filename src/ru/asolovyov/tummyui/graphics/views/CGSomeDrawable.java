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

    protected Frame frameBinding = new Frame(CGFrame.zero());
    protected Point originBinding = new Point(CGPoint.zero());

    protected Int xBinding = new Int(0);
    protected Int yBinding = new Int(0);

    protected Int minXBinding = new Int(0);
    protected Int minYBinding = new Int(0);

    protected Int maxXBinding = new Int(Integer.MAX_VALUE);
    protected Int maxYBinding = new Int(Integer.MAX_VALUE);
    
    protected Int widthBinding = new Int(0);
    protected Int heightBinding = new Int(0);

    protected Int minWidthBinding = new Int(0);
    protected Int minHeightBinding = new Int(0);

    protected Int maxWidthBinding = new Int(Integer.MAX_VALUE);
    protected Int maxHeightBinding = new Int(Integer.MAX_VALUE);

    protected Size intrinsicContentSizeBinding = new Size(CGSize.zero());

    protected Int color = new Int(CG.NULL);
    protected Int backgroundColor = new Int(CG.NULL);

    protected Int borderColor = new Int(CG.NULL);
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
        if (backgroundColor != CG.NULL) {
            g.setColor(backgroundColor);
            g.fillRoundRect(
                    frame.x,
                    frame.y,
                    frame.width,
                    frame.height,
                    cornerRadius().width,
                    cornerRadius().height);
        }

        int borderColor = this.getBorderColor();
        if (borderColor != CG.NULL) {
            g.setStrokeStyle(this.getStrokeStyle());
            g.setColor(borderColor);
            g.drawRoundRect(
                    frame.x,
                    frame.y,
                    frame.width,
                    frame.height,
                    cornerRadius().width,
                    cornerRadius().height);
        }
    }

    public CGDrawable frame(int x, int y, int width, int height) {
        return this.frame(new Frame(new CGFrame(x, y, width, height)));
    }

    public CGDrawable frame(Frame frame) {
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
        this.needsRelayout(this.frame());
    }

    public void needsRelayout(CGFrame frame) {
        if (this.canvas() != null) {
            this.canvas().repaint(frame);
        }
    }

    public Frame getFrame() {
        return this.frameBinding;
    }

    public CGFrame frame() {
        return this.frameBinding.getCGFrame();
    }

    public CGFrame intrinsicAwareFrame() {
        CGFrame frame = this.frame();
        CGSize size = this.intrinsicContentSize();
        CGInsets insets = this.contentInsetBinding.getCGInsets();

        frame.width = size.width + insets.left + insets.right;
        frame.height = size.height + insets.top + insets.bottom;
        
        return frame;
    }

    public CGDrawable canvas(CGCanvas canvas) {
        this.canvas = canvas;
        this.frame(frameBinding);
        this.origin(originBinding);
        this.contentInset(contentInsetBinding);
        this.width(widthBinding);
        this.height(heightBinding);

        this.startHandlingKeyboard();
        canvas.needsRepaint().setBool(true);
        return this;
    }

    public CGCanvas canvas() {
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

    public CGDrawable origin(Point origin) {
        this.originBinding = origin;
        
        this.originBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                CGPoint origin = (CGPoint)value;
                CGFrame frame = frame();
                frame.x = origin.x;
                frame.y = origin.y;

                setFrame(frame);
            }
        });
        return this;
    }

    public CGDrawable x(Int x) {
        this.xBinding = x;
        this.xBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                CGFrame frame = frame();
                frame.x = ((Integer)value).intValue();
                frameBinding.setCGFrame(frame);
            }
        });
        return this;
    }

    public CGDrawable x(int x) {
        return x(new Int(x));
    }

    public CGDrawable y(Int y) {
        this.yBinding = y;
        this.yBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                CGFrame frame = frame();
                frame.y = ((Integer)value).intValue();
                frameBinding.setCGFrame(frame);
            }
        });
        return this;
    }

    public CGDrawable y(int y) {
        return y(new Int(y));
    }

    public CGDrawable minX(Int minX) {
        this.minXBinding = minX;
        this.minXBinding.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable minX(int minX) {
        return minX(new Int(minX));
    }

    public CGDrawable minY(Int minY) {
        this.minYBinding = minY;
        this.minYBinding.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable minY(int minY) {
        return minY(new Int(minY));
    }

    public CGDrawable maxX(Int maxX) {
        this.maxXBinding = maxX;
        this.maxXBinding.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable maxX(int maxX) {
        return maxX(new Int(maxX));
    }

    public CGDrawable maxY(Int maxY) {
        this.maxYBinding = maxY;
        this.maxYBinding.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable maxY(int maxY) {
        return maxY(new Int(maxY));
    }

    public CGDrawable minWidth(Int minWidth) {
        this.minWidthBinding = minWidth;
        this.minWidthBinding.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable minWidth(int minWidth) {
        return minWidth(new Int(minWidth));
    }

    public CGDrawable minHeight(Int minHeight) {
        this.minHeightBinding = minHeight;
        this.minHeightBinding.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable minHeight(int minHeight) {
        return minHeight(new Int(minHeight));
    }

    public CGDrawable maxWidth(Int maxWidth) {
        this.maxWidthBinding = maxWidth;
        this.maxWidthBinding.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable maxWidth(int maxWidth) {
        return maxWidth(new Int(maxWidth));
    }

    public CGDrawable maxHeight(Int maxHeight) {
        this.maxHeightBinding = maxHeight;
        this.maxHeightBinding.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable maxHeight(int maxHeight) {
        return maxHeight(new Int(maxHeight));
    }

    public int x() {
        return this.xBinding.getInt();
    }

    public int y() {
        return this.yBinding.getInt();
    }

    public int minX() {
        return this.minXBinding.getInt();
    }

    public int minY() {
        return this.minYBinding.getInt();
    }

    public int maxX() {
        return this.maxXBinding.getInt();
    }

    public int maxY() {
        return this.maxYBinding.getInt();
    }

    public int minWidth() {
        return this.minWidthBinding.getInt();
    }

    public int minHeight() {
        return this.minHeightBinding.getInt();
    }

    public int maxWidth() {
        return this.maxWidthBinding.getInt();
    }

    public int maxHeight() {
        return this.maxHeightBinding.getInt();
    }

    public CGDrawable width(Int width) {
        this.widthBinding = width;
        this.widthBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                CGFrame frame = frame();
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
                CGFrame frame = frame();
                frame.height = ((Integer)value).intValue();
                frameBinding.setCGFrame(frame);
            }
        });
        return this;
    }

    public CGDrawable height(int height) {
        return height(new Int(height));
    }

    public int width() {
        return this.widthBinding.getInt();
    }
    
    public int height() {
        return this.heightBinding.getInt();
    }

    public boolean hasGrowableWidth() {
        return this.maxWidth() > this.width();
    }

    public boolean hasShrinkableWidth() {
        return this.minWidth() < this.width();
    }

    public boolean hasShrinkableHeight() {
        return this.minHeight() < this.height();
    }

    public boolean hasGrowableHeight() {
        return this.maxHeight() > this.height();
    }

    public int updateX(int x) {
        int value = CG.clamp(x, minX(), maxX());
        this.x(value);
        return value;
    }

    public int updateY(int y) {
        int value = CG.clamp(y, minY(), maxY());
        this.y(value);
        return value;
    }

    public int updateWidth(int width) {
        int value = CG.clamp(width, minWidth(), maxWidth());
        this.width(value);
        return value;
    }

    public int updateHeight(int height) {
        int value = CG.clamp(height, minHeight(), maxX());
        this.height(value);
        return value;
    }

    public CGDrawable isVisible(boolean isVisible) {
        return this.isVisible(new Bool(isVisible));
    }

    public CGDrawable isVisible(Bool isVisible) {
        this.isVisible = isVisible;
        this.isVisible.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(frame());
            }
        });
        return this;
    }

    public boolean isVisible() {
        return this.isVisible.getBoolean();
    }

    public CGDrawable sizeToFit() {
        return this;
    }

    public CGSize intrinsicContentSize() {
        return this.intrinsicContentSizeBinding.getCGSize();
    }

    protected void updateIntrinsicContentSize() {
        this.intrinsicContentSizeBinding.setCGSize(this.frame().getCGSize());
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

    public CGDrawable origin(int x, int y) {
        this.originBinding.setCGPoint(new CGPoint(x, y));
        return this;
    }

    public CGPoint origin() {
        return this.originBinding.getCGPoint();
    }

    public CGDrawable contentOffset(Point offset) {
        this.contentOffsetBinding = offset;
        this.contentOffsetBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(frame());
            }
        });
        return this;
    }

    public CGDrawable contentOffset(int x, int y) {
        this.contentOffsetBinding.setCGPoint(new CGPoint(x, y));
        return this;
    }

    public CGDrawable contentInset(Insets inset) {
        this.contentInsetBinding = inset;
        this.contentInsetBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(frame());
            }
        });
        return this;
    }

    public CGDrawable contentInset(int top, int left, int bottom, int right) {
        this.contentInsetBinding.setCGInsets(new CGInsets(top, left, bottom, right));
        return this;
    }

    public CGPoint contentOffset() {
        return this.contentOffsetBinding.getCGPoint();
    }

    public CGInsets contentInset() {
        return this.contentInsetBinding.getCGInsets();
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

    public CGSize cornerRadius() {
        if (this.cornerRadiusBinding != null) {
            return this.cornerRadiusBinding.getCGSize();
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

        final CGFrame frame = this.frame();
        final CGPoint origin = this.origin();

        final CGPoint contentOffset = this.contentOffset();
        final CGInsets contentInset = this.contentInset();
        final CGSize cornerRadius = this.cornerRadius();

        animations.run();

        // TODO посчитать дельты!
    }
}
