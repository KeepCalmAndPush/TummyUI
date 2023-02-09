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
import ru.asolovyov.tummyui.bindings.Insets;
import ru.asolovyov.tummyui.bindings.Point;
import ru.asolovyov.tummyui.bindings.Size;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGAnimation;
import ru.asolovyov.tummyui.graphics.CGDisplayLink;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;
import ru.asolovyov.tummyui.graphics.CGPoint;
import ru.asolovyov.tummyui.graphics.CGSize;

/**
 *
 * @author Администратор
 */
public abstract class CGSomeDrawable implements CGDrawable {
    protected Size intrinsicContentSizeBinding = new Size(CGSize.zero());

    //TODO сделать получение корректного последнего значения, а не значения из последнего переданного Биндинга.
    protected Int xBinding = new Int(CG.NULL);
    protected Int yBinding = new Int(CG.NULL);
    
    protected Int widthBinding = new Int(CG.NULL);
    protected Int heightBinding = new Int(CG.NULL);

    protected Int minXBinding = new Int(CG.NULL);
    protected Int minYBinding = new Int(CG.NULL);

    protected Int maxXBinding = new Int(CG.NULL);
    protected Int maxYBinding = new Int(CG.NULL);

    protected Int minWidthBinding = new Int(CG.NULL);
    protected Int minHeightBinding = new Int(CG.NULL);

    protected Int maxWidthBinding = new Int(CG.NULL);
    protected Int maxHeightBinding = new Int(CG.NULL);

    protected Int color = new Int(CG.NULL);
    protected Int backgroundColor = new Int(CG.NULL);
    protected Int borderColor = new Int(CG.NULL);
    
    protected Int borderWidth = new Int(0);
    protected Int shadowColor = new Int(CG.NULL);
    protected Point shadowOffset = new Point(1, 1);

    protected Int strokeStyle = new Int(Graphics.SOLID);

    protected Point offsetBinding = new Point(CGPoint.zero());
    protected Insets contentInsetBinding = new Insets(CGInsets.zero());
    protected Int cornerRadiusBinding = new Int(0);

    protected Bool isVisible = new Bool(true);

    private int flexibilityWidth = CGDrawable.FLEXIBILITY_DEFAULT;
    private int flexibilityHeight = CGDrawable.FLEXIBILITY_DEFAULT;

    private CGCanvas canvas;

    private KeyboardHandler keyboardHandler;
    private GeometryReader geometryReader;

    protected boolean isXSetByUser = false;
    protected boolean isYSetByUser = false;
    protected boolean isWidthSetByUser = false;
    protected boolean isHeightSetByUser = false;
    
    public CGSomeDrawable() {
        super();
        this.setupSubscriptions();
    }

    public CGDrawable canvas(CGCanvas canvas) {
        this.canvas = canvas;

        S.println("WILL SET CANVAS! " + canvas);

        if (canvas == null) {
            return this;
        }

        S.println("DID SET CANVAS! " + canvas);
        
        this.startHandlingKeyboard();
        this.updateIntrinsicContentSize();
        canvas.setNeedsRepaint();
        return this;
    }

    private void setupSubscriptions() {
        this.intrinsicContentSizeBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                S.println(CGSomeDrawable.this + " DID UPDATE INTRINSIC " + value);
                relayout();
            }
        });

        Publisher.combineLatest(new IPublisher[]{
            this.xBinding,
            this.yBinding,
            this.widthBinding,
            this.heightBinding,
        }).removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                Object[] values = (Object[]) value;
                S.println(CGSomeDrawable.this + " XYWH 4: " + S.arrayToString(values));

                relayout();
            }
        });
        
        Publisher.combineLatest(new IPublisher[] {
                    this.shadowColor,
                    this.shadowOffset,
                    this.offsetBinding,
                    this.contentInsetBinding,
                    this.cornerRadiusBinding,
                    this.isVisible
                }).removeDuplicates().sink(new Sink() {
                    protected void onValue(Object value) {
                        Object[] values = ((Object[])value);
                        S.println("8 COLORS: " + S.arrayToString(values));

                        relayout();
                    }
                });

        Publisher.combineLatest(new IPublisher[]{
                    this.minXBinding,
                    this.minYBinding,
                    this.maxXBinding,
                    this.maxYBinding,
                    this.minWidthBinding,
                    this.minHeightBinding,
                    this.maxWidthBinding,
                    this.maxHeightBinding
        }).removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                S.println("8 MINMAX: " + S.arrayToString((Object[])value));
                relayout();
            }
        });

        Publisher.combineLatest(new IPublisher[]{
                    this.color,
                    this.backgroundColor,
                    this.borderColor,
                    this.strokeStyle,
                    this.borderWidth
        }).removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                S.println("2 SHADOWS" + S.arrayToString((Object[])value));
                repaint();
            }
        });
    }

    Int xBinding() {
        return xBinding;
    }

    Int yBinding() {
        return yBinding;
    }

    Int widthBinding() {
        return widthBinding;
    }

    Int heightBinding() {
        return heightBinding;
    }

    public CGDrawable width(Int width) {
        S.println(this + " KEK WILL SET WIDTH BINDING " + width);

        int value = width.getInt();
        this.initMinMaxWidth(value);

        width.route(this.widthBinding);
        
        return this;
    }

    private void initMinMaxWidth(int value) {
        this.isWidthSetByUser = true;

        if (minWidthBinding.getInt() == CG.NULL && value != CG.NULL) {
            minWidth(value);
        }
        if (maxWidthBinding.getInt() == CG.NULL && value != CG.NULL) {
            maxWidth(value);
        }
    }

    public CGDrawable width(int width) {
        S.println(this + " KEK WILL SET WIDTH " + width);
        this.initMinMaxWidth(width);
        this.widthBinding.setInt(width);
        return this;
    }

    public CGDrawable height(Int height) {
        int value = height.getInt();
        this.initMinMaxHeight(value);
        height.route(this.heightBinding);
        
        return this;
    }

    private void initMinMaxHeight(int value) {
        this.isHeightSetByUser = true;
        
        if (minHeightBinding.getInt() == CG.NULL && value != CG.NULL) {
            S.println("WILL INIT MIN HEIGHT " + value);
            minHeight(value);
        }
        if (maxHeightBinding.getInt() == CG.NULL && value != CG.NULL) {
            S.println("WILL INIT MAX HEIGHT " + value);
            maxHeight(value);
        }
    }

    public CGDrawable height(int height) {
        S.println(this + " KEK WILL SET HEIGHT " + height);
        this.initMinMaxHeight(height);
        this.heightBinding.setInt(height);
        return this;
    }

    public int width() {
        int value = widthBinding.getInt();
        return value == CG.NULL ? 0 : value;
    }

    public int height() {
        int value = heightBinding.getInt();
        return value == CG.NULL ? 0 : value;
    }

    public int x() {
        int value = xBinding.getInt();
        return value == CG.NULL ? 0 : value;
    }

    public int y() {
        int value = yBinding.getInt();
        return value == CG.NULL ? 0 : value;
    }

    public CGDrawable x(Int x) {
        int value = x.getInt();
        this.initMinMaxX(value);
        x.route(this.xBinding);
        return this;
    }

    private void initMinMaxX(int value) {
        this.isXSetByUser = true;

        if (minXBinding.getInt() == CG.NULL && value != CG.NULL) {
            minX(value);
        }
        if (maxXBinding.getInt() == CG.NULL && value != CG.NULL) {
            maxX(value);
        }
    }

    public CGDrawable x(int x) {
        this.initMinMaxX(x);
        this.xBinding.setInt(x);
        return this;
    }

    public CGDrawable y(Int y) {
        int value = y.getInt();
        this.initMinMaxY(value);
        y.route(this.yBinding);
        return this;
    }

    private void initMinMaxY(int value) {
        this.isYSetByUser = true;

        if (minYBinding.getInt()== CG.NULL && value != CG.NULL) {
            minY(value);
        }
        if (maxYBinding.getInt() == CG.NULL && value != CG.NULL) {
            maxY(value);
        }
    }

    public CGDrawable y(int y) {
        this.initMinMaxY(y);
        this.yBinding.setInt(y);
        return this;
    }

    protected void drawShadow(Graphics g, CGFrame frame) {
        int shadowColor = this.shadowColor();
        int cornerRadius = cornerRadius() * 2;
        if (shadowColor != CG.NULL) {
            g.setColor(shadowColor);
            g.fillRoundRect(
                    frame.x + shadowOffset().x,
                    frame.y + shadowOffset().y,
                    frame.width,
                    frame.height,
                    cornerRadius,
                    cornerRadius);
        }
    }

    protected void drawBackground(Graphics g, CGFrame frame) {
        S.println(this + " WILL DRAW BACKGROUND IN FRAME " + frame);
        
        int backgroundColor = this.backgroundColor();
        int cornerRadius = cornerRadius() * 2;

//        int borderColor = borderColor();
//
//        if (backgroundColor != CG.NULL && borderColor != CG.NULL && cornerRadius > 1) {
//            g.setColor(backgroundColor);
//            g.fillRoundRect(
//                    frame.x,
//                    frame.y,
//                    frame.width,
//                    frame.height,
//                    cornerRadius,
//                    cornerRadius);
//
//            return;
//        }

        if (backgroundColor != CG.NULL) {
            g.setColor(backgroundColor);
            g.fillRoundRect(
                    frame.x,
                    frame.y,
                    frame.width,
                    frame.height,
                    cornerRadius,
                    cornerRadius);
        }
    }

    protected abstract void drawContent(Graphics g, CGFrame frame);

    protected void drawBorder(Graphics g, CGFrame frame) {
        int borderWidth = this.borderWidth();
        int borderColor = this.borderColor();
        int cornerRadius = cornerRadius() * 2;

        if (borderColor != CG.NULL && borderWidth > 0) {
            g.setStrokeStyle(this.strokeStyle());
            g.setColor(borderColor);

            for (int i = 0; i < borderWidth; i ++) {
                    g.drawRoundRect(
                    frame.x + i,
                    frame.y + i,
                    frame.width - 2*i,
                    frame.height - 2*i,
                    cornerRadius - 2 * i,
                    cornerRadius - 2 * i);
//
//                    g.drawRoundRect(
//                    frame.x,
//                    frame.y + i,
//                    frame.width,
//                    frame.height - 2*i,
//                    cornerRadius - i,
//                    cornerRadius - i);
                }
        }
    }

    public void draw(Graphics g) {
        CGFrame frame = frame();
        if (frame == null) {
            return;
        }

        frame.x += offset().x;
        frame.y += offset().y;

        this.drawShadow(g, frame);
        this.drawBackground(g, frame);

        CGFrame oldClip = new CGFrame(g.getClipX(), g.getClipY(), g.getClipWidth(), g.getClipHeight());

        CGFrame contentFrame = frame.insetting(borderWidth(), borderWidth());
        g.clipRect(contentFrame.x, contentFrame.y, contentFrame.width, contentFrame.height);
        this.drawContent(g, contentFrame);
        g.setClip(oldClip.x, oldClip.y, oldClip.width, oldClip.height);
        
        this.drawBorder(g, frame);
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
        
        this.x(frame.x);
        this.y(frame.y);
        this.width(frame.width);
        this.height(frame.height);
        
        return this;
    }

    public int backgroundColor() {
        return backgroundColor.getInt();
    }

    public CGDrawable backgroundColor(int colorHex) {
        this.backgroundColor.setInt(colorHex);
        return this;
    }

    public CGDrawable backgroundColor(Int backgroundColor) {
        backgroundColor.route(this.backgroundColor);
        return this;
    }

    public int color() {
        return color.getInt();
    }

    public CGDrawable color(int colorHex) {
        this.color.setInt(colorHex);
        return this;
    }

    public CGDrawable color(Int color) {
        color.route(this.color);
        return this;
    }

    public int shadowColor() {
        return this.shadowColor.getInt();
    }

    public CGDrawable shadowColor(int shadowColorHex) {
        this.shadowColor.setInt(shadowColorHex);
        return this;
    }

    public CGDrawable shadowColor(Int shadowColor) {
        shadowColor.route(this.shadowColor);
        return this;
    }

    public CGPoint shadowOffset() {
        return this.shadowOffset.getCGPoint();
    }

    public CGDrawable shadowOffset(int x, int y) {
        this.shadowOffset.sendValue(new CGPoint(x, y));
        return this;
    }
    
    public CGDrawable shadowOffset(Point shadowOffset) {
        shadowOffset.route(this.shadowOffset);
        return this;
    }

    public void relayout() {
        S.println("relayout() CANVAS " + canvas);
        if (this.canvas() != null) {
            S.println("WILL RELAYOUT!");
            this.updateIntrinsicContentSize();
            this.canvas.setNeedsRepaint();
        }
    }
    
    public void repaint() {
        if (this.canvas() == null) {
            return;
        }
        
        this.repaint(frame());
    }
    
    public void repaint(CGFrame frame) {
        if (this.canvas() == null) {
            return;
        }
        
        this.canvas.repaint(frame.x, frame.y, frame.width, frame.height);
    }
    
    public CGFrame intrinsicAwareFrame() {
        CGFrame frame = this.frame();
        S.println(this + " WILL SAY ITS INTRAWARE FRAME!");

        CGSize size = this.intrinsicContentSize();
        CGInsets insets = this.contentInset();

        int width = size.width;// + insets.left + insets.right;
        int height = size.height;// + insets.top + insets.bottom;

        width = Math.max(width, Math.max(frame.width, minWidth()));
        height = Math.max(height, Math.max(frame.height, minHeight()));

        frame.width = width;
        frame.height = height;

        S.println(this + " INTRAWARE FRAME IS " + frame);

        return frame;
    }

    public CGCanvas canvas() {
        return canvas;
    }

    public int borderColor() {
        return borderColor.getInt();
    }

    public CGDrawable minX(Int minX) {
        minX.route(this.minXBinding);
        return this;
    }

    public CGDrawable minX(int minX) {
        this.minXBinding.setInt(minX);
        return this;
    }

    public CGDrawable minY(Int minY) {
        minY.route(this.minYBinding);
        return this;
    }

    public CGDrawable minY(int minY) {
        this.minYBinding.setInt(minY);
        return this;
    }

    public CGDrawable maxX(Int maxX) {
        maxX.route(this.maxXBinding);
        return this;
    }

    public CGDrawable maxX(int maxX) {
        this.maxXBinding.setInt(maxX);
        return this;
    }

    public CGDrawable maxY(Int maxY) {
        maxY.route(this.maxYBinding);
        return this;
    }

    public CGDrawable maxY(int maxY) {
        this.maxYBinding.setInt(maxY);
        return this;
    }

    public CGDrawable minWidth(Int minWidth) {
        minWidth.route(this.minWidthBinding);
        return this;
    }

    public CGDrawable minWidth(int minWidth) {
        this.minWidthBinding.setInt(minWidth);
        return this;
    }

    public CGDrawable minHeight(Int minHeight) {
        minHeight.route(this.minHeightBinding);
        return this;
    }

    public CGDrawable minHeight(int minHeight) {
        this.minHeightBinding.setInt(minHeight);
        return this;
    }

    public CGDrawable maxWidth(Int maxWidth) {
        maxWidth.route(this.maxWidthBinding);
        return this;
    }

    public CGDrawable maxWidth(int maxWidth) {
        this.maxWidthBinding.setInt(maxWidth);
        return this;
    }

    public CGDrawable maxHeight(Int maxHeight) {
        maxHeight.route(this.maxHeightBinding);
        return this;
    }

    public CGDrawable maxHeight(int maxHeight) {
        this.maxHeightBinding.setInt(maxHeight);
        return this;
    }

    public int minX() {
        int value = minXBinding.getInt();
        return value == CG.NULL ? 0 : value;
    }

    public int minY() {
        int value = minYBinding.getInt();
        return value == CG.NULL ? 0 : value;
    }

    public int maxX() {
        int value = maxXBinding.getInt();
        return value == CG.NULL ? Integer.MAX_VALUE : value;
    }

    public int maxY() {
        int value = maxYBinding.getInt();
        return value == CG.NULL ? Integer.MAX_VALUE : value;
    }

    public int minWidth() {
        int value = minWidthBinding.getInt();
        return value == CG.NULL ? 0 : value;
    }

    public int minHeight() {
        int value = minHeightBinding.getInt();
        return value == CG.NULL ? 0 : value;
    }

    public int maxWidth() {
        int value = maxWidthBinding.getInt();
        return value == CG.NULL ? Integer.MAX_VALUE : value;
    }

    public int maxHeight() {
        int value = maxHeightBinding.getInt();
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
        this.isVisible.setBool(isVisible);
        return this;
    }

    public CGDrawable isVisible(Bool isVisible) {
        isVisible.route(this.isVisible);
        return this;
    }

    public boolean isVisible() {
        return this.isVisible.getBoolean();
    }

    public CGDrawable sizeToFit() {
        return this;
    }

    public CGSize intrinsicContentSize() {
        return this.intrinsicContentSizeBinding.getCGSize(); //SIC!
    }

    protected void updateIntrinsicContentSize() {
        //INTENTIONALLY DO NOTHING
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

    public CGDrawable offset(Point offset) {
        offset.route(this.offsetBinding);
        return this;
    }

    public CGDrawable offset(int x, int y) {
        this.offsetBinding.sendValue(new CGPoint(x, y));
        return this;
    }

    public CGDrawable contentInset(Insets inset) {
        inset.route(this.contentInsetBinding);
        return this;
    }

    public CGDrawable contentInset(int top, int left, int bottom, int right) {
        CGInsets insets = new CGInsets(top, left, bottom, right);
        this.contentInsetBinding.sendValue(insets);
        return this;
    }

    public CGPoint offset() {
        return this.offsetBinding.getCGPoint();
    }

    public CGInsets contentInset() {
        return this.contentInsetBinding.getCGInsets();
    }

    public CGDrawable stroke(int strokeStyle) {
        return this.stroke(strokeStyle);
    }

    public CGDrawable stroke(Int strokeStyle) {
        strokeStyle.route(this.strokeStyle);
        return this;
    }

    protected int strokeStyle() {
        return strokeStyle.getInt();
    }

    public CGDrawable cornerRadius(int cornerRadius) {
        this.cornerRadiusBinding.setInt(cornerRadius);
        return this;
    }

    public CGDrawable cornerRadius(Int cornerRadiusBinding) {
        cornerRadiusBinding.route(this.cornerRadiusBinding);
        return this;
    }

    public int cornerRadius() {
        return this.cornerRadiusBinding.getInt();
    }

    public CGDrawable borderColor(int borderColorHex) {
        this.borderColor.setInt(borderColorHex);
        return this;
    }

    public CGDrawable borderColor(Int borderColorHex) {
        borderColorHex.route(this.borderColor);
        return this;
    }

    public int[] flexibility() {
        return new int[]{ this.flexibilityWidth, this.flexibilityHeight };
    }

    public CGDrawable flexibility(int[] flexibility) {
        this.flexibilityWidth(flexibility[0]);
        this.flexibilityHeight(flexibility[1]);
        return this;
    }

    public int flexibilityWidth() {
        return this.flexibilityWidth;
    }

    public CGDrawable flexibilityWidth(int flexibility) {
        this.flexibilityWidth = flexibility;
        this.relayout();
        return this;
    }

    public int flexibilityHeight() {
        return this.flexibilityHeight;
    }

    public CGDrawable flexibilityHeight(int flexibility) {
        this.flexibilityHeight = flexibility;
        this.relayout();
        return this;
    }

    public int borderWidth() {
        return this.borderWidth.getInt();
    }
    
    public CGDrawable borderWidth(int borderWidth) {
        this.borderWidth.setInt(borderWidth);
        return this;
    }

    public CGDrawable borderWidth(Int borderWidth) {
        this.borderWidth.getInt();
        return this;
    }


    public CGDrawable animate(CGAnimation animation) {
        animation.setDrawable(this);
        CGDisplayLink.addAnimation(animation);
        return this;
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
}
