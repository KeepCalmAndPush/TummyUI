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

    protected Size contentSize = new Size(0, 0);

    private List repeatedKeys = new List();

    public Size contentSize() {
        return contentSize;
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
                needsRelayout(getCGFrame());
            }
        });
    }

    public Int axis() {
        return axis;
    }

    public void draw(Graphics g) {
        super.draw(g);
        
        if (this.axis.getInt() == AXIS_HORIZONTAL) {
            this.hDraw(g);
        } else if (this.axis.getInt() == AXIS_VERTICAL) {
            this.vDraw(g);
        } else if (this.axis.getInt() == AXIS_Z) {
            this.zDraw(g);
        }
    }

    private void pushFrameToChildren() {
        S.print(this.drawables);
        
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

        this.nextLeft = getCGFrame().x;
        this.nextTop = getCGFrame().y;

        this.nextLeft += getCGFrame().x;

        int alignmentInt = this.alignment.getInt();
        int contentWidth = updateContentSizeAndApplyMasksToChildren().width;

        if (CG.isBitSet(alignmentInt,CG.HCENTER)) {
            this.nextLeft = (getCGFrame().width - contentWidth) / 2;
        } 
        if (CG.isBitSet(alignmentInt,CG.LEFT)) {
            this.nextLeft = getCGFrame().x;
        }
        if (CG.isBitSet(alignmentInt,CG.RIGHT)) {
            this.nextLeft = getCGFrame().width - contentWidth;
        }

        final boolean isVCenter = CG.isBitSet(alignmentInt, CG.VCENTER);
        final boolean isTop = CG.isBitSet(alignmentInt, CG.TOP);
        final boolean isBottom = CG.isBitSet(alignmentInt, CG.BOTTOM);

        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGDrawable drawable = (CGDrawable) element;
                CGFrame frame = drawable.intrinsicAwareFrame();

                int mask = drawable.resizingMask().getInt();
                final boolean hasFlexibleX = CG.isBitSet(mask, CGFrame.FLEXIBLE_X);
                final boolean hasFlexibleY = CG.isBitSet(mask, CGFrame.FLEXIBLE_Y);

                if (hasFlexibleX) {
                    frame.x = nextLeft;
                }

                if (hasFlexibleY) {
                    if (isVCenter) {
                        frame.y = (getCGFrame().height - frame.height) / 2;
                    }
                    if (isTop) {
                        frame.y = 0;
                    }
                    if (isBottom) {
                        frame.y = getCGFrame().height - frame.height;
                    }
                }

                frame.x += drawable.getOrigin().getCGPoint().x;
                frame.y += drawable.getOrigin().getCGPoint().y;

                frame.x -= getContentOffset().getCGPoint().x;
                frame.y -= getContentOffset().getCGPoint().y;

                drawable.setOrigin(frame.x, frame.y);

                S.println("Will draw " + frame.x + ", " + frame.y + "; " + frame.width + ", " + frame.height);

                drawable.draw(graphics);
                nextLeft += drawable.intrinsicAwareFrame().width;
            }
        });
    }
    
    private void vDraw(Graphics g) {
        final Graphics graphics = g;

        this.nextLeft = getCGFrame().x;
        this.nextTop = getCGFrame().y;
           
        int alignmentInt = this.alignment.getInt();
        int contentHeight = updateContentSizeAndApplyMasksToChildren().height;

        if (CG.isBitSet(alignmentInt, CG.VCENTER)) {
            this.nextTop = (getCGFrame().height - contentHeight) / 2;
        }
        if (CG.isBitSet(alignmentInt, CG.TOP)) {
            this.nextTop = getCGFrame().y;
        }
        if (CG.isBitSet(alignmentInt, CG.BOTTOM)) {
            this.nextTop = getCGFrame().height - contentHeight;
        }

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
                
                if (hasFlexibleX) {
                    if (isHCenter) {
                        frame.x = (getCGFrame().width - frame.width) / 2;
                    }
                    if (isLeft) {
                        frame.x = 0;
                    }
                    if (isRight) {
                        frame.x = getCGFrame().width - frame.width;
                    }
                }

                if (hasFlexibleY) {
                    frame.y = nextTop;
                }
                
                frame.x += drawable.getOrigin().getCGPoint().x;
                frame.y += drawable.getOrigin().getCGPoint().y;

                frame.x -= getContentOffset().getCGPoint().x;
                frame.y -= getContentOffset().getCGPoint().y;

                S.println("Will draw " + frame.x + ", " + frame.y + "; " + frame.width + ", " + frame.height);

                drawable.setOrigin(frame.x, frame.y);
                
                drawable.draw(graphics);
                nextTop += drawable.intrinsicAwareFrame().height;
            }
        });

        S.println("=======");
    }

    private void zDraw(Graphics g) {
        final Graphics graphics = g;

        this.nextLeft = getCGFrame().x;
        this.nextTop = getCGFrame().y;

        for (int i = 0; i < this.drawables.getArray().length; i++) {
            CGDrawable object = (CGDrawable)this.drawables.getArray()[i];
            int mask = object.resizingMask().getInt();
            CGFrame frame = object.intrinsicAwareFrame();

            if (CG.isBitSet(mask, CGFrame.FLEXIBLE_WIDTH)) {
                frame.width = getCGFrame().width;
            }

            if (CG.isBitSet(mask,  CGFrame.FLEXIBLE_HEIGHT)) {
                frame.height = getCGFrame().height;
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
                        frame.y = (getCGFrame().height - frame.height) / 2;
                    }
                    if (isTop) {
                        frame.y = 0;
                    }
                    if (isBottom) {
                        frame.y = getCGFrame().height - frame.height;
                    }
                }

                if (hasFlexibleX) {
                    if (isHCenter) {
                        frame.x = (getCGFrame().width - frame.width) / 2;
                    }
                    if (isLeft) {
                        frame.x = 0;
                    }
                    if (isRight) {
                        frame.x = getCGFrame().width - frame.width;
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

        if (contentSize.height > getCGFrame().height) {
            S.println("contentSize.height > getCGFrame().height");
            int extent = contentSize.height - getCGFrame().height;

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

        if (contentSize.width > getCGFrame().width) {
            S.println("contentSize.width > getCGFrame().width");
            int extent = contentSize.width - getCGFrame().width;
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

        int thisWidth = this.getCGFrame().width;
        int thisHeight = this.getCGFrame().height;
        
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

        int defaultHeight = thisHeight;
        if (axis == AXIS_VERTICAL) {
             defaultHeight = unallocatedHeightCount > 0
                ? (thisHeight - contentHeight) / unallocatedHeightCount
                : 0;
        } else if (axis == AXIS_HORIZONTAL) {
            if (unallocatedHeightCount > 0 && contentHeight > 0) {
                defaultHeight = contentHeight;
            }
        }

        int defaultWidth = thisWidth;
        if (axis == AXIS_HORIZONTAL) {
             defaultWidth = unallocatedWidthCount > 0
                ? (thisWidth - contentWidth) / unallocatedWidthCount
                : 0;
        } else if (axis == AXIS_VERTICAL) {
            if (unallocatedWidthCount > 0 && contentWidth > 0) {
                defaultWidth = contentWidth;
            }
        }

        // TODO внутри контейнера конструировать фрейм отталкиваясь от интринсика, ориджина, ширины и высоты
        // а вот сама вьюха пусть рисует себя по тому фрейму, который ей даден

        for (int i = 0; i < drawables.length; i++) {
            CGDrawable object = (CGDrawable)drawables[i];
            int mask = object.resizingMask().getInt();
            CGFrame frame = object.intrinsicAwareFrame();

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

            object.width(frame.width);
            object.height(frame.height);

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
        
        CGSize size = new CGSize(contentWidth, contentHeight);
        this.contentSize.setCGSize(size);
        
        return size;
    }
}