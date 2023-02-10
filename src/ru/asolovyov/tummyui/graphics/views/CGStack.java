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
import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.operators.mapping.Map;
import ru.asolovyov.combime.publishers.Publisher;
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
   
    protected Arr drawables = new Arr(new CGDrawable[]{});

    protected Int alignment = new Int(CG.TOP | CG.HCENTER);
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

    protected Point contentOffsetBinding = new Point(CGPoint.zero());

    public CGStack maxContentWidth(int width) {
        this.maxContentWidthBinding.setInt(width);
        return this;
    }

    public CGStack maxContentHeight(int height) {
        this.maxContentHeightBinding.setInt(height);
        return this;
    }
    
    public CGStack maxContentWidth(Int width) {
        width.route(this.maxContentWidthBinding);
        return this;
    }

    public CGStack maxContentHeight(Int height) {
        height.route(this.maxContentHeightBinding);
        return this;
    }

    public int spacing() {
        return this.spacing.getInt();
    }

    public CGStack spacing(Int spacing) {
        spacing.route(this.spacing);
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

    public CGPoint contentOffset() {
        return this.contentOffsetBinding.getCGPoint();
    }

    public CGDrawable contentOffset(Point offset) {
        offset.route(this.contentOffsetBinding);
        return this;
    }

    public CGDrawable contentOffset(int x, int y) {
        this.contentOffsetBinding.sendValue(new CGPoint(x, y));
        return this;
    }

    public CGStack(Int axis, Arr models, DrawableFactory factory) {
        this(axis, new Arr(new CGDrawable[]{}));
        this.factory = factory;

        S.println(" CGStack(Int axis, Int alignment, Arr models, DrawableFactory factory) " + drawables);
        models.route(this.models);
    }

    public CGStack(int axis, Object[] models, DrawableFactory factory) {
        this(new Int(axis), new Arr(models), factory);
    }

    public CGStack(Int axis, Arr drawables) {
        super();
        S.println("KEK CGSTACK WILL SET DRAWABLES: " + drawables);
        
        this.subscribeToBindings();

        axis.route(this.axis);
        drawables.route(this.drawables);
    }

    private void subscribeToBindings() {
        Publisher.combineLatest(new IPublisher[] {
            this.widthBinding,
            this.heightBinding
        }).removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                limitExcessiveIntrinsicsToThisSize();
            }
        });

        Publisher.combineLatest(new Publisher[] {
            this.drawables,
            this.alignment,
            this.axis,
            this.maxContentWidthBinding,
            this.maxContentHeightBinding,
            this.spacing,
            this.contentSize,
            this.contentOffsetBinding
        }).removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                relayout();
            }
        });
        
        this.models.to(
                new Map() {
                    public Object mapValue(Object value) {
                        Object[] models = (Object[]) value;
                        CGDrawable[] drawables = new CGDrawable[models.length];
                        S.println("KEK CGDrawable[] drawables = new CGDrawable[models.length]; " + drawables);
                        for (int i = 0; i < models.length; i++) {
                            Object model = models[i];

                            CGDrawable drawable = CGStack.this.factory.itemFor(model);
                            drawables[i] = drawable;
                        }
                        return drawables;
                    }
                }).route(drawables);
    }

    public int axis() {
        return axis.getInt();
    }

    protected void drawContent(Graphics g, CGFrame frame) {
        if (frame == null) {
            return;
        }

        S.println(this + " CGSTACK DRAW!");

        if (this.axis.getInt() == AXIS_HORIZONTAL) {
            this.hDraw(g);
        } else if (this.axis.getInt() == AXIS_VERTICAL) {
            this.vDraw(g);
        } else if (this.axis.getInt() == AXIS_Z) {
            this.zDraw(g);
        }
    }

    private void pushFrameToChildren() {
        S.println("KEK DRAWABLES " + this.drawables.getArray());
        CGDrawable[] drawables = (CGDrawable[]) this.drawables.getArray();
        for (int i = 0; i < drawables.length; i++) {
            CGDrawable drawable = drawables[i];
            if (drawable.geometryReader() != null) {
                drawable.geometryReader().read(drawable, frame());
            }
        }
    }

    public void relayout() {
        super.relayout();
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
            this.nextLeft += (thisFrame.width - contentWidth) / 2;
        }
        if (CG.isBitSet(alignmentInt, CG.RIGHT)) {
            this.nextLeft += thisFrame.width - contentWidth;
        }

        final boolean isVCenter = CG.isBitSet(alignmentInt, CG.VCENTER);
        final boolean isTop = CG.isBitSet(alignmentInt, CG.TOP);
        final boolean isBottom = CG.isBitSet(alignmentInt, CG.BOTTOM);

        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGSomeDrawable child = (CGSomeDrawable) element;
                CGFrame childFrame = child.intrinsicAwareFrame();

//                S.println("WILL DRAW " + child + " " + childFrame);
                CGInsets contentInsets = contentInset();

                childFrame.x = nextLeft;
                childFrame.y = thisFrame.y + contentInsets.top;
                
                if (isVCenter) {
                    childFrame.y += (thisFrame.height - childFrame.height) / 2 - contentInsets.bottom;
                }
                else if(isBottom) {
                    childFrame.y += thisFrame.height - childFrame.height - contentInsets.bottom;
                }

                childFrame.x -= contentOffset().x;
                childFrame.y -= contentOffset().y;

//                child.origin(childFrame.x, childFrame.y);
//                child.frame(childFrame.x, childFrame.y, childFrame.width, childFrame.height);
//                S.println("HSTACK Will draw " + child + " " + childFrame.x + ", " + childFrame.y + "; " + childFrame.width + ", " + childFrame.height);

                child.xBinding.setInt(childFrame.x); child.yBinding.setInt(childFrame.y);
                S.println("HDRAW WILL SET W:" + childFrame.width + " H: " + childFrame.height + " TO " + child);
                child.widthBinding.setInt(childFrame.width); child.heightBinding.setInt(childFrame.height);

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

        int alignment = this.alignment.getInt();
        int contentHeight = this.contentSize.getCGSize().height;

        S.println("VSTACK FIRST TOP ALIGNMENT " + alignment);

        if (CG.isBitSet(alignment, CG.VCENTER)) {
            S.println("VSTACK FIRST TOP 1");
            this.nextTop = thisFrame.y + (thisFrame.height - contentHeight) / 2 + contentInsets.top;
        }
        if (CG.isBitSet(alignment, CG.TOP)) {
            S.println("VSTACK FIRST TOP 2");
            this.nextTop = thisFrame.y + contentInsets.top;
        }
        if (CG.isBitSet(alignment, CG.BOTTOM)) {
            S.println("VSTACK FIRST TOP 3");
            this.nextTop = thisFrame.y + thisFrame.height - contentHeight + contentInsets.top;
        }

        final boolean isHCenter = CG.isBitSet(alignment, CG.HCENTER);
        final boolean isRight = CG.isBitSet(alignment, CG.RIGHT);
        
        S.println("VSTACK " + this);
        S.println("VSTACK CI " + contentInsets);

        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGSomeDrawable child = (CGSomeDrawable) element;
                CGFrame childFrame = child.intrinsicAwareFrame();

                childFrame.x = thisFrame.x + contentInsets.left;
                childFrame.y = nextTop;

                if (isHCenter) {
                    childFrame.x += (thisFrame.width - childFrame.width) / 2 - contentInsets.left;
                }
                else if(isRight) {
                    childFrame.x += thisFrame.width - childFrame.width - contentInsets.horizontal();
                }

                childFrame.x -= contentOffset().x;
                childFrame.y -= contentOffset().y;

                S.println("VSTACK Will draw " + child + " " + childFrame.x + ", " + childFrame.y + "; " + childFrame.width + ", " + childFrame.height);

//                child.origin(childFrame.x, childFrame.y);
//                child.frame(childFrame.x, childFrame.y, childFrame.width, childFrame.height);
                child.xBinding.setInt(childFrame.x); child.yBinding.setInt(childFrame.y);
                child.widthBinding.setInt(childFrame.width); child.heightBinding.setInt(childFrame.height);

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
                CGSomeDrawable child = (CGSomeDrawable) element;
                CGFrame childFrame = child.intrinsicAwareFrame();
                childFrame.width = Math.max(childFrame.width, contentSize.getCGSize().width);
                childFrame.height = Math.max(childFrame.height, contentSize.getCGSize().height);

                if (isLeft) {
                    childFrame.x = thisFrame.x + contentInsets.left;
                }
                if (isHCenter) {
                    childFrame.x = (thisFrame.width - childFrame.width) / 2 + contentInsets.left;
                }
                if (isRight) {
                    childFrame.x = thisFrame.width - childFrame.width + contentInsets.left;
                }

                if (isTop) {
                    childFrame.y = thisFrame.y + contentInsets.top;
                }
                if (isVCenter) {
                    childFrame.y = (thisFrame.height - childFrame.height) / 2 + contentInsets.top;
                }
                if (isBottom) {
                    childFrame.y = thisFrame.height - childFrame.height + contentInsets.top;
                }

                childFrame.x -= contentOffset().x;
                childFrame.y -= contentOffset().y;
                
//                child.origin(childFrame.x, childFrame.y);
                //УБРАТЬ НАХУЙ ПРОСЕТЫВАНИЕ ИНТРИНСИКА И ВЫКИНУТЬ ЕГО В ТЕКСТ_ВЬЮ ТК ИЗ_ЗА НЕГО ВЕСЬ ГЕМОР
//                child.frame(childFrame.x, childFrame.y, childFrame.width, childFrame.height);
                child.xBinding.setInt(childFrame.x); child.yBinding.setInt(childFrame.y);
                child.widthBinding.setInt(childFrame.width); child.heightBinding.setInt(childFrame.height);

                child.draw(graphics);
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
                S.println("PRESS DETECTED: " + value);
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

        S.println("moveContentByKeyPress: CON_SIZE: " + contentSize + " vs FRAME: " + thisFrame + " vs CON_OFFSET " + contentOffset);

        CGPoint minOffset = this.minContentOffset();
        CGPoint maxOffset = this.maxContentOffset();
        
        if (contentSize.height > thisFrame.height) {
            S.println("contentSize.height > thisFrame.height");

            if (keyCode == CG.KEY_UP) {//t
                S.println("keyCode == Canvas.UP");
                contentOffset.y = Math.max( contentOffset.y - 5, minOffset.y);
            } else if (keyCode == CG.KEY_DOWN) {//b
                S.println("keyCode == Canvas.DOWN");
                contentOffset.y = Math.min(contentOffset.y + 5, maxOffset.y);
            }
        }

        if (contentSize.width > thisFrame.width) {
            if (keyCode == CG.KEY_LEFT) {//l
                S.println("Canvas.LEFT");
                contentOffset.x = Math.max(contentOffset.x - 5, minOffset.x);
                
            } else if (keyCode == CG.KEY_RIGHT) { //r
                S.println("keyCode == Canvas.RIGHT");
                contentOffset.x = Math.min(contentOffset.x + 5, maxOffset.x);
            }
        }

        S.println("CONTENT OFFSET NOW: " + contentOffset.x + "; " + contentOffset.y + "\n");

        contentOffsetBinding.sendValue(contentOffset);
    }

    private CGPoint minContentOffset() {
        int axis = this.axis();
        int alignment = this.alignment.getInt();
        CGSize contentSize = this.contentSize.getCGSize();
        
        CGFrame frame = this.frame();

        boolean isVCenter = CG.isBitSet(alignment, CG.VCENTER);
        boolean isBottom = CG.isBitSet(alignment, CG.BOTTOM);

        boolean isHCenter = CG.isBitSet(alignment, CG.HCENTER);
        boolean isRight = CG.isBitSet(alignment, CG.RIGHT);

        //contentInset().left и contentInset().top приплюсовываются в методах xDraw
        CGPoint offset = new CGPoint(0, 0);

        if(isHCenter) {
            offset.x -= ((contentSize.width - frame.width) / 2);// - contentInset().right);
        } 
        else if (isRight) {
            offset.x -= (contentSize.width - frame.width);
        }

        if(isVCenter) {
            offset.y -= ((contentSize.height - frame.height) / 2);
        }
        else if(isBottom) {
            offset.y -= (contentSize.height - frame.height);
        }

        return offset;
    }

    private CGPoint maxContentOffset() {
        int axis = this.axis();
        int alignment = this.alignment.getInt();
        CGSize contentSize = this.contentSize.getCGSize();
        CGFrame frame = this.frame();

        boolean isVCenter = CG.isBitSet(alignment, CG.VCENTER);
        boolean isTop = CG.isBitSet(alignment, CG.TOP);
        boolean isBottom = CG.isBitSet(alignment, CG.BOTTOM);

        boolean isHCenter = CG.isBitSet(alignment, CG.HCENTER);
        boolean isLeft = CG.isBitSet(alignment, CG.LEFT);
        boolean isRight = CG.isBitSet(alignment, CG.RIGHT);

        CGPoint offset = new CGPoint(
                contentSize.width - frame.width,
                contentSize.height - frame.height);
        
        if (isRight) {
            offset.x -= (contentSize.width - frame.width);
        }
        else if(isHCenter) {
            offset.x -= ((contentSize.width - frame.width) / 2);
        }

        if (isBottom) {
            offset.y -= (contentSize.height - frame.height);
        }
        else if(isVCenter) {
            offset.y -= ((contentSize.height - frame.height) / 2);
        }

        return offset;
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
                S.println("PRESS RELEASED: " + value);
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
            S.println("9 WIDTH");
            this.widthBinding.setInt(size.width);
        }

        if (size.height != this.height()) {
            S.println("9 HEIGHT");
            this.heightBinding.setInt(size.height);
        }

        return contentSize;
    }
    
    private int adjustChildrenDimensions(final boolean isHeight, final int delta) {
        if (delta == 0) {
            return delta;
        }

        int remainingDelta = Math.abs(delta);
        boolean isAxisDimension = this.axis.getInt() == (isHeight ? AXIS_VERTICAL : AXIS_HORIZONTAL);
        
        final boolean isExpanding = delta > 0;

        Hashtable groupedAdjustables = isHeight ? this.drawablesGroupedByHeightFlexibility : this.drawablesGroupedByWidthFlexibility;
        S.println("GROUPED ADJUSTABLES " + groupedAdjustables);
        
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

                S.println("NAX WILL ADJUST " + (isHeight ? "HEIGHT" : "WIDTH") + " OF " + view + " CUR VALUE " + value + " NEW VALUE ");

                if (isHeight) {
                    if (value != view.height()) {
                        S.println(" NAX " + value);
                        view.heightBinding.setInt(value);
                    }
                } else if (value != view.width()) {
                    S.println(" NAX " + value);
                    view.widthBinding.setInt(value);
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
                        view.heightBinding.setInt(value);
                    } else if (!isHeight && value != view.width()) {
                        view.widthBinding.setInt(value);
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

        S.println("WILL GROUP DRAWABLES: " + drawables.length);

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
                if (f == CGDrawable.FLEXIBILITY_NONE) {
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

    private void limitExcessiveIntrinsicsToThisSize() {
        int maxWidth = frame().width == CG.NULL ? Integer.MAX_VALUE : frame().width;
        int maxHeight = frame().height == CG.NULL ? Integer.MAX_VALUE : frame().height;

        if (this.axis() == CGStack.AXIS_HORIZONTAL) {
            maxWidth = Integer.MAX_VALUE;
        } else if (this.axis() == CGStack.AXIS_VERTICAL) {
            maxHeight = Integer.MAX_VALUE;
        }

        S.println("EXC WILL LIMIT EXCESSIVE TO w: " + maxWidth + " h: " + maxHeight);

        CGDrawable[] drawables = (CGDrawable[]) this.drawables.getArray();
        for (int i = 0; i < drawables.length; i++) {
            CGSomeDrawable drawable = (CGSomeDrawable)drawables[i];
                    
            CGFrame frame = drawable.intrinsicAwareFrame();
            frame.width = Math.min(frame.width, maxWidth);
            frame.height = Math.min(frame.height, maxHeight);

//            if (drawable.hasShrinkableWidth()) {
                S.println("EXC w: " + frame.width);
                drawable.widthBinding.setInt(frame.width);
//            }
//            if (drawable.hasShrinkableHeight()) {
                S.println("EXC h: " + frame.height);
                drawable.heightBinding.setInt(frame.height);
//            }
        }
    }
    
    protected void updateIntrinsicContentSize() {
        super.updateIntrinsicContentSize();

        S.println(this + " will GROUP ADJUSTABLES");
        
        this.groupDrawablesByFlexibility();

        S.println(this + " will updateContentSizeAndChildrenDimensions()");
        this.updateContentSizeAndChildrenDimensions();

        CGSize size = this.contentSize().getCGSize();
        S.println(this + " DID UPDATE SIZES, NOW HAS: " + size);

        this.intrinsicContentSizeBinding.sendValue(size);
        S.println("DID SEND NEW SIZE TO INTRINSIC OK");
    }
}
