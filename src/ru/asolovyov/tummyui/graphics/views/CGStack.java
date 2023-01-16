/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.bindings.Size;
import java.lang.Math.*;
import ru.asolovyov.combime.operators.mapping.Map;
import ru.asolovyov.threading.DispatchQueue;
import ru.asolovyov.tummyui.bindings.Point;
import ru.asolovyov.tummyui.data.List;
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.CGInsets;
import ru.asolovyov.tummyui.graphics.CGPoint;
import ru.asolovyov.tummyui.graphics.CGSize;

/**
 *
 * @author Администратор
 */
public class CGStack extends CGSomeDrawable {

    public static abstract class DrawableFactory {

        public abstract CGDrawable itemFor(Object viewModel);
    }

    {
        this.drawables = new Arr(new CGDrawable[]{});
    }
    public final static int AXIS_HORIZONTAL = 0;
    public final static int AXIS_VERTICAL = 1;
    public final static int AXIS_Z = 2;
    private int nextLeft = 0;
    private int nextTop = 0;
    //TODO сделать биндинги как в CGSomeDrawable
    protected Arr drawables;
    protected Int alignment = new Int(CG.LEFT);
    protected Int axis = new Int(AXIS_HORIZONTAL);
    protected Int maxContentWidthBinding = new Int(Integer.MAX_VALUE);
    protected Int maxContentHeightBinding = new Int(Integer.MAX_VALUE);

    public CGStack maxContentWidth(int width) {
        this.maxContentWidthBinding.setInt(width);
        return this;
    }

    public CGStack maxContentHeight(int height) {
        this.maxContentHeightBinding.setInt(height);
        return this;
    }

    // TODO хуйня какая-то эти биндинги. НЕ НАДО ЗАМЕНЯТЬ ВНУТРЕННИЙ СУБСКРИПШЕН!
    // Надо наверно иметь ОДИН ПРОТЕКТЕД БИНДИНГ и ОДИН СУБСКРИПШЕН.
    // Когд получаем новый биндинг, то ТЕРМИНИРУЕМ СУБСКРИПШЕН, НАЧИНАЕМ СЛИВАТЬ ДАННЫЕ ИЗ НОВОГО БИНДИНГА В ТЕКУЩИЙ
    // И СОХРАНЯЕМ СУБСКРИПШЕН ЭТОГО СЛИВАНИЯ.
    public CGStack maxContentWidth(Int width) {
        this.maxContentWidthBinding = width;
        this.maxContentWidthBinding.sink(new Sink() {

            protected void onValue(Object value) {
                updateIntrinsicContentSize();
                needsRelayout();
            }
        });
        return this;
    }

    public CGStack maxContentHeight(Int height) {
        this.maxContentHeightBinding = height;
        this.maxContentHeightBinding.sink(new Sink() {

            protected void onValue(Object value) {
                updateIntrinsicContentSize();
                needsRelayout();
            }
        });
        return this;
    }

    public int spacing() {
        return this.spacing.getInt();
    }

    public CGStack spacing(Int spacing) {
        this.spacing = spacing;
        this.spacing.sink(new Sink() {

            protected void onValue(Object value) {
                updateIntrinsicContentSize();
                needsRelayout();
            }
        });
        return this;
    }

    public CGStack spacing(int spacing) {
        this.spacing.setInt(spacing);
        return this;
    }
    protected Int spacing = new Int(0);
    protected Arr models = new Arr(new Object[]{});
    protected DrawableFactory factory;
    protected Size contentSize = new Size(0, 0);
    private List repeatedKeys = new List();
    private DispatchQueue keyRepeatQueue = new DispatchQueue(120);

    public Size contentSize() {
        return contentSize;
    }

    public CGStack(Int axis, Int alignment, Arr models, DrawableFactory factory) {
        this(axis, alignment, new Arr(new CGDrawable[]{}));
        this.factory = factory;
        this.models = models;

        S.debugln(" CGStack(Int axis, Int alignment, Arr models, DrawableFactory factory) " + drawables);

        this.models.to(
                new Map() {

                    public Object mapValue(Object value) {
                        Object[] models = (Object[]) value;
                        CGDrawable[] drawables = new CGDrawable[models.length];
                        S.debugln("KEK CGDrawable[] drawables = new CGDrawable[models.length]; " + drawables);
                        for (int i = 0; i < models.length; i++) {
                            Object model = models[i];

                            CGDrawable drawable = CGStack.this.factory.itemFor(model);
                            drawables[i] = drawable;
                        }
                        return drawables;
                    }
                }).route(drawables);
    }

    public CGStack(int axis, Object[] models, DrawableFactory factory) {
        this(new Int(axis), new Int(CG.CENTER), new Arr(models), factory);
    }

    public CGStack(Int axis, Arr models, DrawableFactory factory) {
        this(axis, new Int(CG.CENTER), models, factory);
    }

    public CGStack(Int axis, Arr drawables) {
        this(axis, new Int(CG.CENTER), drawables);
    }

    public CGStack(Int axis, Int alignment, Arr drawables) {
        super();
        S.debugln("KEK CGSTACK WILL SET DRAWABLES: " + drawables);
        this.axis = axis;
        this.alignment = alignment;
        this.drawables = drawables;

        this.axis.sink(new Sink() {

            protected void onValue(Object value) {
                updateIntrinsicContentSize();
                needsRelayout(frame());
            }
        });

        this.alignment.sink(new Sink() {

            protected void onValue(Object value) {
                needsRelayout();
            }
        });

        this.drawables.sink(new Sink() {

            protected void onValue(Object value) {
                updateIntrinsicContentSize();
                needsRelayout(frame());
            }
        });

        //TODO Trigger subscription
        this.spacing = spacing;
        this.maxContentWidthBinding = maxContentWidthBinding;
        this.maxContentHeightBinding = maxContentHeightBinding;
    }

    public Int axis() {
        return axis;
    }

    public void draw(Graphics g) {
        super.draw(g);

        S.debugln(this + "CGSTACK DRAW!");

        if (this.axis.getInt() == AXIS_HORIZONTAL) {
            this.hDraw(g);
        } else if (this.axis.getInt() == AXIS_VERTICAL) {
            this.vDraw(g);
        } else if (this.axis.getInt() == AXIS_Z) {
            this.zDraw(g);
        }
    }

    private void pushFrameToChildren() {
        S.debugln("KEK DRAWABLES " + this.drawables.getArray());
        CGDrawable[] drawables = (CGDrawable[]) this.drawables.getArray();
        for (int i = 0; i < drawables.length; i++) {
            CGDrawable drawable = drawables[i];
            if (drawable.geometryReader() != null) {
                drawable.geometryReader().read(drawable, frame());
            }
        }
    }

    public void needsRelayout(CGFrame frame) {
        super.needsRelayout(frame);
        if (this.canvas() != null) {
            this.pushFrameToChildren();
        }
    }

    private void hDraw(Graphics g) {
        final Graphics graphics = g;

        final CGFrame thisFrame = frame();
        CGInsets contentInsets = contentInset();

        this.nextLeft = thisFrame.x + contentInsets.left;

        int alignmentInt = this.alignment.getInt();
        int contentWidth = this.contentSize().getCGSize().width;

        if (CG.isBitSet(alignmentInt, CG.HCENTER)) {
            this.nextLeft = thisFrame.x + (thisFrame.width - contentWidth) / 2 + contentInsets.deltaX();
        }
        if (CG.isBitSet(alignmentInt, CG.LEFT)) {
            this.nextLeft = thisFrame.x + contentInsets.deltaX();
        }
        if (CG.isBitSet(alignmentInt, CG.RIGHT)) {
            this.nextLeft = thisFrame.x + thisFrame.width - contentWidth + contentInsets.deltaX();
        }

        final boolean isVCenter = CG.isBitSet(alignmentInt, CG.VCENTER);
        final boolean isTop = CG.isBitSet(alignmentInt, CG.TOP);
        final boolean isBottom = CG.isBitSet(alignmentInt, CG.BOTTOM);

        this.drawables.forEach(new Arr.Enumerator() {

            public void onElement(Object element) {
                CGDrawable child = (CGDrawable) element;
                CGFrame childFrame = child.intrinsicAwareFrame();

                S.debugln("WILL DRAW " + child + " " + childFrame);
                CGInsets contentInsets = contentInset();

                childFrame.x = nextLeft;

                if (isVCenter) {
                    childFrame.y = thisFrame.y + (thisFrame.height - childFrame.height) / 2 + contentInsets.deltaY();
                }
                if (isTop) {
                    childFrame.y = thisFrame.y + contentInsets.deltaY();
                }
                if (isBottom) {
                    childFrame.y = thisFrame.y + thisFrame.height - childFrame.height + contentInsets.deltaY();
                }

                childFrame.x -= contentOffset().x;
                childFrame.y -= contentOffset().y;

                child.origin(childFrame.x, childFrame.y);

                S.debugln("HSTACK Will draw " + child + " " + childFrame.x + ", " + childFrame.y + "; " + childFrame.width + ", " + childFrame.height);

                child.draw(graphics);
                nextLeft += childFrame.width + spacing();
            }
        });
    }

    private void vDraw(Graphics g) {
        final Graphics graphics = g;

        final CGFrame thisFrame = frame().copy();
        final CGInsets contentInsets = contentInset();

        this.nextTop = thisFrame.y + contentInsets.top;

        int alignmentInt = this.alignment.getInt();
        int contentHeight = this.contentSize.getCGSize().height;

        if (CG.isBitSet(alignmentInt, CG.VCENTER)) {
            this.nextTop = thisFrame.y + (thisFrame.height - contentHeight) / 2 + contentInsets.deltaY();
        }
        if (CG.isBitSet(alignmentInt, CG.TOP)) {
            this.nextTop = thisFrame.y + contentInsets.deltaY();
        }
        if (CG.isBitSet(alignmentInt, CG.BOTTOM)) {
            this.nextTop = thisFrame.y + thisFrame.height - contentHeight + contentInsets.deltaY();
        }

        final boolean isHCenter = CG.isBitSet(alignmentInt, CG.HCENTER);
        final boolean isLeft = CG.isBitSet(alignmentInt, CG.LEFT);
        final boolean isRight = CG.isBitSet(alignmentInt, CG.RIGHT);

        this.drawables.forEach(new Arr.Enumerator() {

            public void onElement(Object element) {
                CGDrawable child = (CGDrawable) element;
                CGFrame childFrame = child.intrinsicAwareFrame();

                if (isHCenter) {
                    childFrame.x = thisFrame.x + (thisFrame.width - childFrame.width) / 2 + contentInsets.deltaX();
                }
                if (isLeft) {
                    childFrame.x = thisFrame.x + contentInsets.deltaX();
                }
                if (isRight) {
                    childFrame.x = thisFrame.x + thisFrame.width - childFrame.width + contentInsets.deltaX();
                }

                childFrame.y = nextTop;

                childFrame.x -= contentOffset().x;
                childFrame.y -= contentOffset().y;

                S.debugln("VSTACK Will draw " + child + " " + childFrame.x + ", " + childFrame.y + "; " + childFrame.width + ", " + childFrame.height);

                child.origin(childFrame.x, childFrame.y);

                child.draw(graphics);

                nextTop += childFrame.height + spacing();
            }
        });
    }

    private void zDraw(Graphics g) {
        final Graphics graphics = g;

        final CGFrame thisFrame = frame();
        final CGInsets contentInsets = contentInset();

        for (int i = 0; i < this.drawables.getArray().length; i++) {
            CGDrawable object = (CGDrawable) this.drawables.getArray()[i];
            CGFrame frame = object.intrinsicAwareFrame();

            frame.width = thisFrame.width;
            frame.height = thisFrame.height;
        }

        int alignmentInt = this.alignment.getInt();

        final boolean isVCenter = CG.isBitSet(alignmentInt, CG.VCENTER);
        final boolean isTop = CG.isBitSet(alignmentInt, CG.TOP);
        final boolean isBottom = CG.isBitSet(alignmentInt, CG.BOTTOM);

        final boolean isHCenter = CG.isBitSet(alignmentInt, CG.HCENTER);
        final boolean isLeft = CG.isBitSet(alignmentInt, CG.LEFT);
        final boolean isRight = CG.isBitSet(alignmentInt, CG.RIGHT);

        this.drawables.forEach(new Arr.Enumerator() {

            public void onElement(Object element) {
                CGDrawable drawable = (CGDrawable) element;
                CGFrame frame = drawable.intrinsicAwareFrame();

                if (isVCenter) {
                    frame.y = (thisFrame.height - frame.height) / 2 + contentInsets.deltaY();
                }
                if (isTop) {
                    frame.y = 0 + contentInsets.deltaY();
                }
                if (isBottom) {
                    frame.y = thisFrame.height - frame.height + contentInsets.deltaY();
                }

                if (isHCenter) {
                    frame.x = (thisFrame.width - frame.width) / 2 + contentInsets.deltaX();
                }
                if (isLeft) {
                    frame.x = contentInsets.deltaX();
                }
                if (isRight) {
                    frame.x = thisFrame.width - frame.width + contentInsets.deltaX();
                }

                frame.x += drawable.origin().x;
                frame.y += drawable.origin().y;

                drawable.origin(frame.x, frame.y);

                drawable.draw(graphics);
            }
        });
    }

    public CGDrawable canvas(CGCanvas canvas) {
        super.canvas(canvas);

        this.subscribeToKeyPressed();
        this.subscribeToKeyReleased();

        this.drawables.forEach(new Arr.Enumerator() {

            public void onElement(Object element) {
                CGDrawable drawable = (CGDrawable) element;
                drawable.canvas(CGStack.this.canvas());
            }
        });
        return this;
    }

    private void subscribeToKeyPressed() {
        CGCanvas canvas = this.canvas();
        if (canvas == null) {
            return;
        }
        final Int keyPublisher = canvas.getKeyPressed();
        keyPublisher.sink(new Sink() {

            protected void onValue(Object value) {
                S.debugln("PRESS DETECTED: " + value);
                super.onValue(value);

                int keyCode = keyPublisher.getInt();
                moveContentByKeyPress(keyCode);
                final Integer keyCodeInteger = new Integer(keyCode);
                repeatedKeys.removeElement(keyCodeInteger);
                repeatedKeys.addElement(keyCodeInteger);

                keyRepeatQueue.async(2 * CG.FRAME_MILLIS, new Runnable() {

                    public void run() {
                        scheduleKeyRepeatedHandling(keyCodeInteger);
                    }
                });
            }
        });
    }

    private void moveContentByKeyPress(int keyCode) {
        CGInsets contentInset = this.contentInset();

        CGPoint contentOffset = this.contentOffset();
        CGSize contentSize = contentSize().getCGSize();

        CGFrame thisFrame = frame();

        if (contentSize.height > thisFrame.height) {
            S.debugln("contentSize.height > thisFrame.height");
            int extent = contentSize.height - thisFrame.height;

            if (keyCode == CG.KEY_UP) {//t
                S.debugln("keyCode == Canvas.UP");
                contentOffset.y = Math.max(
                        contentOffset.y - 5,
                        -(extent - contentInset.top));
            } else if (keyCode == CG.KEY_DOWN) {//b
                S.debugln("keyCode == Canvas.DOWN");
                contentOffset.y = Math.min(
                        contentOffset.y + 5,
                        extent + contentInset.bottom);
            }
        }

        if (contentSize.width > thisFrame.width) {
            S.debugln("contentSize.width > thisFrame.width");
            int extent = contentSize.width - thisFrame.width;
            if (keyCode == CG.KEY_LEFT) {//l
                S.debugln("Canvas.LEFT");
                contentOffset.x = Math.max(
                        contentOffset.x - 5,
                        -(extent + contentInset.left));
            } else if (keyCode == CG.KEY_RIGHT) { //r
                S.debugln("keyCode == Canvas.RIGHT");
                contentOffset.x = Math.min(
                        contentOffset.x + 5,
                        extent + contentInset.right);
            }
        }

        S.debugln("\nCONTENT OFFSET NOW: " + contentOffset.x + "; " + contentOffset.y + "\n");

        contentOffsetBinding.sendValue(new Point(contentOffset));
    }

    private void scheduleKeyRepeatedHandling(final Integer keyCode) {
        this.keyRepeatQueue.async(CG.FRAME_MILLIS, new Runnable() {

            public void run() {
                if (repeatedKeys.contains(keyCode)) {
                    moveContentByKeyPress(keyCode.intValue());
                    scheduleKeyRepeatedHandling(keyCode);
                }
            }
        });
    }

    private void subscribeToKeyReleased() {
        CGCanvas canvas = this.canvas();
        if (canvas == null) {
            return;
        }
        final Int keyPublisher = canvas.getKeyReleased();
        keyPublisher.sink(new Sink() {

            protected void onValue(Object value) {
                S.debugln("PRESS RELEASED: " + value);
                super.onValue(value);

                Integer keyCode = new Integer(keyPublisher.getInt());
                repeatedKeys.removeElement(keyCode);
            }
        });
    }

    protected CGSize updateContentSizeAndChildrenDimensions() {
        int contentWidth = 0;
        int contentHeight = 0;

        int maxWidth = 0;
        int maxHeight = 0;

        int axis = this.axis.getInt();

        Object[] objDrawables = this.drawables.getArray();
        CGDrawable[] drawables = new CGDrawable[objDrawables.length];
        S.debugln("KEK CGSize updateContentSizeAndChildrenDimensions() " + drawables);

        S.debugln("LETS COUNT CONTENT SIZE OF " + drawables.length + " SUBVIEWS!");
        for (int i = 0; i < drawables.length; i++) {
            CGDrawable drawable = (CGDrawable) objDrawables[i];
            drawables[i] = drawable;

            S.debugln(i + "-th subview is " + drawable);

            int width = drawable.intrinsicAwareFrame().width;
            int height = drawable.intrinsicAwareFrame().height;

            maxWidth = Math.max(maxWidth, width);
            maxHeight = Math.max(maxHeight, height);

            if (axis == AXIS_HORIZONTAL) {
                contentWidth += width;
                contentHeight = maxHeight;
            } else if (axis == AXIS_VERTICAL) {
                contentHeight += height;
                contentWidth = maxWidth;
            } else {
                contentWidth = maxWidth;
                contentHeight = maxHeight;
            }
        }

        contentWidth += this.contentInset().horizontal();
        contentHeight += this.contentInset().vertical();

        int spaces = (drawables.length - 1) * this.spacing();
        if (axis == AXIS_HORIZONTAL) {
            contentWidth += spaces;
        } else if (axis == AXIS_VERTICAL) {
            contentHeight += spaces;
        }

        S.debugln("1 BEFORE MASSIVE CALCULATIONS CONTENT SIZE IS: " + contentWidth + "x" + contentHeight);

        contentWidth = Math.min(contentWidth, this.maxContentWidthBinding.getInt());
        contentHeight = Math.min(contentHeight, this.maxContentHeightBinding.getInt());

        S.debugln("2 BEFORE MASSIVE CALCULATIONS CONTENT SIZE IS: " + contentWidth + "x" + contentHeight);

        CGSize size = this.adjustFrameToContentSize(contentWidth, contentHeight);
        S.debugln("CGSize size = this.adjustFrameToContentSize(contentWidth, contentHeight);");
        S.debugln(size);

        int delta = size.width - contentWidth;
        S.debugln("DELTA WIDTH " + delta);

        int remainingDelta = this.adjustChildrenDimensions(drawables, false, delta);

        delta += (delta > 0) ? -remainingDelta : +remainingDelta;
        contentWidth += delta;
        S.debugln("DELTA WIDTH " + delta);

        delta = size.height - contentHeight;
        S.debugln("DELTA HEIGHT " + delta);

        remainingDelta = this.adjustChildrenDimensions(drawables, true, delta);

        delta += (delta > 0) ? -remainingDelta : +remainingDelta;
        S.debugln("DELTA HEIGHT " + delta);
        contentHeight += delta;

        S.debugln("AFTER MASSIVE CALCULATIONS CONTENT SIZE IS: " + contentWidth + "x" + contentHeight);

        CGSize contentSize = new CGSize(contentWidth, contentHeight);
        this.contentSize.setCGSize(contentSize);

        CGFrame frame = this.frame();
        frame.width = size.width;
        frame.height = size.height;
        S.debugln("AFTER MASSIVE CALCULATIONS STACK SIZE IS: " + size);

        if (size.width != this.width()) {
            this.widthBinding.sendValue(new Int(size.width));
        }

        if (size.height != this.height()) {
            this.heightBinding.sendValue(new Int(size.height));
        }

        return contentSize;
    }

    private CGSize adjustFrameToContentSize(int contentWidth, int contentHeight) {
        CGSize size = this.frame().getCGSize();
        S.debugln("adjustFrameToContentSize " + size);

        if (this.height() < contentHeight && this.hasGrowableHeight()) {
            S.debugln("1 adjustFrameToContentSize");
            int height = Math.min(contentHeight, this.maxHeight());
            size.height = height;
        } else if (contentHeight > this.height() && this.hasShrinkableHeight()) {
            S.debugln("2 adjustFrameToContentSize");
            int height = Math.max(contentHeight, this.minHeight());
            size.height = height;
        }

        if (contentWidth > this.width() && this.hasGrowableWidth()) {
            S.debugln("3 adjustFrameToContentSize");
            int width = Math.min(contentWidth, this.maxWidth());
            size.width = width;
        } else if (contentWidth > this.width() && this.hasShrinkableWidth()) {
            S.debugln("4 adjustFrameToContentSize");
            int width = Math.max(contentWidth, this.minWidth());
            size.width = width;
        }

        return size;
    }

    // TODO ВОЗМОЖНО вот тут надо еще и ориджины/инсеты двигать
    private int adjustChildrenDimensions(CGDrawable[] views, final boolean isHeight, final int delta) {
        if (delta == 0) {
            return delta;
        }

        final boolean isExpanding = delta > 0;
        S.debugln("IS EXPANDING " + isExpanding);
        S.debugln("IS HEIGT " + isHeight);

        int remainingDelta = Math.abs(delta);

        Object[] adjustables = S.filter(views, new S.Filter() {

            public boolean filter(Object object) {
                CGDrawable view = (CGDrawable) object;
                if (isExpanding) {
                    return isHeight
                            ? view.maxHeight() > view.height()
                            : view.maxWidth() > view.width();
                }
                return isHeight
                        ? view.height() > view.minHeight()
                        : view.width() > view.minWidth();
            }
        });

        S.debugln(adjustables.length + " ADJUSTABLES: " + adjustables);

        boolean isAxisDimension = this.axis.getInt() == (isHeight ? AXIS_VERTICAL : AXIS_HORIZONTAL);

        if (false == isAxisDimension) {
            S.debugln("if (false == isAxisDimension) {");
            int maxDelta = 0;
            for (int i = 0; i < adjustables.length; i++) {
                CGSomeDrawable view = (CGSomeDrawable) adjustables[i];
                int viewDelta = 0;
                int value = isHeight ? view.height() : view.width();

                S.debugln("WILL ADJUST " + (isHeight ? "HEIGHT" : "WIDTH") + " OF " + view + " CUR VALUE " + value);

                if (isExpanding) {
                    viewDelta = isHeight
                            ? view.maxHeight() - view.height()
                            : view.maxWidth() - view.width();
                } else {
                    viewDelta = isHeight
                            ? view.height() - view.minHeight()
                            : view.width() - view.minWidth();
                }

                viewDelta = Math.min(viewDelta, remainingDelta);

                value += (isExpanding ? +viewDelta : -viewDelta);
                S.debugln("ADJUST VALUE BINDING WILL SET " + value);

                if (isHeight && value != view.height()) {
                    view.heightBinding.sendValue(new Int(value));
                } else if (value != view.width()) {
                    view.widthBinding.sendValue(new Int(value));
                }

                maxDelta = Math.max(maxDelta, Math.abs(viewDelta));
            }

            S.debugln("REMAINIG DELTA " + remainingDelta + "; MAX DELTA " + maxDelta);
            return remainingDelta - maxDelta;
        }

        S.debugln("IS isAxisDimension ");

        int adjustablesCount = adjustables.length;
        while (adjustablesCount > 0 && remainingDelta > 0) {
            int sliceToShare = remainingDelta / adjustables.length;
            for (int i = 0; i < adjustablesCount; i++) {
                CGSomeDrawable view = (CGSomeDrawable) adjustables[i];
                int extremeValue = isExpanding
                        ? isHeight ? view.maxHeight() : view.maxWidth()
                        : isHeight ? view.minHeight() : view.minWidth();
                int value = isHeight ? view.height() : view.width();

                S.debugln("WILL ADJUST " + (isHeight ? "HEIGHT" : "WIDTH") + " OF " + view + " CUR VALUE " + value);

                int spaceToAdjust = isExpanding
                        ? extremeValue - value
                        : value - extremeValue;

                spaceToAdjust = Math.min(spaceToAdjust, sliceToShare);
                value += isExpanding ? +spaceToAdjust : -spaceToAdjust;

                S.debugln("VALUE BINDING WILL SET " + value);

                if (isHeight && value != view.height()) {
                    view.heightBinding.sendValue(new Int(value));
                } else if (value != view.width()) {
                    view.widthBinding.sendValue(new Int(value));
                } else {
                    adjustablesCount--;
                }

                S.debugln("REMAINIG DELTA " + remainingDelta + "; spaceToAdjust " + spaceToAdjust);

                remainingDelta -= spaceToAdjust;
                if (value == extremeValue) {
                    adjustablesCount--;
                }
            }
        }

        S.debugln("REMAINIG DELTA " + remainingDelta);
        return remainingDelta;
    }

    protected void updateIntrinsicContentSize() {
        super.updateIntrinsicContentSize();
        S.debugln(this + " will updateContentSizeAndChildrenDimensions()");
        this.updateContentSizeAndChildrenDimensions();

        CGSize size = this.contentSize().getCGSize();
        S.debugln(this + " DID UPDATE SIZES, NOW HAS: " + size);

        this.intrinsicContentSizeBinding.sendValue(size);
    }
}
