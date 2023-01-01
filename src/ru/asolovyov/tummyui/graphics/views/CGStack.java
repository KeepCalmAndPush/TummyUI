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

    private int nextLeft = 0;
    private int nextTop = 0;
    
    protected Arr drawables = new Arr(new CGDrawable[]{});
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
                needsRedraw();
            }
        });
        return this;
    }

    public CGStack maxContentHeight(Int height) {
        this.maxContentHeightBinding = height;
        this.maxContentHeightBinding.sink(new Sink() {
            protected void onValue(Object value) {
                updateIntrinsicContentSize();
                needsRedraw();
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
                needsRedraw();
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

    protected Size contentSize = new Size(CG.NULL, CG.NULL);

    private List repeatedKeys = new List();
    private DispatchQueue keyRepeatQueue = new DispatchQueue(120);

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
                updateIntrinsicContentSize();
                needsRelayout(frame());
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
                needsRelayout(frame());
            }
        });

        //Trigger subscription
        this.spacing = spacing;
        this.maxContentWidthBinding = maxContentWidthBinding;
        this.maxContentHeightBinding = maxContentHeightBinding;
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
                drawable.getGeometryReader().read(drawable, frame());
            }
        }
    }

    public void needsRelayout(CGFrame frame) {
        super.needsRelayout(frame);
        this.pushFrameToChildren();
    }

    private void hDraw(Graphics g) {
        final Graphics graphics = g;

        final CGFrame thisFrame = frame();
        CGInsets contentInsets = contentInset();

        this.nextLeft = thisFrame.x + contentInsets.left;
        this.nextTop = thisFrame.y + contentInsets.top;

        int alignmentInt = this.alignment.getInt();
        int contentWidth = this.contentSize().getCGSize().width;

        if (CG.isBitSet(alignmentInt,CG.HCENTER)) {
            this.nextLeft = thisFrame.x + (thisFrame.width - contentWidth) / 2 + contentInsets.deltaX();
        } 
        if (CG.isBitSet(alignmentInt,CG.LEFT)) {
            this.nextLeft = thisFrame.x + contentInsets.deltaX();
        }
        if (CG.isBitSet(alignmentInt,CG.RIGHT)) {
            this.nextLeft = thisFrame.x + thisFrame.width - contentWidth + contentInsets.deltaX();
        }

        final boolean isVCenter = CG.isBitSet(alignmentInt, CG.VCENTER);
        final boolean isTop = CG.isBitSet(alignmentInt, CG.TOP);
        final boolean isBottom = CG.isBitSet(alignmentInt, CG.BOTTOM);

        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGDrawable child = (CGDrawable) element;
                CGFrame childFrame = child.intrinsicAwareFrame().copy();

                CGInsets contentInsets = contentInset();

                if (hasFlexibleX) {
                    childFrame.x = nextLeft;
                }

                if (hasFlexibleY) {
                    if (isVCenter) {
                        childFrame.y = thisFrame.y + (thisFrame.height - childFrame.height) / 2 + contentInsets.deltaY();
                    }
                    if (isTop) {
                        childFrame.y = thisFrame.y + contentInsets.deltaY();
                    }
                    if (isBottom) {
                        childFrame.y = thisFrame.y + thisFrame.height - childFrame.height + contentInsets.deltaY();
                    }
                }

//                childFrame.x += child.origin().getCGPoint().x;
//                childFrame.y += child.origin().getCGPoint().y;

                childFrame.x -= contentOffset().x;
                childFrame.y -= contentOffset().y;

                child.origin(childFrame.x, childFrame.y);

                S.println("HSTACK Will draw " + child + " " + childFrame.x + ", " + childFrame.y + "; " + childFrame.width + ", " + childFrame.height);

                child.draw(graphics);
                nextLeft += childFrame.width;
            }
        });
    }
    
    private void vDraw(Graphics g) {
        final Graphics graphics = g;

        final CGFrame thisFrame = frame().copy();
        final CGInsets contentInsets = contentInset();

        this.nextLeft = thisFrame.x + contentInsets.left;
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
                CGDrawable child = (CGDrawable)element;
                CGFrame childFrame = child.intrinsicAwareFrame();

                int mask = child.flexibility();
                final boolean hasFlexibleX = CG.isBitSet(mask, CG.FLEXIBLE_X);
                final boolean hasFlexibleY = CG.isBitSet(mask, CG.FLEXIBLE_Y);
                
                if (hasFlexibleX) {
                    if (isHCenter) {
                        childFrame.x = thisFrame.x + (thisFrame.width - childFrame.width) / 2 + contentInsets.deltaX();
                    }
                    if (isLeft) {
                        childFrame.x = thisFrame.x + contentInsets.deltaX();
                    }
                    if (isRight) {
                        childFrame.x = thisFrame.x + thisFrame.width - childFrame.width + contentInsets.deltaX();
                    }
                }

                if (hasFlexibleY) {
                    childFrame.y = nextTop;
                }
                
//                childFrame.x += child.origin().getCGPoint().x;
//                childFrame.y += child.origin().getCGPoint().y;

                childFrame.x -= contentOffset().x;
                childFrame.y -= contentOffset().y;

                S.println("VSTACK Will draw " + child + " " + childFrame.x + ", " + childFrame.y + "; " + childFrame.width + ", " + childFrame.height);

                child.origin(childFrame.x, childFrame.y);
                
                child.draw(graphics);

                nextTop += childFrame.height;
            }
        });

//        S.println("=======");
    }

    private void zDraw(Graphics g) {
        final Graphics graphics = g;

        final CGFrame thisFrame = frame();
        final CGInsets contentInsets = contentInset().getCGInsets();

        this.nextLeft = thisFrame.x + contentInsets.left;
        this.nextTop = thisFrame.y + contentInsets.top;

        for (int i = 0; i < this.drawables.getArray().length; i++) {
            CGDrawable object = (CGDrawable)this.drawables.getArray()[i];
            int mask = object.flexibility();
            CGFrame frame = object.intrinsicAwareFrame();

            if (CG.isBitSet(mask, CG.FLEXIBLE_WIDTH)) {
                frame.width = thisFrame.width;
            }

            if (CG.isBitSet(mask,  CG.FLEXIBLE_HEIGHT)) {
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

                int mask = drawable.flexibility();
                final boolean hasFlexibleX = CG.isBitSet(mask, CG.FLEXIBLE_X);
                final boolean hasFlexibleY = CG.isBitSet(mask, CG.FLEXIBLE_Y);

                if (hasFlexibleY) {
                    if (isVCenter) {
                        frame.y = (thisFrame.height - frame.height) / 2 + contentInsets.deltaY();
                    }
                    if (isTop) {
                        frame.y = 0 + contentInsets.deltaY();
                    }
                    if (isBottom) {
                        frame.y = thisFrame.height - frame.height + contentInsets.deltaY();
                    }
                }

                if (hasFlexibleX) {
                    if (isHCenter) {
                        frame.x = (thisFrame.width - frame.width) / 2 + contentInsets.deltaX();
                    }
                    if (isLeft) {
                        frame.x =  contentInsets.deltaX();
                    }
                    if (isRight) {
                        frame.x = thisFrame.width - frame.width + contentInsets.deltaX();
                    }
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
                CGDrawable drawable = (CGDrawable)element;
                drawable.canvas(CGStack.this.canvas());
            }
        });
        return this;
    }

    private void subscribeToKeyPressed() {
        CGCanvas canvas = this.canvas();
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

        CGFrame thisFrame = frame();

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

//    private List childrenWithFlexibility(int flexibility) {
//        List children = new List();
//        Object[] drawables = this.drawables.getArray();
//
//        for (int i = 0; i < drawables.length; i++) {
//            CGDrawable object = (CGDrawable)drawables[i];
//            int mask = object.flexibility();
//
//            if (CG.isBitSet(mask, flexibility)) {
//                children.addElement(object);
//            }
//        }
//
//        return children;
//    }
//
//    private int[] minWidthHeightMaxWidthHeight(Object[] drawables) {
//        int minWidth = 0;
//        int maxWidth = 0;
//        int minHeight = 0;
//        int maxHeight = 0;
//
//        for (int i = 0; i < drawables.length; i++) {
//            CGDrawable object = (CGDrawable)drawables[i];
//            if (minWidth > object.width()) {
//                minWidth = object.width();
//            }
//            if (maxWidth < object.width()) {
//                maxWidth = object.width();
//            }
//            if (minHeight > object.height()) {
//                minHeight = object.height();
//            }
//            if (maxHeight < object.height()) {
//                maxHeight = object.height();
//            }
//        }
//
//        return new int[]{minWidth, maxWidth, minHeight, maxHeight};
//    }

    /*
     * Если фрейм сайз НЕ задан
     *      1) Вычисляем интринсик
     *      2) Просетываем его во фрейм сайз
     *      3) Конец
     *
     * Если фрейм сайз задан
     *      1) Вычисляем интринсик
     *      2) Если он равен фрейму то конец
     *      3) Если фрейм флексибильный, то подтягиваем фрейм под инринсик
     *      4) Если все равно не равен, то
     *      - Если меньше, то сжимаем сжимаемых чилдов ПРОПОРЦИОНАЛЬНО!
     *      - Если больше, то растягиваем растягиваемых чилдов ПРОПОРЦИОНАЛЬНО!
     *
     *      5) Центрируем чидлы внутри стека
     */

    protected CGSize updateContentSizeAndApplyMasksToChildren() {
        /*
         * Сделать что-то типа максКонтентСайз
         * Пройтись по вьюхам, посчитать их суммарный контент сайз
         * Если он меньше/больше - ИТЕРАТИВНО и ПРОПОРЦИОНАЛЬНО расширить/уменбшить расширябельные вьюхи, так как у вьюхи может быть допуск для расширения 1 пиксель, а мы насчитаем что она пропорционально
         * может вырасти на 10. Поэтому, увеличили на сколько получилось, отняли увеличенное из общего запаса и далее с остатком запаса переходим к оставщимся вьюхам, пока не посетим все.
         * Вооот. Таким образом определили истинный контентСайзСтека
         * Если не задан ФРЕЙМ (или шир/выс) стека, то этот сайз и станет сайзом (или шир/выс) фрейма.
         *
         * Нужен массив допусков = [[50, inf], [0, 100], [0, 0] итп] первое - на уменьшение, второе - на увеличение.
         */
        int contentWidth = 0;
        int contentHeight = 0;

        int unallocatedWidthCount = 0;
        int unallocatedHeightCount = 0;

        int axis = axis().getInt();

        List shrinkableWidthChildren = new List();
        List growableWidthChildren = new List();

        List shrinkableHeightChildren = new List();
        List growableHeightChildren = new List();

        CGFrame thisFrame = frame();
        CGInsets contentInsets = contentInset();

        int availableWidth = thisFrame.width - contentInsets.left - contentInsets.right;
        int availableHeight = thisFrame.height - contentInsets.top - contentInsets.bottom;

        int maxWidth = 0;
        int maxHeight = 0;

        Object[] drawables = this.drawables.getArray();

        for (int i = 0; i < drawables.length; i++) {
            CGDrawable drawable = (CGDrawable)drawables[i];

            if (drawable.hasGrowableWidth()) {
                growableWidthChildren.addElement(drawable);
            }
            if (drawable.hasShrinkableWidth()) {
                shrinkableWidthChildren.addElement(drawable);
            }

            if (drawable.hasGrowableHeight()) {
                growableHeightChildren.addElement(drawable);
            }
            if (drawable.hasShrinkableHeight()) {
                shrinkableHeightChildren.addElement(drawable);
            }

            int childWidth = drawable.intrinsicAwareFrame().width;
            int childHeight = drawable.intrinsicAwareFrame().height;

            if (axis == AXIS_HORIZONTAL) {
                if (childWidth == 0 && drawable.hasGrowableWidth()) {
                    unallocatedWidthCount++;
                }
                contentWidth += childWidth;
            } else if (axis == AXIS_VERTICAL) {
                if (childHeight == 0 && drawable.hasGrowableHeight()) {
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
            CGFrame frame = drawable.intrinsicAwareFrame();

            if (frame.width == 0 && drawable.hasGrowableWidth()) {
                frame.width = defaultWidth;
            }

            if (frame.height == 0 && drawable.hasGrowableHeight()) {
                frame.height = defaultHeight;
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

        if (size.width > this.width() && this.hasGrowableWidth() ||
            size.width < this.width() && this.hasShrinkableWidth()) {
            this.width(size.width);
        }

        if (size.height > this.height() && this.hasGrowableHeight() ||
            size.height < this.height() && this.hasShrinkableHeight()) {
            this.height(size.height);
        }
        
        return size;
    }
    
    //TODO может тут надо будет учитывать офссеты дочерних вьюх
//    protected CGSize updateContentSizeAndApplyMasksToChildren() {
//        int contentWidth = 0;
//        int contentHeight = 0;
//
//        int unallocatedWidthCount = 0;
//        int unallocatedHeightCount = 0;
//
//        int axis = axis().getInt();
//
//        List dynamicWidthChildren = new List();
//        List dynamicHeightChildren = new List();
//
//        CGFrame thisFrame = frame();
//        CGInsets contentInsets = contentInset();
//
//        int availableWidth = thisFrame.width - contentInsets.left - contentInsets.right;
//        int availableHeight = thisFrame.height - contentInsets.top - contentInsets.bottom;
//
//        int maxWidth = 0;
//        int maxHeight = 0;
//
//        Object[] drawables = this.drawables.getArray();
//
//        for (int i = 0; i < drawables.length; i++) {
//            CGDrawable object = (CGDrawable)drawables[i];
//            int mask = object.flexibility();
//
//            boolean hasFlexibleWidth = CG.isBitSet(mask, CG.FLEXIBLE_WIDTH);
//            boolean hasFlexibleHeight = CG.isBitSet(mask, CG.FLEXIBLE_HEIGHT);
//
//            if (hasFlexibleWidth) {
//                dynamicWidthChildren.addElement(object);
//            }
//
//            if (hasFlexibleHeight) {
//                dynamicHeightChildren.addElement(object);
//            }
//
//            int childWidth = object.intrinsicAwareFrame().width;
//            int childHeight = object.intrinsicAwareFrame().height;
//
//            if (axis == AXIS_HORIZONTAL) {
//                if (childWidth == CG.NULL && hasFlexibleWidth) {
//                    unallocatedWidthCount++;
//                }
//                contentWidth += childWidth;
//            } else if (axis == AXIS_VERTICAL) {
//                if (childHeight == CG.NULL && hasFlexibleHeight) {
//                    unallocatedHeightCount++;
//                }
//                contentHeight += childHeight;
//            }
//        }
//
//        int defaultHeight = availableHeight;
//        if (axis == AXIS_VERTICAL) {
//             defaultHeight = unallocatedHeightCount > 0
//                ? (availableHeight - contentHeight) / unallocatedHeightCount
//                : 0;
//        } else if (axis == AXIS_HORIZONTAL) {
//            if (unallocatedHeightCount > 0 && contentHeight > 0) {
//                defaultHeight = contentHeight;
//            }
//        }
//
//        int defaultWidth = availableWidth;
//        if (axis == AXIS_HORIZONTAL) {
//             defaultWidth = unallocatedWidthCount > 0
//                ? (availableWidth - contentWidth) / unallocatedWidthCount
//                : 0;
//        } else if (axis == AXIS_VERTICAL) {
//            if (unallocatedWidthCount > 0 && contentWidth > 0) {
//                defaultWidth = contentWidth;
//            }
//        }
//
//        for (int i = 0; i < drawables.length; i++) {
//            CGDrawable drawable = (CGDrawable)drawables[i];
//            int mask = drawable.flexibility();
//            CGFrame frame = drawable.intrinsicAwareFrame();
//
//            if (axis == AXIS_VERTICAL) {
//                if (frame.width == CG.NULL && CG.isBitSet(mask, CG.FLEXIBLE_WIDTH)) {
//                    frame.width = defaultWidth;
//                }
//
//                if (frame.height == CG.NULL && CG.isBitSet(mask, CG.FLEXIBLE_HEIGHT)) {
//                    frame.height = defaultHeight;
//                }
//            }
//
//            if (axis == AXIS_HORIZONTAL) {
//                if (frame.width == CG.NULL && CG.isBitSet(mask, CG.FLEXIBLE_WIDTH)) {
//                    frame.width = defaultWidth;
//                }
//
//                if (frame.height == CG.NULL && CG.isBitSet(mask, CG.FLEXIBLE_HEIGHT)) {
//                    frame.height = defaultHeight;
//                }
//            }
//
//            drawable.width(frame.width);
//            drawable.height(frame.height);
//
//            maxWidth = Math.max(maxWidth, frame.width);
//            maxHeight = Math.max(maxHeight, frame.height);
//        }
//
//        if (axis == AXIS_VERTICAL) {
//            contentWidth = maxWidth;
//        } else {
//            contentWidth += defaultWidth * unallocatedWidthCount;
//        }
//
//        if (axis == AXIS_HORIZONTAL) {
//            contentHeight = maxHeight;
//        } else {
//            contentHeight += defaultHeight * unallocatedHeightCount;
//        }
//
//        contentWidth += (contentInsets.left + contentInsets.right);
//        contentHeight += (contentInsets.top + contentInsets.bottom);
//
//        CGSize size = new CGSize(contentWidth, contentHeight);
//        this.contentSize.setCGSize(size);
//
//        int mask = this.flexibility();
//        if (CG.isBitSet(mask, CG.FLEXIBLE_HEIGHT)) {
//            this.height(size.height);
//        }
//        if (CG.isBitSet(mask, CG.FLEXIBLE_WIDTH)) {
//            this.width(size.width);
//        }
//
//        return size;
//    }

    protected void updateIntrinsicContentSize() {
        super.updateIntrinsicContentSize();
        this.updateContentSizeAndApplyMasksToChildren();
        CGSize size = this.contentSize().getCGSize();
        this.intrinsicContentSizeBinding.setCGSize(size);
    }
}