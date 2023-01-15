/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.publishers.Publisher;
import ru.asolovyov.combime.subjects.CurrentValueSubject;
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
    protected CurrentValueSubject/*!!!<CGSize>!!!*/ intrinsicContentSizeBinding = new Size(CGSize.zero());

    protected CurrentValueSubject/*<Int>*/ xBinding = new CurrentValueSubject(new Int(CG.NULL));
    protected CurrentValueSubject/*<Int>*/ yBinding = new CurrentValueSubject(new Int(CG.NULL));
    
    protected CurrentValueSubject/*<Int>*/ widthBinding = new CurrentValueSubject(new Int(CG.NULL));
    protected CurrentValueSubject/*<Int>*/ heightBinding = new CurrentValueSubject(new Int(CG.NULL));

    protected CurrentValueSubject/*<Int>*/ minXBinding = new CurrentValueSubject(new Int(CG.NULL));
    protected CurrentValueSubject/*<Int>*/ minYBinding = new CurrentValueSubject(new Int(CG.NULL));

    protected CurrentValueSubject/*<Int>*/ maxXBinding = new CurrentValueSubject(new Int(CG.NULL));
    protected CurrentValueSubject/*<Int>*/ maxYBinding = new CurrentValueSubject(new Int(CG.NULL));

    protected CurrentValueSubject/*<Int>*/ minWidthBinding = new CurrentValueSubject(new Int(CG.NULL));
    protected CurrentValueSubject/*<Int>*/ minHeightBinding = new CurrentValueSubject(new Int(CG.NULL));

    protected CurrentValueSubject/*<Int>*/ maxWidthBinding = new CurrentValueSubject(new Int(CG.NULL));
    protected CurrentValueSubject/*<Int>*/ maxHeightBinding = new CurrentValueSubject(new Int(CG.NULL));

    protected CurrentValueSubject/*<Int>*/ color = new CurrentValueSubject(new Int(CG.NULL));
    protected CurrentValueSubject/*<Int>*/ backgroundColor = new CurrentValueSubject(new Int(CG.NULL));
    protected CurrentValueSubject/*<Int>*/ borderColor = new CurrentValueSubject(new Int(CG.NULL));
    protected CurrentValueSubject/*<Int>*/ strokeStyle = new CurrentValueSubject(new Int(Graphics.SOLID));

    protected CurrentValueSubject/*<Point>*/ contentOffsetBinding = new CurrentValueSubject(new Point(CGPoint.zero()));
    protected CurrentValueSubject/*<Insets>*/ contentInsetBinding = new CurrentValueSubject(new Insets(CGInsets.zero()));
    protected CurrentValueSubject/*<Size>*/ cornerRadiusBinding = new CurrentValueSubject(new Size(CGSize.zero()));

    protected CurrentValueSubject/*<Bool>*/ isVisible = new CurrentValueSubject(new Bool(true));
    private CGCanvas canvas;

    private KeyboardHandler keyboardHandler;
    private GeometryReader geometryReader;
    
    public CGSomeDrawable() {
        super();
        this.setupSubscriptions();
    }

    public CGDrawable canvas(CGCanvas canvas) {
        this.canvas = canvas;

        this.startHandlingKeyboard();
        this.updateIntrinsicContentSize();
        canvas.setNeedsRepaint();
        return this;
    }

    private void setupSubscriptions() {
        this.intrinsicContentSizeBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                S.println(CGSomeDrawable.this + " DID UPDATE INTRINSIC " + value);
                needsRelayout();
            }
        });

        Publisher xyWidthHeight = (Publisher) Publisher.combineLatest(new IPublisher[]{
            this.xBinding.switchToLatest(),
            this.yBinding.switchToLatest(),
            this.widthBinding.switchToLatest(),
            this.heightBinding.switchToLatest(),
        });

        xyWidthHeight.sink(new Sink() {
            protected void onValue(Object value) {
                Object[] values = ((Object[])value);
                S.println("XYWH 4: " + S.arrayToString(values));
                needsRelayout();
            }
        });
        
        Publisher.combineLatest(new IPublisher[] {
                    this.color.switchToLatest(),
                    this.backgroundColor.switchToLatest(),
                    this.borderColor.switchToLatest(),
                    this.strokeStyle.switchToLatest(),
                    this.contentOffsetBinding.switchToLatest(),
                    this.contentInsetBinding.switchToLatest(),
                    this.cornerRadiusBinding.switchToLatest(),
                    this.isVisible.switchToLatest()
                }).sink(new Sink() {
                    protected void onValue(Object value) {
                        Object[] values = ((Object[])value);
                        S.println("8 COLORS: " + S.arrayToString(values));

                        needsRelayout();
                    }
                });

        Publisher minsMaxes = (Publisher) Publisher.combineLatest(new IPublisher[]{
                    this.minXBinding.switchToLatest(),
                    this.minYBinding.switchToLatest(),
                    this.maxXBinding.switchToLatest(),
                    this.maxYBinding.switchToLatest(),
                    this.minWidthBinding.switchToLatest(),
                    this.minHeightBinding.switchToLatest(),
                    this.maxWidthBinding.switchToLatest(),
                    this.maxHeightBinding.switchToLatest()
        });

        minsMaxes.sink(new Sink() {
            protected void onValue(Object value) {
                Object[] values = ((Object[])value);
                S.println("8 MINMAX: " + S.arrayToString(values));
                needsRelayout();
            }
        });
    }

    public CGDrawable width(Int width) {
        S.println(this + " KEK WILL SET WIDTH BINDING " + width);
        this.widthBinding.sendValue(width);
        int value = width.getInt();
        if (intValueFrom(minWidthBinding) == CG.NULL && value != CG.NULL) {
            minWidth(value);
        }
        if (intValueFrom(maxWidthBinding) == CG.NULL && value != CG.NULL) {
            maxWidth(value);
        }
        return this;
    }

    public CGDrawable width(int width) {
        S.println(this + " KEK WILL SET WIDTH " + width);
        return width(new Int(width));
    }

    public CGDrawable height(Int height) {
        this.heightBinding.sendValue(height);
        int value = height.getInt();
        if (intValueFrom(minHeightBinding) == CG.NULL && value != CG.NULL) {
            minHeight(value);
        }
        if (intValueFrom(maxHeightBinding) == CG.NULL && value != CG.NULL) {
            maxHeight(value);
        }
        return this;
    }

    public CGDrawable height(int height) {
        S.println(this + " KEK WILL SET HEIGHT " + height);
        return height(new Int(height));
    }

    public int width() {
        int value = this.intValueFrom(widthBinding);
        return value == CG.NULL ? 0 : value;
    }

    public int height() {
        int value = this.intValueFrom(heightBinding);
        return value == CG.NULL ? 0 : value;
    }

    public int x() {
        int value = this.intValueFrom(xBinding);
        return value == CG.NULL ? 0 : value;
    }

    public int y() {
        int value = this.intValueFrom(yBinding);
        return value == CG.NULL ? 0 : value;
    }

    public CGDrawable x(Int x) {
        this.xBinding.sendValue(x);
        int value = x.getInt();

        if (intValueFrom(minXBinding) == CG.NULL && value != CG.NULL) {
            minX(value);
        }
        if (intValueFrom(maxXBinding) == CG.NULL && value != CG.NULL) {
            maxX(value);
        }
        return this;
    }

    public CGDrawable x(int x) {
        return x(new Int(x));
    }

    public CGDrawable y(Int y) {
        this.yBinding.sendValue(y);
        int value = y.getInt();
        if (intValueFrom(minYBinding) == CG.NULL && value != CG.NULL) {
            minY(value);
        }
        if (intValueFrom(maxYBinding) == CG.NULL && value != CG.NULL) {
            maxY(value);
        }
        return this;
    }

    public CGDrawable y(int y) {
        return y(new Int(y));
    }

    public void draw(Graphics g) {
        CGFrame frame = intrinsicAwareFrame();
        if (frame == null) {
            return;
        }

        int backgroundColor = this.backgroundColor();
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

        int borderColor = this.borderColor();
        if (borderColor != CG.NULL) {
            g.setStrokeStyle(this.strokeStyle());
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

    public CGFrame frame() {
        return new CGFrame(x(), y(), width(), height());
    }

    public CGDrawable frame(int x, int y, int width, int height) {
        CGFrame frame = new CGFrame(x, y, width, height);
        S.println(this + " will be given a frame comps " + frame);
        
        return this.frame(frame);
    }

    public CGDrawable frame(CGFrame frame) {
        S.println(this + " will be given a CGFrame " + frame);
//        ((Frame)this.frameBinding.getValue()).setCGFrame(frame);
        this.x(frame.x);
        this.y(frame.y);
        this.width(frame.width);
        this.height(frame.height);
        
        return this;
    }

    public int backgroundColor() {
        return intValueFrom(this.backgroundColor);
    }

    public CGDrawable backgroundColor(int colorHex) {
        return this.backgroundColor(new Int(colorHex));
    }

    public CGDrawable backgroundColor(Int backgroundColorHex) {
        this.backgroundColor.sendValue(backgroundColorHex);
        return this;
    }

    public int color() {
        return intValueFrom(this.color);
    }

    public CGDrawable color(int colorHex) {
        return this.color(new Int(colorHex));
    }

    public CGDrawable color(Int backgroundColorHex) {
        this.color.sendValue(backgroundColorHex);
        return this;
    }

    public void needsRelayout() {
        this.needsRelayout(this.frame());
    }

    public void needsRelayout(CGFrame frame) {
        if (this.canvas() != null) {
            this.updateIntrinsicContentSize();
            this.canvas().repaint(frame);
        }
    }
    
    public CGFrame intrinsicAwareFrame() {
        CGFrame frame = this.frame();
        S.println(this + " WILL SAY ITS INTRAWARE FRAME!");

        CGSize size = this.intrinsicContentSize();
        CGInsets insets = this.contentInset();

        int width = size.width + insets.left + insets.right;
        int height = size.height + insets.top + insets.bottom;

        width = Math.max(width, frame.width);
        height = Math.max(height, frame.height);

        frame.width = width;
        frame.height = height;

        S.println(this + " INTRAWARE FRAME IS " + frame);

        return frame;
    }

    public CGCanvas canvas() {
        return canvas;
    }

    public int borderColor() {
        return this.intValueFrom(borderColor);
    }

    public CGDrawable minX(Int minX) {
        this.minXBinding.sendValue(minX);
        return this;
    }

    public CGDrawable minX(int minX) {
        return minX(new Int(minX));
    }

    public CGDrawable minY(Int minY) {
        this.minYBinding.sendValue(minY);
        return this;
    }

    public CGDrawable minY(int minY) {
        return minY(new Int(minY));
    }

    public CGDrawable maxX(Int maxX) {
        this.maxXBinding.sendValue(maxX);
        return this;
    }

    public CGDrawable maxX(int maxX) {
        return maxX(new Int(maxX));
    }

    public CGDrawable maxY(Int maxY) {
        this.maxYBinding.sendValue(maxY);
        return this;
    }

    public CGDrawable maxY(int maxY) {
        return maxY(new Int(maxY));
    }

    public CGDrawable minWidth(Int minWidth) {
        this.minWidthBinding.sendValue(minWidth);
        return this;
    }

    public CGDrawable minWidth(int minWidth) {
        return minWidth(new Int(minWidth));
    }

    public CGDrawable minHeight(Int minHeight) {
        this.minHeightBinding.sendValue(minHeight);
        return this;
    }

    public CGDrawable minHeight(int minHeight) {
        return minHeight(new Int(minHeight));
    }

    public CGDrawable maxWidth(Int maxWidth) {
        this.maxWidthBinding.sendValue(maxWidth);
        return this;
    }

    public CGDrawable maxWidth(int maxWidth) {
        return maxWidth(new Int(maxWidth));
    }

    public CGDrawable maxHeight(Int maxHeight) {
        this.maxHeightBinding.sendValue(maxHeight);
        return this;
    }

    public CGDrawable maxHeight(int maxHeight) {
        return maxHeight(new Int(maxHeight));
    }

    public int minX() {
        int value = this.intValueFrom(minXBinding);
        return value == CG.NULL ? 0 : value;
    }

    public int minY() {
        int value = this.intValueFrom(minYBinding);
        return value == CG.NULL ? 0 : value;
    }

    public int maxX() {
        int value = this.intValueFrom(maxXBinding);
        return value == CG.NULL ? Integer.MAX_VALUE : value;
    }

    public int maxY() {
        int value = this.intValueFrom(maxYBinding);
        return value == CG.NULL ? Integer.MAX_VALUE : value;
    }

    public int minWidth() {
        int value = this.intValueFrom(minWidthBinding);
        return value == CG.NULL ? 0 : value;
    }

    public int minHeight() {
        int value = this.intValueFrom(minHeightBinding);
        return value == CG.NULL ? 0 : value;
    }

    public int maxWidth() {
        int value = this.intValueFrom(maxWidthBinding);
        return value == CG.NULL ? Integer.MAX_VALUE : value;
    }

    public int maxHeight() {
        int value = this.intValueFrom(maxHeightBinding);
        return value == CG.NULL ? Integer.MAX_VALUE : value;
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
    
    public CGDrawable isVisible(boolean isVisible) {
        return this.isVisible(new Bool(isVisible));
    }

    public CGDrawable isVisible(Bool isVisible) {
        this.isVisible.sendValue(isVisible);
        return this;
    }

    public boolean isVisible() {
        return ((Bool)this.isVisible.getValue()).getBoolean();
    }

    public CGDrawable sizeToFit() {
        return this;
    }

    public CGSize intrinsicContentSize() {
        return (CGSize)this.intrinsicContentSizeBinding.getValue(); //SIC!
    }

    protected void updateIntrinsicContentSize() {
        S.println(this + " WILL updateIntrinsicContentSize()");
        this.intrinsicContentSizeBinding.sendValue(this.frame().getCGSize()); //SIC!
    }

    public CGDrawable readGeometry(GeometryReader reader) {
        this.geometryReader = reader;
        return this;
    }

    public GeometryReader geometryReader() {
        return geometryReader;
    }
    
    public CGDrawable handleKeyboard(KeyboardHandler handler) {
        this.keyboardHandler = handler;
        this.startHandlingKeyboard();
        return this;
    }

    public KeyboardHandler keyboardHandler() {
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
        x(x); y(y);
        return this;
    }

    public CGPoint origin() {
        return new CGPoint(x(), y());
    }

    public CGDrawable contentOffset(Point offset) {
        this.contentOffsetBinding.sendValue(offset);
        return this;
    }

    public CGDrawable contentOffset(int x, int y) {
        this.contentOffsetBinding.sendValue(new Point(new CGPoint(x, y)));
        return this;
    }

    public CGDrawable contentInset(Insets inset) {
        this.contentInsetBinding.sendValue(inset);
        return this;
    }

    public CGDrawable contentInset(int top, int left, int bottom, int right) {
        CGInsets insets = new CGInsets(top, left, bottom, right);
        this.contentInsetBinding.sendValue(new Insets(insets));
        return this;
    }

    public CGPoint contentOffset() {
        return ((Point)this.contentOffsetBinding.getValue()).getCGPoint();
    }

    public CGInsets contentInset() {
        return ((Insets)this.contentInsetBinding.getValue()).getCGInsets();
    }

    public CGDrawable stroke(int strokeStyle) {
        return this.stroke(new Int(strokeStyle));
    }

    public CGDrawable stroke(Int strokeStyle) {
        this.strokeStyle.sendValue(strokeStyle);
        return this;
    }

    protected int strokeStyle() {
        return this.intValueFrom(strokeStyle);
    }

    public CGDrawable cornerRaduis(CGSize cornerRadius) {
        return this.cornerRaduis(new Size(cornerRadius));
    }

    public CGDrawable cornerRaduis(Size cornerRadiusBinding) {
        this.cornerRadiusBinding.sendValue(cornerRadiusBinding);
        return this;
    }

    public CGSize cornerRadius() {
        return ((Size)this.cornerRadiusBinding.getValue()).getCGSize();
    }

    public CGDrawable borderColor(int borderColorHex) {
        return this.borderColor(new Int(borderColorHex));
    }

    public CGDrawable borderColor(Int borderColorHex) {
        this.borderColor.sendValue(borderColorHex);
        return this;
    }

    public void animate(int durationMillis, Runnable animations) {
        final int backgroundColor = this.backgroundColor();
        final int strokeColor = this.borderColor();

        final CGFrame frame = this.frame();
        final CGPoint origin = this.origin();

        final CGPoint contentOffset = this.contentOffset();
        final CGInsets contentInset = this.contentInset();
        final CGSize cornerRadius = this.cornerRadius();

        animations.run();

        // TODO посчитать дельты!
    }

    public String toString() {
        return S.stripPackageName(super.toString()) + " " + frame()
                + ", {x: " + minX() + "..." + orMax(maxX())
                + ", y: " + minY() + "..." + orMax(maxY())
                + "; wi: " + minWidth() + "..." + orMax(maxWidth())
                + ", he: " + minHeight() + "..." + orMax(maxHeight()) + "}"
                ;
    }

    private String orMax(int value) {
        return value == Integer.MAX_VALUE ? "INF" : "" + value;
    }

    private int intValueFrom(CurrentValueSubject binding) {
        int value = ((Int)binding.getValue()).getInt();
        return value;
    }

    private void intValueTo(CurrentValueSubject binding, int value) {
        ((Int)binding.getValue()).setInt(value);
    }
}
