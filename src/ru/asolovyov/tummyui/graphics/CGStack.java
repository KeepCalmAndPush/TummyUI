/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;

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

    protected Arr drawables;
    protected Int alignment;
    protected Int axis;

    public CGStack(Int axis, Arr drawables) {
        this(axis, new Int(CG.ALIGNMENT_H_CENTER | CG.ALIGNMENT_V_CENTER), drawables);
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

        S.println("public void draw(Graphics g) {");

        if (this.axis.getInt() == AXIS_HORIZONTAL) {
            this.hDraw(g);
        } else if (this.axis.getInt() == AXIS_VERTICAL) {
            this.vDraw(g);
        } else if (this.axis.getInt() == AXIS_Z) {
            this.zDraw(g);
        }
    }

    private void pushFrameToChildren() {
        CGDrawable[] drawables = (CGDrawable[]) this.drawables.getArray();
        for (int i = 0; i < drawables.length; i++) {
            CGDrawable drawable = drawables[i];
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
        
        int unallocatedWidthCount = 0;
        int contentWidth = 0;
        for (int i = 0; i < this.drawables.getArray().length; i++) {
            CGDrawable object = (CGDrawable)this.drawables.getArray()[i];
            int width = object.getCGFrame().width;
            if (width != CGFrame.AUTOMATIC_DIMENSION) {
                contentWidth += width;
            } else {
                unallocatedWidthCount++;
            }
        }

        final int defaultWidth = unallocatedWidthCount > 0
                ? (getCGFrame().width - contentWidth) / unallocatedWidthCount
                : 0;

        for (int i = 0; i < this.drawables.getArray().length; i++) {
            CGDrawable object = (CGDrawable)this.drawables.getArray()[i];
            CGFrame frame = object.getCGFrame();

            if (frame.height == CGFrame.AUTOMATIC_DIMENSION) {
                frame.height = getCGFrame().height;
            }
            if (frame.width == CGFrame.AUTOMATIC_DIMENSION) {
                frame.width = defaultWidth;
            }
        }

        contentWidth += defaultWidth * unallocatedWidthCount;

        this.nextLeft += getCGFrame().x;

        int alignment = this.alignment.getInt();

        if ((alignment & CG.ALIGNMENT_H_CENTER) == CG.ALIGNMENT_H_CENTER) {
            this.nextLeft = (getCGFrame().width - contentWidth) / 2;
        } 
        if ((alignment & CG.ALIGNMENT_LEFT) == CG.ALIGNMENT_LEFT) {
            this.nextLeft = getCGFrame().x;
        }
        if ((alignment & CG.ALIGNMENT_RIGHT) == CG.ALIGNMENT_RIGHT) {
            this.nextLeft = getCGFrame().width - contentWidth;
        }

        final boolean isVCenter = (alignment & CG.ALIGNMENT_V_CENTER) == CG.ALIGNMENT_V_CENTER;
        final boolean isTop = (alignment & CG.ALIGNMENT_TOP) == CG.ALIGNMENT_TOP;
        final boolean isBottom = (alignment & CG.ALIGNMENT_BOTTOM) == CG.ALIGNMENT_BOTTOM;

        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGDrawable drawable = (CGDrawable) element;
                CGFrame frame = drawable.getCGFrame();
                frame.x = nextLeft;

                if (isVCenter) {
                    frame.y = (getCGFrame().height - frame.height) / 2;
                }
                if (isTop) {
                    frame.y = 0;
                }
                if (isBottom) {
                    frame.y = getCGFrame().height - frame.height;
                }

                frame.x += drawable.getOffset().getCGPoint().x;
                frame.y += drawable.getOffset().getCGPoint().y;

                drawable.draw(graphics);
                nextLeft += drawable.getCGFrame().width;
            }
        });
    }
    
    private void vDraw(Graphics g) {
        final Graphics graphics = g;

        this.nextLeft = getCGFrame().x;
        this.nextTop = getCGFrame().y;

        int unallocatedHeightCount = 0;
        int contentHeight = 0;
        for (int i = 0; i < this.drawables.getArray().length; i++) {
            CGDrawable object = (CGDrawable)this.drawables.getArray()[i];
            int height = object.getCGFrame().height;
            if (height != CGFrame.AUTOMATIC_DIMENSION) {
                contentHeight += object.getCGFrame().height;
            } else {
                unallocatedHeightCount++;
            }
        }
        
        final int defaultHeight = unallocatedHeightCount > 0
                ? (getCGFrame().height - contentHeight) / unallocatedHeightCount
                : 0;
        
        for (int i = 0; i < this.drawables.getArray().length; i++) {
            CGDrawable object = (CGDrawable)this.drawables.getArray()[i];
            int mask = object.resizingMask().getInt();
            CGFrame frame = object.getCGFrame();

            if ((mask & CGFrame.FLEXIBLE_WIDTH) == CGFrame.FLEXIBLE_WIDTH) {
                frame.width = getCGFrame().width;
            }

            if ((mask & CGFrame.FLEXIBLE_HEIGHT) == CGFrame.FLEXIBLE_HEIGHT) {
                frame.height = defaultHeight;
            }
        }

        contentHeight += defaultHeight * unallocatedHeightCount;
           
        int alignment = this.alignment.getInt();
        if ((alignment & CG.ALIGNMENT_V_CENTER) == CG.ALIGNMENT_V_CENTER) {
            this.nextTop = (getCGFrame().height - contentHeight) / 2;
        }
        if ((alignment & CG.ALIGNMENT_TOP) == CG.ALIGNMENT_TOP) {
            this.nextTop = getCGFrame().y;
        }
        if ((alignment & CG.ALIGNMENT_BOTTOM) == CG.ALIGNMENT_BOTTOM) {
            this.nextTop = getCGFrame().height - contentHeight;
        }

        final boolean isHCenter = (alignment & CG.ALIGNMENT_H_CENTER) == CG.ALIGNMENT_H_CENTER;
        final boolean isLeft = (alignment & CG.ALIGNMENT_LEFT) == CG.ALIGNMENT_LEFT;
        final boolean isRight = (alignment & CG.ALIGNMENT_RIGHT) == CG.ALIGNMENT_RIGHT;
        
        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGDrawable drawable = (CGDrawable)element;
                CGFrame frame = drawable.getCGFrame();
                frame.y = nextTop;

                if (isHCenter) {
                    frame.x = (getCGFrame().width - frame.width) / 2;
                }
                if (isLeft) {
                    frame.x = 0;
                }
                if (isRight) {
                    frame.x = getCGFrame().width - frame.width;
                }

                frame.x += drawable.getOffset().getCGPoint().x;
                frame.y += drawable.getOffset().getCGPoint().y;
                
                drawable.draw(graphics);
                nextTop += drawable.getCGFrame().height;
            }
        });
    }

    private void zDraw(Graphics g) {
        final Graphics graphics = g;

        this.nextLeft = getCGFrame().x;
        this.nextTop = getCGFrame().y;

        for (int i = 0; i < this.drawables.getArray().length; i++) {
            CGDrawable object = (CGDrawable)this.drawables.getArray()[i];
            int mask = object.resizingMask().getInt();
            CGFrame frame = object.getCGFrame();

            if ((mask & CGFrame.FLEXIBLE_WIDTH) == CGFrame.FLEXIBLE_WIDTH) {
                frame.width = getCGFrame().width;
            }

            if ((mask & CGFrame.FLEXIBLE_HEIGHT) == CGFrame.FLEXIBLE_HEIGHT) {
                frame.height = getCGFrame().height;
            }
        }

        int alignment = this.alignment.getInt();
        
        final boolean isVCenter = (alignment & CG.ALIGNMENT_V_CENTER) == CG.ALIGNMENT_V_CENTER;
        final boolean isTop = (alignment & CG.ALIGNMENT_TOP) == CG.ALIGNMENT_TOP;
        final boolean isBottom = (alignment & CG.ALIGNMENT_BOTTOM) == CG.ALIGNMENT_BOTTOM;

        final boolean isHCenter = (alignment & CG.ALIGNMENT_H_CENTER) == CG.ALIGNMENT_H_CENTER;
        final boolean isLeft = (alignment & CG.ALIGNMENT_LEFT) == CG.ALIGNMENT_LEFT;
        final boolean isRight = (alignment & CG.ALIGNMENT_RIGHT) == CG.ALIGNMENT_RIGHT;

        this.drawables.forEach(new Arr.Enumerator() {
            public void onElement(Object element) {
                CGDrawable drawable = (CGDrawable)element;
                CGFrame frame = drawable.getCGFrame();

                if (isVCenter) {
                    frame.y = (getCGFrame().height - frame.height) / 2;
                }
                if (isTop) {
                    frame.y = 0;
                }
                if (isBottom) {
                    frame.y = getCGFrame().height - frame.height;
                }

                if (isHCenter) {
                    frame.x = (getCGFrame().width - frame.width) / 2;
                }
                if (isLeft) {
                    frame.x = 0;
                }
                if (isRight) {
                    frame.x = getCGFrame().width - frame.width;
                }

                frame.x += drawable.getOffset().getCGPoint().x;
                frame.y += drawable.getOffset().getCGPoint().y;

                drawable.draw(graphics);
            }
        });
    }
}