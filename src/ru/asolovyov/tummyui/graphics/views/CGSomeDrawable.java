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

    protected CurrentValueSubject/*!!!<CGSize>!!!*/ intrinsicContentSizeBinding = new Size(CGSize.zero());

    protected CurrentValueSubject/*<Frame>*/ frameBinding = new CurrentValueSubject(new Frame(CGFrame.zero()));
    protected CurrentValueSubject/*<Point>*/ originBinding = new CurrentValueSubject(new Point(CGPoint.zero()));

    protected CurrentValueSubject/*<Int>*/ widthBinding = new CurrentValueSubject(new Int(0));
    protected CurrentValueSubject/*<Int>*/ heightBinding = new CurrentValueSubject(new Int(0));

    protected CurrentValueSubject/*<Int>*/ xBinding = new CurrentValueSubject(new Int(0));
    protected CurrentValueSubject/*<Int>*/ yBinding = new CurrentValueSubject(new Int(0));

    protected CurrentValueSubject/*<Int>*/ minXBinding = new CurrentValueSubject(new Int(0));
    protected CurrentValueSubject/*<Int>*/ minYBinding = new CurrentValueSubject(new Int(0));

    protected CurrentValueSubject/*<Int>*/ maxXBinding = new CurrentValueSubject(new Int(Integer.MAX_VALUE));
    protected CurrentValueSubject/*<Int>*/ maxYBinding = new CurrentValueSubject(new Int(Integer.MAX_VALUE));

    protected CurrentValueSubject/*<Int>*/ minWidthBinding = new CurrentValueSubject(new Int(0));
    protected CurrentValueSubject/*<Int>*/ minHeightBinding = new CurrentValueSubject(new Int(0));

    protected CurrentValueSubject/*<Int>*/ maxWidthBinding = new CurrentValueSubject(new Int(Integer.MAX_VALUE));
    protected CurrentValueSubject/*<Int>*/ maxHeightBinding = new CurrentValueSubject(new Int(Integer.MAX_VALUE));

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

    public CGDrawable frame(int x, int y, int width, int height) {
        return this.frame(new Frame(new CGFrame(x, y, width, height)));
    }

    public CGDrawable frame(Frame frame) {
        this.frameBinding.sendValue(frame);
        return this;
    }

    public CGDrawable frame(CGFrame frame) {
        ((Frame)this.frameBinding.getValue()).setCGFrame(frame);
        return this;
    }

    public CGDrawable backgroundColor(int colorHex) {
        return this.backgroundColor(new Int(colorHex));
    }

    public CGDrawable backgroundColor(Int backgroundColorHex) {
        this.backgroundColor.sendValue(backgroundColorHex);
        return this;
    }

    public CGDrawable color(int colorHex) {
        return this.color(new Int(colorHex));
    }

    public CGDrawable color(Int backgroundColorHex) {
        this.color.sendValue(backgroundColorHex);
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

    public CGFrame frame() {
        return ((Frame)this.frameBinding.getValue()).getCGFrame();
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

    public CGDrawable canvas(CGCanvas canvas) {
        this.canvas = canvas;
        this.setupSubscriptions();

        this.startHandlingKeyboard();
        canvas.needsRepaint().setBool(true);
        return this;
    }
    
    private void setupSubscriptions() {
        this.frameBinding.switchToLatest().removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        
        this.originBinding.switchToLatest().removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                CGPoint origin = (CGPoint)value;
                CGFrame frame = frame().origin(origin);
                frameBinding.sendValue(new Frame(frame));
            }
        });

        this.intrinsicContentSizeBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });

        Publisher.combineLatest(new IPublisher[]{
            this.xBinding.switchToLatest().removeDuplicates(), 
            this.yBinding.switchToLatest().removeDuplicates(),
            this.widthBinding.switchToLatest().removeDuplicates(),
            this.heightBinding.switchToLatest().removeDuplicates(),
        }).sink(new Sink() {
            protected void onValue(Object value) {
                Object[] values = ((Object[])value);
                int[] ints = new int[values.length];

                S.println("MASEV " + values + " COUNT " + values.length);
                for (int i = 0; i < values.length; i++) {
                    S.print(values[i] + " ");
                    ints[i] = values[i] == null ? 0 : ((Integer)values[i]).intValue();
                }
                S.println("");
                /*
MASEV [Ljava.lang.Object;@fe41703f COUNT 5
null 0 null null null
TRACE: <at java.lang.NullPointerException:   0>, startApp threw an Exception
java.lang.NullPointerException:   0
                 */
                CGFrame frame = frame();
                frame.x = ints[0];
                frame.y = ints[1];
                frame.width = ints[2];
                frame.height = ints[3];
                frameBinding.sendValue(new Frame(frame));
            }
        });

        Publisher.merge(new IPublisher[]{
                    this.minXBinding.switchToLatest().removeDuplicates(),
                    this.minYBinding.switchToLatest().removeDuplicates(),
                    this.maxXBinding.switchToLatest().removeDuplicates(),
                    this.maxYBinding.switchToLatest().removeDuplicates(),
                    this.minWidthBinding.switchToLatest().removeDuplicates(),
                    this.minHeightBinding.switchToLatest().removeDuplicates(),
                    this.maxWidthBinding.switchToLatest().removeDuplicates(),
                    this.maxHeightBinding.switchToLatest().removeDuplicates(),
                    this.color.switchToLatest().removeDuplicates(),
                    this.backgroundColor.switchToLatest().removeDuplicates(),
                    this.borderColor.switchToLatest().removeDuplicates(),
                    this.strokeStyle.switchToLatest().removeDuplicates(),
                    this.contentOffsetBinding.switchToLatest().removeDuplicates(),
                    this.contentInsetBinding.switchToLatest().removeDuplicates(),
                    this.cornerRadiusBinding.switchToLatest().removeDuplicates(),
                    this.isVisible.switchToLatest().removeDuplicates()
                }).sink(new Sink() {
                    protected void onValue(Object value) {
                        needsRedraw();
                    }
                });
    }

    public CGCanvas canvas() {
        return canvas;
    }

    protected int color() {
        return this.intValueFrom(color);
    }

    protected int backgroundColor() {
        return this.intValueFrom(backgroundColor);
    }

    protected int borderColor() {
        return this.intValueFrom(borderColor);
    }

    public CGDrawable origin(Point origin) {
        this.originBinding.sendValue(origin);
        return this;
    }

    public CGDrawable x(Int x) {
        this.xBinding.sendValue(x);
        return this;
    }

    public CGDrawable x(int x) {
        return x(new Int(x));
    }

    public CGDrawable y(Int y) {
        this.yBinding.sendValue(y);
        return this;
    }

    public CGDrawable y(int y) {
        return y(new Int(y));
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

    public int x() {
        return this.intValueFrom(xBinding);
    }

    public int y() {
        return this.intValueFrom(yBinding);
    }

    public int minX() {
        return this.intValueFrom(minXBinding);
    }

    public int minY() {
        return this.intValueFrom(minYBinding);
    }

    public int maxX() {
        return this.intValueFrom(maxXBinding);
    }

    public int maxY() {
        return this.intValueFrom(maxYBinding);
    }

    public int minWidth() {
        return this.intValueFrom(minWidthBinding);
    }

    public int minHeight() {
        return this.intValueFrom(minHeightBinding);
    }

    public int maxWidth() {
        return this.intValueFrom(maxWidthBinding);
    }

    public int maxHeight() {
        return this.intValueFrom(maxHeightBinding);
    }

    public CGDrawable width(Int width) {
        this.widthBinding.sendValue(width);
        return this;
    }

    public CGDrawable width(int width) {
        return width(new Int(width));
    }

    public CGDrawable height(Int height) {
        this.heightBinding.sendValue(height);
        return this;
    }
    
    public CGDrawable height(int height) {
        //TODO ИМПЕРАТИВНАЯ ХУЙНЯ!
        if (height == height()) {
            return this;
        }
        return height(new Int(height));
    }

    public int width() {
        return this.intValueFrom(widthBinding);
    }
    
    public int height() {
        return this.intValueFrom(heightBinding);
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
        return this.origin(new Point(new CGPoint(x, y)));
    }

    public CGPoint origin() {
        return ((Point)this.originBinding.getValue()).getCGPoint();
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
//                + ", x: " + minX() + "-" + maxX()
//                + ", y: " + minY() + "-" + maxY()
//                + "; wi: " + minWidth() + "-" + maxWidth()
//                + ", he: " + minHeight() + "-" + maxHeight()
                ;
    }

    private int intValueFrom(CurrentValueSubject binding) {
        return ((Int)binding.getValue()).getInt();
    }
}
