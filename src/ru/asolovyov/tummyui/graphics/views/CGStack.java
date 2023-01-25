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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
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

    protected DrawableFactory factory;

    public final static int AXIS_HORIZONTAL = 0;
    public final static int AXIS_VERTICAL = 1;
    public final static int AXIS_Z = 2;
    
    //TODO сделать биндинги как в CGSomeDrawable
    protected Arr drawables = new Arr(new CGDrawable[]{});
    protected Int alignment = new Int(CG.CENTER);
    protected Int axis = new Int(AXIS_HORIZONTAL);
    protected Int maxContentWidthBinding = new Int(Integer.MAX_VALUE);
    protected Int maxContentHeightBinding = new Int(Integer.MAX_VALUE);
    
    protected Int spacing = new Int(0);
    protected Arr models = new Arr(new Object[]{});
    protected Size contentSize = new Size(0, 0);

    private List repeatedKeys = new List();
    private DispatchQueue keyRepeatQueue = new DispatchQueue(120);
    
    private int nextLeft = 0;
    private int nextTop = 0;

    private Hashtable drawablesGroupedByWidthFlexibility = new Hashtable();
    private Hashtable drawablesGroupedByHeightFlexibility = new Hashtable();

    public CGStack maxContentWidth(int width) {
        this.maxContentWidthBinding.setInt(width);
        return this;
    }

    public CGStack maxContentHeight(int height) {
        this.maxContentHeightBinding.setInt(height);
        return this;
    }
    
    public CGStack maxContentWidth(Int width) {
        this.maxContentWidthBinding = width;
        this.maxContentWidthBinding.sink(new Sink() {

            protected void onValue(Object value) {
                updateIntrinsicContentSize();
                relayout();
            }
        });
        return this;
    }

    public CGStack maxContentHeight(Int height) {
        this.maxContentHeightBinding = height;
        this.maxContentHeightBinding.sink(new Sink() {
            protected void onValue(Object value) {
                updateIntrinsicContentSize();
                relayout();
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
                relayout();
            }
        });
        return this;
    }

    public CGStack spacing(int spacing) {
        this.spacing.setInt(spacing);
        return this;
    }

    public CGStack alignment(int alignment) {
        this.alignment.setInt(alignment);
        return this;
    }

    public CGStack alignment(Int alignment) {
        alignment.route(this.alignment);
        return this;
    }

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
                relayout(frame());
            }
        });

        this.alignment.sink(new Sink() {
            protected void onValue(Object value) {
                relayout();
            }
        });

        this.drawables.sink(new Sink() {
            protected void onValue(Object value) {
                groupDrawablesByFlexibility();
                updateIntrinsicContentSize();
                relayout(frame());
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

    protected void drawContent(Graphics g, CGFrame frame) {
        if (frame == null) {
            return;
        }

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

    public void relayout(CGFrame frame) {
        super.relayout(frame);
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

                S.println("WILL DRAW " + child + " " + childFrame);
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

                S.println("HSTACK Will draw " + child + " " + childFrame.x + ", " + childFrame.y + "; " + childFrame.width + ", " + childFrame.height);

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
        S.println(this + " WILL ZDRAW! DRAWABLES: " + this.drawables.getArray().length);
        final Graphics graphics = g;

        final CGFrame thisFrame = frame();
        final CGInsets contentInsets = contentInset();
        int alignment = this.alignment.getInt();

        final boolean isVCenter = CG.isBitSet(alignment, CG.VCENTER);
        final boolean isTop = CG.isBitSet(alignment, CG.TOP);
        final boolean isBottom = CG.isBitSet(alignment, CG.BOTTOM);

        final boolean isHCenter = CG.isBitSet(alignment, CG.HCENTER);
        final boolean isLeft = CG.isBitSet(alignment, CG.LEFT);
        final boolean isRight = CG.isBitSet(alignment, CG.RIGHT);

        S.println("VC top bott ; HC left right " + isVCenter + isTop + isBottom + "; " + isHCenter + isLeft + isRight);

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
        S.println("KEK CGSize updateContentSizeAndChildrenDimensions() " + drawables);

        S.println("LETS COUNT CONTENT SIZE OF " + drawables.length + " SUBVIEWS!");
        for (int i = 0; i < drawables.length; i++) {
            CGDrawable drawable = (CGDrawable) objDrawables[i];
            drawables[i] = drawable;

            S.println(i + "-th subview is " + drawable);

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

        S.println("1 BEFORE MASSIVE CALCULATIONS CONTENT SIZE IS: " + contentWidth + "x" + contentHeight);

        contentWidth = Math.min(contentWidth, this.maxContentWidthBinding.getInt());
        contentHeight = Math.min(contentHeight, this.maxContentHeightBinding.getInt());

        S.println("2 BEFORE MASSIVE CALCULATIONS CONTENT SIZE IS: " + contentWidth + "x" + contentHeight);

        CGSize size = this.adjustFrameToContentSize(contentWidth, contentHeight);
        S.println("3 CGSize size = this.adjustFrameToContentSize(contentWidth, contentHeight); " + size);

        int delta = size.width;
        if (axis != AXIS_Z) {
            delta -= contentWidth;
        }
        
        S.println("4 DELTA WIDTH " + delta);

        int remainingDelta = this.adjustChildrenDimensions(false, delta);

        delta += (delta > 0) ? -remainingDelta : +remainingDelta;
        contentWidth += delta;
        S.println("5 DELTA WIDTH " + delta);

        delta = size.height;
        if (axis != AXIS_Z) {
            delta -= contentHeight;
        }

        S.println("6 DELTA HEIGHT " + delta);

        remainingDelta = this.adjustChildrenDimensions(true, delta);

        delta += (delta > 0) ? -remainingDelta : +remainingDelta;
        S.println("7 DELTA HEIGHT " + delta);
        contentHeight += delta;

        S.println("8 AFTER MASSIVE CALCULATIONS CONTENT SIZE IS: " + contentWidth + "x" + contentHeight);

        CGSize contentSize = new CGSize(contentWidth, contentHeight);
        this.contentSize.setCGSize(contentSize);

        CGFrame frame = this.frame();
        frame.width = size.width;
        frame.height = size.height;
        S.println("AFTER MASSIVE CALCULATIONS STACK SIZE IS: " + size);

        if (size.width != this.width()) {
            this.widthBinding.sendValue(new Int(size.width));
        }

        if (size.height != this.height()) {
            this.heightBinding.sendValue(new Int(size.height));
        }

        return contentSize;
    }
    
    // TODO ВОЗМОЖНО вот тут надо еще и ориджины/инсеты двигать
    private int adjustChildrenDimensions(final boolean isHeight, final int delta) {
        if (delta == 0) {
            return delta;
        }

        int remainingDelta = Math.abs(delta);
        boolean isAxisDimension = this.axis.getInt() == (isHeight ? AXIS_VERTICAL : AXIS_HORIZONTAL);
        
        final boolean isExpanding = delta > 0;

        Hashtable groupedAdjustables = isHeight ? this.drawablesGroupedByHeightFlexibility : this.drawablesGroupedByWidthFlexibility;
        
        if (false == isAxisDimension) {
            S.println("NAX DELTA: " + delta + " IS EXPANDING: " + isExpanding + " IS HEIGHT: " + isHeight + "\n" + (isAxisDimension ? "!!!AXIS" : "!!!NOT AXIS"));

            Vector allAdjustables = new Vector();
            Enumeration values = groupedAdjustables.elements();
            while (values.hasMoreElements()) {
                Vector array = (Vector) values.nextElement();
                for (int i = 0; i < array.size(); i++) {
                    CGDrawable drawable = (CGDrawable) array.elementAt(i);
                    allAdjustables.addElement(drawable);
                }
            }
            Object[] adjustables = S.toArray(allAdjustables);
            S.println("NAX ALL ADJUSTABLES: " + allAdjustables.size());
            
            adjustables = S.filter(adjustables, new S.Filter() {
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

            S.println("NAX " + adjustables.length + " ADJUSTABLES: " + adjustables);
            
            int maxDelta = 0;
            for (int i = 0; i < adjustables.length; i++) {
                CGSomeDrawable view = (CGSomeDrawable) adjustables[i];
                int value = 0;
                int viewDelta = 0;

                if (axis.getInt() == AXIS_Z) {
                    value = isHeight
                            ? Math.min(view.maxHeight(), delta)
                            : Math.min(view.maxWidth(), delta);
                } else {
                    value = isHeight ? view.height() : view.width();
                    
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
                }

                S.print("NAX WILL ADJUST " + (isHeight ? "HEIGHT" : "WIDTH") + " OF " + view + " CUR VALUE " + value + " NEW VALUE ");

                if (isHeight && value != view.height()) {
                    S.print("NAX " + value);
                    view.heightBinding.sendValue(new Int(value));
                } else if (value != view.width()) {
                    S.print("NAX " + value);
                    view.widthBinding.sendValue(new Int(value));
                }

                S.println("");

                maxDelta = Math.max(maxDelta, Math.abs(viewDelta));
            }

            S.println("NAX REMAINIG DELTA " + remainingDelta + "; MAX DELTA " + maxDelta);
            return remainingDelta - maxDelta;
        }

        S.println("AX DELTA: " + delta + " IS EXPANDING: " + isExpanding + " IS HEIGHT: " + isHeight + "\n" + (isAxisDimension ? "!!!AXIS" : "!!!NOT AXIS"));
        
        Enumeration ekeys = groupedAdjustables.keys();
        int flexibilities[] = new int[groupedAdjustables.size()];
        int iKeyIndex = 0;

        S.println("GROUPED ADJUSTABLES: " + flexibilities.length);

        while(ekeys.hasMoreElements()) {
            Integer integerKey = (Integer) ekeys.nextElement();
            flexibilities[iKeyIndex] = integerKey.intValue();

            S.println("ADJ: " + groupedAdjustables.get(integerKey));
            
            iKeyIndex++;
        }
        
        S.sort(flexibilities, 0, flexibilities.length - 1);
        
        for(int flexibilityKeyIndex = flexibilities.length - 1; flexibilityKeyIndex >= 0; flexibilityKeyIndex--) {
            int flexibility = flexibilities[flexibilityKeyIndex];

            Vector adjustablesVector = (Vector) groupedAdjustables.get(new Integer(flexibility));
            Object[] adjustables = S.toArray(adjustablesVector);

            adjustables = S.filter(adjustables, new S.Filter() {
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

            S.println(adjustables.length + " ADJUSTABLES: " + adjustables);

            int adjustablesCount = adjustables.length;
            while (adjustablesCount > 0 && remainingDelta > 0) {
                int sliceToShare = remainingDelta / adjustables.length;
                for (int i = 0; i < adjustablesCount; i++) {
                    CGSomeDrawable view = (CGSomeDrawable) adjustables[i];
                    int extremeValue = isExpanding
                            ? isHeight ? view.maxHeight() : view.maxWidth()
                            : isHeight ? view.minHeight() : view.minWidth();
                    int value = isHeight ? view.height() : view.width();

                    S.println("AX WILL ADJUST " + (isHeight ? "HEIGHT" : "WIDTH") + " OF " + view + " CUR VALUE " + value);

                    int spaceToAdjust = isExpanding
                            ? extremeValue - value
                            : value - extremeValue;

                    spaceToAdjust = Math.min(spaceToAdjust, sliceToShare);
                    value += isExpanding ? +spaceToAdjust : -spaceToAdjust;

                    S.println("VALUE BINDING WILL SET " + value);

                    if (isHeight && value != view.height()) {
                        view.heightBinding.sendValue(new Int(value));
                    } else if (value != view.width()) {
                        view.widthBinding.sendValue(new Int(value));
                    } else {
                        adjustablesCount--;
                    }

                    S.println("REMAINIG DELTA " + remainingDelta + "; spaceToAdjust " + spaceToAdjust);

                    remainingDelta -= spaceToAdjust;
                    if (value == extremeValue) {
                        adjustablesCount--;
                    }
                }
            }
        }
        
        S.println("REMAINIG DELTA " + remainingDelta);
        return remainingDelta;
    }

    private CGSize adjustFrameToContentSize(int contentWidth, int contentHeight) {
        CGSize size = this.frame().getCGSize();
        S.println("adjustFrameToContentSize " + size);

        if (this.height() < contentHeight && this.hasGrowableHeight()) {
            S.println("1 adjustFrameToContentSize");
            int height = Math.min(contentHeight, this.maxHeight());
            size.height = height;
        } else if (contentHeight > this.height() && this.hasShrinkableHeight()) {
            S.println("2 adjustFrameToContentSize");
            int height = Math.max(contentHeight, this.minHeight());
            size.height = height;
        }

        if (contentWidth > this.width() && this.hasGrowableWidth()) {
            S.println("3 adjustFrameToContentSize");
            int width = Math.min(contentWidth, this.maxWidth());
            size.width = width;
        } else if (contentWidth > this.width() && this.hasShrinkableWidth()) {
            S.println("4 adjustFrameToContentSize");
            int width = Math.max(contentWidth, this.minWidth());
            size.width = width;
        }

        return size;
    }

    private void groupDrawablesByFlexibility() {
        this.drawablesGroupedByWidthFlexibility = new Hashtable();
        this.drawablesGroupedByHeightFlexibility = new Hashtable();

        CGDrawable[] drawables = (CGDrawable[]) this.drawables.getArray();
        Hashtable[] tables = new Hashtable[]{
            this.drawablesGroupedByWidthFlexibility,
            this.drawablesGroupedByHeightFlexibility
        };

        for (int i = 0; i < drawables.length; i++) {
            CGDrawable drawable = drawables[i];
            for (int j = 0; j < drawable.flexibility().length; j++) {
                Hashtable hashtable = tables[j];
                if (j == 0 && !(drawable.hasGrowableWidth() || drawable.hasShrinkableWidth())) {
                    continue;
                }
                else if(j == 1 && !(drawable.hasGrowableHeight() || drawable.hasShrinkableHeight())) {
                    continue;
                }
                int f = drawable.flexibility()[j];
                if (f == 0) {
                    continue;
                }
                Integer flexibility = new Integer(f);
                Vector group = (Vector) hashtable.get(flexibility);
                if (group == null) {
                    group = new Vector();
                    hashtable.put(flexibility, group);
                }
                group.addElement(drawable);
            }
        }
    }
    
    protected void updateIntrinsicContentSize() {
        super.updateIntrinsicContentSize();
        
        this.groupDrawablesByFlexibility();

        S.println(this + " will updateContentSizeAndChildrenDimensions()");
        this.updateContentSizeAndChildrenDimensions();

        CGSize size = this.contentSize().getCGSize();
        S.println(this + " DID UPDATE SIZES, NOW HAS: " + size);

        this.intrinsicContentSizeBinding.sendValue(size);
    }
}
