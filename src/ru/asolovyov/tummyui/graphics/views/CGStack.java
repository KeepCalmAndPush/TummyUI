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

    public final static int AXIS_HORIZONTAL = 0;
    public final static int AXIS_VERTICAL = 1;
    public final static int AXIS_Z = 2;

    protected Arr drawables = new Arr(new CGDrawable[]{});
    protected Int alignment = new Int(CG.LEFT);
    protected Int axis = new Int(AXIS_HORIZONTAL);

    protected Arr models = new Arr(new Object[]{});
    protected DrawableFactory factory;

    protected Size contentSize = new Size(CG.VALUE_NOT_SET, CG.VALUE_NOT_SET);

    private List repeatedKeys = new List();

    public Size contentSize() {
        return contentSize;
    }

    public CGStack(Int axis, Int alignment, Arr models, DrawableFactory factory) {
        this(axis, alignment, new Arr(new CGDrawable[]{}));
        this.factory = factory;
        this.models = models;

        this.models.to(
                new Map() {
                    public Object mapValue(Object value) {
                        Object[] models = (Object[]) value;
                        CGDrawable[] drawables = new CGDrawable[models.length];
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
        this.axis = axis;
        this.alignment = alignment;
        this.drawables = drawables;

        this.axis.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });

        this.alignment.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });

        this.drawables.sink(new Sink() {
            protected void onValue(Object value) {
                updateIntrinsicContentSize();
                needsRelayout(getCGFrame());
            }
        });
    }

    public Int axis() {
        return axis;
    }

    public void draw(Graphics g) {
        super.draw(g);

        S.println("CGSTACK DRAW!");
        
        if (this.axis.getInt() == AXIS_HORIZONTAL) {
            this.hDraw(g);
        } else if (this.axis.getInt() == AXIS_VERTICAL) {
            this.vDraw(g);
        } else if (this.axis.getInt() == AXIS_Z) {
            this.zDraw(g);
        }
    }

    private void pushFrameToChildren() {
        S.println("CGSTACK WILL DRAW N VIEWS: " + this.drawables.getArray().length);
        
        CGDrawable[] drawables_ = (CGDrawable[]) this.drawables.getArray();
        for (int i = 0; i < drawables_.length; i++) {
            CGDrawable drawable = drawables_[i];
            if (drawable.getGeometryReader() != null) {
                drawable.getGeometryReader().read(drawable, getCGFrame());
            }
        }
    }

    public void needsRelayout(CGFrame frame) {
        super.needsRelayout(frame);
        this.pushFrameToChildren();
    }

    private int nextLeft = 0;
    private int nextTop = 0;

    private void hDraw(Graphics g) {
        final Graphics graphics = g;

        final CGFrame thisFrame = getCGFrame().copy();
        CGInsets contentInsets = getContentInset().getCGInsets();

        this.nextLeft = thisFrame.x + contentInsets.left;
        this.nextTop = thisFrame.y + contentInsets.top;

        int alignmentInt = this.alignment.getInt();
        int contentWidth = this.contentSize().getCGSize().width;

        if (CG.isBitSet(alignmentInt,CG.HCENTER)) {
            this.nextLeft = thisFrame.x + (thisFrame.width - contentWidth) / 2 + contentInsets.left - contentInsets.right;
        } 
        if (CG.isBitSet(alignmentInt,CG.LEFT)) {
            this.nextLeft = thisFrame.x + contentInsets.left + contentInsets.left;
        }
        if (CG.isBitSet(alignmentInt,CG.RIGHT)) {
            this.nextLeft = thisFrame.x + thisFrame.width - contentWidth - contentInsets.right;
        }

        final boolean isVCenter = CG.isBitSet(alignmentInt, CG.VCENTER);
        final boolean isTop = CG.isBitSet(alignmentInt, CG.TOP);
        final boolean isBottom = CG.isBitSet(alignmentInt, CG.BOTTOM);

        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGDrawable child = (CGDrawable) element;
                CGFrame childFrame = child.intrinsicAwareFrame().copy();

                CGInsets contentInsets = getContentInset().getCGInsets();

                int mask = child.resizingMask().getInt();
                final boolean hasFlexibleX = CG.isBitSet(mask, CGFrame.FLEXIBLE_X);
                final boolean hasFlexibleY = CG.isBitSet(mask, CGFrame.FLEXIBLE_Y);

                if (hasFlexibleX) {
                    childFrame.x = nextLeft;
                }

                if (hasFlexibleY) {
                    if (isVCenter) {
                        childFrame.y = thisFrame.y + (thisFrame.height - childFrame.height) / 2 + contentInsets.top - contentInsets.bottom;
                    }
                    if (isTop) {
                        childFrame.y = thisFrame.y + contentInsets.top;
                    }
                    if (isBottom) {
                        childFrame.y = thisFrame.y + thisFrame.height - childFrame.height - contentInsets.bottom;
                    }
                }

//                childFrame.x += child.getOrigin().getCGPoint().x;
//                childFrame.y += child.getOrigin().getCGPoint().y;

                childFrame.x -= getContentOffset().getCGPoint().x;
                childFrame.y -= getContentOffset().getCGPoint().y;

                child.setOrigin(childFrame.x, childFrame.y);

                S.println("HSTACK Will draw " + child + " " + childFrame.x + ", " + childFrame.y + "; " + childFrame.width + ", " + childFrame.height);

                child.draw(graphics);
                nextLeft += childFrame.width;
            }
        });
    }
    
    private void vDraw(Graphics g) {
        final Graphics graphics = g;

        final CGFrame thisFrame = getCGFrame().copy();
        final CGInsets contentInsets = getContentInset().getCGInsets();

        this.nextLeft = thisFrame.x + contentInsets.left;
        this.nextTop = thisFrame.y + contentInsets.top;
           
        int alignmentInt = this.alignment.getInt();
        int contentHeight = this.contentSize.getCGSize().height;

        if (CG.isBitSet(alignmentInt, CG.VCENTER)) {
            this.nextTop = thisFrame.y + (thisFrame.height - contentHeight) / 2 + contentInsets.top - contentInsets.bottom;
        }
        if (CG.isBitSet(alignmentInt, CG.TOP)) {
            this.nextTop = thisFrame.y + contentInsets.top;
        }
        if (CG.isBitSet(alignmentInt, CG.BOTTOM)) {
            this.nextTop = thisFrame.y + thisFrame.height - contentHeight - contentInsets.bottom;
        }

        final boolean isHCenter = CG.isBitSet(alignmentInt, CG.HCENTER);
        final boolean isLeft = CG.isBitSet(alignmentInt, CG.LEFT);
        final boolean isRight = CG.isBitSet(alignmentInt, CG.RIGHT);

        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGDrawable child = (CGDrawable)element;
                CGFrame childFrame = child.intrinsicAwareFrame();

                int mask = child.resizingMask().getInt();
                final boolean hasFlexibleX = CG.isBitSet(mask, CGFrame.FLEXIBLE_X);
                final boolean hasFlexibleY = CG.isBitSet(mask, CGFrame.FLEXIBLE_Y);
                
                if (hasFlexibleX) {
                    if (isHCenter) {
                        childFrame.x = thisFrame.x + (thisFrame.width - childFrame.width) / 2 + contentInsets.left - contentInsets.right;
                    }
                    if (isLeft) {
                        childFrame.x = thisFrame.x + contentInsets.left;
                    }
                    if (isRight) {
                        childFrame.x = thisFrame.x + thisFrame.width - childFrame.width - contentInsets.right;
                    }
                }

                if (hasFlexibleY) {
                    childFrame.y = nextTop;
                }
                
//                childFrame.x += child.getOrigin().getCGPoint().x;
//                childFrame.y += child.getOrigin().getCGPoint().y;

                childFrame.x -= getContentOffset().getCGPoint().x;
                childFrame.y -= getContentOffset().getCGPoint().y;

                S.println("VSTACK Will draw " + child + " " + childFrame.x + ", " + childFrame.y + "; " + childFrame.width + ", " + childFrame.height);

                child.setOrigin(childFrame.x, childFrame.y);
                
                child.draw(graphics);

                nextTop += childFrame.height;
            }
        });

//        S.println("=======");
    }

    private void zDraw(Graphics g) {
        final Graphics graphics = g;

        final CGFrame thisFrame = getCGFrame();
        final CGInsets contentInsets = getContentInset().getCGInsets();

        this.nextLeft = thisFrame.x + contentInsets.left;
        this.nextTop = thisFrame.y + contentInsets.top;

        for (int i = 0; i < this.drawables.getArray().length; i++) {
            CGDrawable object = (CGDrawable)this.drawables.getArray()[i];
            int mask = object.resizingMask().getInt();
            CGFrame frame = object.intrinsicAwareFrame();

            if (CG.isBitSet(mask, CGFrame.FLEXIBLE_WIDTH)) {
                frame.width = thisFrame.width;
            }

            if (CG.isBitSet(mask,  CGFrame.FLEXIBLE_HEIGHT)) {
                frame.height = thisFrame.height;
            }
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
                CGDrawable drawable = (CGDrawable)element;
                CGFrame frame = drawable.intrinsicAwareFrame();

                int mask = drawable.resizingMask().getInt();
                final boolean hasFlexibleX = CG.isBitSet(mask, CGFrame.FLEXIBLE_X);
                final boolean hasFlexibleY = CG.isBitSet(mask, CGFrame.FLEXIBLE_Y);

                if (hasFlexibleY) {
                    if (isVCenter) {
                        frame.y = (thisFrame.height - frame.height) / 2 + contentInsets.top - contentInsets.bottom;
                    }
                    if (isTop) {
                        frame.y = 0 + contentInsets.top;
                    }
                    if (isBottom) {
                        frame.y = thisFrame.height - frame.height - contentInsets.bottom;
                    }
                }

                if (hasFlexibleX) {
                    if (isHCenter) {
                        frame.x = (thisFrame.width - frame.width) / 2 + contentInsets.left - contentInsets.right;
                    }
                    if (isLeft) {
                        frame.x = contentInsets.left;
                    }
                    if (isRight) {
                        frame.x = thisFrame.width - frame.width - contentInsets.right;
                    }
                }

                frame.x += drawable.getOrigin().getCGPoint().x;
                frame.y += drawable.getOrigin().getCGPoint().y;

                drawable.setOrigin(frame.x, frame.y);

                drawable.draw(graphics);
            }
        });
    }

    public CGDrawable setCanvas(CGCanvas canvas) {
        super.setCanvas(canvas);
        
        this.subscribeToKeyPressed();
        this.subscribeToKeyReleased();

        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGDrawable drawable = (CGDrawable)element;
                drawable.setCanvas(CGStack.this.getCanvas());
            }
        });
        return this;
    }

    private void subscribeToKeyPressed() {
        CGCanvas canvas = this.getCanvas();
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

                keyRepeatQueue.async(2*CG.FRAME_MILLIS, new Runnable() {
                    public void run() {
                        scheduleKeyRepeatedHandling(keyCodeInteger);
                    }
                });
            }
        });
    }
    
    private void moveContentByKeyPress(int keyCode) {
        CGInsets contentInset = contentInsetBinding.getCGInsets();

        CGPoint contentOffset = contentOffsetBinding.getCGPoint();
        CGSize contentSize = contentSize().getCGSize();

        CGFrame thisFrame = getCGFrame();

        if (contentSize.height > thisFrame.height) {
            S.println("contentSize.height > thisFrame.height");
            int extent = contentSize.height - thisFrame.height;

            if (keyCode == -1) {//t
                S.println("keyCode == Canvas.UP");
                contentOffset.y = Math.max(
                        contentOffset.y - 5,
                        -(extent - contentInset.top));
            } else if (keyCode == -2) {//b
                S.println("keyCode == Canvas.DOWN");
                contentOffset.y = Math.min(
                        contentOffset.y + 5,
                        extent + contentInset.bottom);
            }
        }

        if (contentSize.width > thisFrame.width) {
            S.println("contentSize.width > thisFrame.width");
            int extent = contentSize.width - thisFrame.width;
            if (keyCode == -3) {//l
                S.println("Canvas.LEFT");
                contentOffset.x = Math.max(
                        contentOffset.x - 5,
                        -(extent + contentInset.left));
            } else if (keyCode == -4) { //r
                S.println("keyCode == Canvas.RIGHT");
                contentOffset.x = Math.min(
                        contentOffset.x + 5,
                        extent + contentInset.right);
            }
        }

        S.println("\nCONTENT OFFSET NOW: " + contentOffset.x + "; " + contentOffset.y + "\n");

        contentOffsetBinding.setCGPoint(contentOffset);
    }


    private DispatchQueue keyRepeatQueue = new DispatchQueue(120);

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
        CGCanvas canvas = this.getCanvas();
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

    //TODO может тут надо будет учитывать офссеты дочерних вьюх
    protected CGSize updateContentSizeAndApplyMasksToChildren() {
        int contentWidth = 0;
        int contentHeight = 0;
        int unallocatedWidthCount = 0;
        int unallocatedHeightCount = 0;
        int axis = axis().getInt();
        
        List dynamicWidthChildren = new List();
        List dynamicHeightChildren = new List();

        CGFrame thisFrame = getCGFrame();
        CGInsets contentInsets = getContentInset().getCGInsets();

        int availableWidth = thisFrame.width - contentInsets.left - contentInsets.right;
        int availableHeight = thisFrame.height - contentInsets.top - contentInsets.bottom;
        
        int maxWidth = 0;
        int maxHeight = 0;

        Object[] drawables = this.drawables.getArray();

        for (int i = 0; i < drawables.length; i++) {
            CGDrawable object = (CGDrawable)drawables[i];
            int mask = object.resizingMask().getInt();

            boolean hasFlexibleWidth = CG.isBitSet(mask, CGFrame.FLEXIBLE_WIDTH);
            boolean hasFlexibleHeight = CG.isBitSet(mask, CGFrame.FLEXIBLE_HEIGHT);

            if (hasFlexibleWidth) {
                dynamicWidthChildren.addElement(object);
            }

            if (hasFlexibleHeight) {
                dynamicHeightChildren.addElement(object);
            }

            int childWidth = object.intrinsicAwareFrame().width;
            int childHeight = object.intrinsicAwareFrame().height;
            
            if (axis == AXIS_HORIZONTAL) {
                if (childWidth == CG.VALUE_NOT_SET && hasFlexibleWidth) {
                    unallocatedWidthCount++;
                }
                contentWidth += childWidth;
            } else if (axis == AXIS_VERTICAL) {
                if (childHeight == CG.VALUE_NOT_SET && hasFlexibleHeight) {
                    unallocatedHeightCount++;
                }
                contentHeight += childHeight;
            }
        }

        int defaultHeight = availableHeight;
        if (axis == AXIS_VERTICAL) {
             defaultHeight = unallocatedHeightCount > 0
                ? (availableHeight - contentHeight) / unallocatedHeightCount
                : 0;
        } else if (axis == AXIS_HORIZONTAL) {
            if (unallocatedHeightCount > 0 && contentHeight > 0) {
                defaultHeight = contentHeight;
            }
        }

        int defaultWidth = availableWidth;
        if (axis == AXIS_HORIZONTAL) {
             defaultWidth = unallocatedWidthCount > 0
                ? (availableWidth - contentWidth) / unallocatedWidthCount
                : 0;
        } else if (axis == AXIS_VERTICAL) {
            if (unallocatedWidthCount > 0 && contentWidth > 0) {
                defaultWidth = contentWidth;
            }
        }

        for (int i = 0; i < drawables.length; i++) {
            CGDrawable drawable = (CGDrawable)drawables[i];
            int mask = drawable.resizingMask().getInt();
            CGFrame frame = drawable.intrinsicAwareFrame();

            if (axis == AXIS_VERTICAL) {
                if (frame.width == CG.VALUE_NOT_SET && CG.isBitSet(mask, CGFrame.FLEXIBLE_WIDTH)) {
                    frame.width = defaultWidth;
                }

                if (frame.height == CG.VALUE_NOT_SET && CG.isBitSet(mask, CGFrame.FLEXIBLE_HEIGHT)) {
                    frame.height = defaultHeight;
                }
            }

            if (axis == AXIS_HORIZONTAL) {
                if (frame.width == CG.VALUE_NOT_SET && CG.isBitSet(mask, CGFrame.FLEXIBLE_WIDTH)) {
                    frame.width = defaultWidth;
                }

                if (frame.height == CG.VALUE_NOT_SET && CG.isBitSet(mask, CGFrame.FLEXIBLE_HEIGHT)) {
                    frame.height = defaultHeight;
                }
            }

            drawable.width(frame.width);
            drawable.height(frame.height);

            maxWidth = Math.max(maxWidth, frame.width);
            maxHeight = Math.max(maxHeight, frame.height);
        }

        if (axis == AXIS_VERTICAL) {
            contentWidth = maxWidth;
        } else {
            contentWidth += defaultWidth * unallocatedWidthCount;
        }

        if (axis == AXIS_HORIZONTAL) {
            contentHeight = maxHeight;
        } else {
            contentHeight += defaultHeight * unallocatedHeightCount;
        }
        
        contentWidth += (contentInsets.left + contentInsets.right);
        contentHeight += (contentInsets.top + contentInsets.bottom);
        
        CGSize size = new CGSize(contentWidth, contentHeight);
        this.contentSize.setCGSize(size);

        int mask = this.resizingMask().getInt();
        if (CG.isBitSet(mask, CGFrame.FLEXIBLE_HEIGHT)) {
            this.height(size.height);
        }
        if (CG.isBitSet(mask, CGFrame.FLEXIBLE_WIDTH)) {
            this.width(size.width);
        }
        
        return size;
    }

    protected void updateIntrinsicContentSize() {
        super.updateIntrinsicContentSize();
        this.updateContentSizeAndApplyMasksToChildren();
        CGSize size = this.contentSize().getCGSize();
        this.intrinsicContentSizeBinding.setCGSize(size);
    }
}