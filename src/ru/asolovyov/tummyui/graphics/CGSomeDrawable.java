/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.publishers.Publisher;
import ru.asolovyov.tummyui.bindings.Frame;
import ru.asolovyov.tummyui.bindings.Insets;
import ru.asolovyov.tummyui.bindings.Point;
import ru.asolovyov.tummyui.bindings.Size;

/**
 *
 * @author Администратор
 */
public abstract class CGSomeDrawable implements CGDrawable {
    public CGSomeDrawable() {
//        Publisher.combineLatest(new IPublisher[] {
//            frameBinding, widthBinding, heightBinding,
//            resizingMaskBinding, backgroundColor, strokeColor,
//            isVisible, offsetBinding, contentOffsetBinding,
//            contentInsetBinding, cornerRadiusBinding, intrinsicContentSizeBinding
//        }).sink(new Sink() {
//
//            protected void onValue(Object value) {
//                needsRedraw();
//            }
//
//        });
    }

    protected Frame frameBinding = new Frame(CGFrame.zero());
    
    protected Int widthBinding = new Int(0);
    protected Int heightBinding = new Int(0);
    protected Int resizingMaskBinding = new Int(CGFrame.FLEXIBLE_ALL);

    protected Int backgroundColor = new Int(-1);
    protected Int strokeColor = new Int(-1);
    protected Int strokeStyle;
    protected Bool isVisible = new Bool(true);

    protected Point offsetBinding = new Point(CGPoint.zero());
    protected Point contentOffsetBinding = new Point(CGPoint.zero());
    protected Insets contentInsetBinding = new Insets(CGInsets.zero());
    protected Size cornerRadiusBinding = new Size(CGSize.zero());
    
    protected Size intrinsicContentSizeBinding = new Size(CGSize.zero());

    private CGCanvas canvas;

    public void draw(Graphics g) {
        CGFrame frame = getCGFrame();
        if (frame == null) {
            return;
        }

        int backgroundColor = this.getBackgroundColor();
        if (backgroundColor != -1) {
            g.setColor(backgroundColor);
            g.fillRoundRect(
                frame.x,
                frame.y,
                frame.width,
                frame.height,
                getCornerRadius().width,
                getCornerRadius().height
                );
        }

        int strokeColor = this.getStrokeColor();
        if (strokeColor != -1) {
            g.setStrokeStyle(this.getStrokeStyle());
            g.setColor(strokeColor);

            g.drawRoundRect(
                frame.x,
                frame.y,
                frame.width,
                frame.height,
                getCornerRadius().width,
                getCornerRadius().height
                );
        }
    }

    public CGDrawable backgroundColor(int colorHex) {
        return this.backgroundColor(new Int(colorHex));
    }

    public CGDrawable setFrame(int x, int y, int width, int height) {
        return this.setFrame(new Frame(new CGFrame(x, y, width, height)));
    }

    public CGDrawable backgroundColor(Int backgroundColorHex) {
        this.backgroundColor = backgroundColorHex;
        this.backgroundColor.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }
    
    public CGDrawable setFrame(Frame frame) {
        this.frameBinding = frame;
        this.frameBinding.sink(new Sink() {
            protected void onValue(Object value) {
                intrinsicContentSizeBinding.setCGSize(frameBinding.getCGFrame().getCGSize());
                needsRelayout(getCGFrame());
            }
        });
        return this;
    }

    public CGDrawable setFrame(CGFrame frame) {
        this.frameBinding.setCGFrame(frame);
        return this;
    }

    public void needsRedraw() {
        this.needsRelayout(this.getCGFrame());
    }

    public void needsRelayout(CGFrame frame) {
        if (this.getCanvas() != null) {
            this.getCanvas().repaint(frame);
        }
    }

    public Frame getFrame() {
        return this.frameBinding;
    }

    public CGFrame getCGFrame() {
        return this.frameBinding.getCGFrame();
    }

    protected int getBackgroundColor() {
        return backgroundColor.getInt();
    }

    protected int getStrokeColor() {
        return strokeColor.getInt();
    }

    public CGDrawable setOffset(Point offset) {
        this.offsetBinding = offset;
        this.offsetBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });
        return this;
    }
    
    public CGDrawable width(Int width) {
        this.widthBinding = width;
        this.widthBinding.sink(new Sink() {
            protected void onValue(Object value) {
                width(widthBinding.getInt());
            }
        });
        return this;
    }

    public CGDrawable width(int width) {
        CGFrame frame = getCGFrame();
        frame.width = width;
        this.setFrame(frame);

        return this;
    }

    public CGDrawable height(Int height) {
        this.heightBinding = height;
        this.heightBinding.sink(new Sink() {
            protected void onValue(Object value) {
                height(heightBinding.getInt());
            }
        });
        return this;
    }

    public CGDrawable height(int height) {
        CGFrame frame = getCGFrame();
        frame.height = height;
        this.setFrame(frame);

        return this;
    }

    public Int resizingMask() {
        return this.resizingMaskBinding;
    }

    public CGDrawable resizingMask(Int mask) {
        this.resizingMaskBinding = mask;
        this.resizingMaskBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });

        return this;
    }

    public CGDrawable resizingMask(int mask) {
        this.resizingMaskBinding.setInt(mask);
        return this;
    }

    public CGDrawable isVisible(boolean isVisible) {
        return this.isVisible(new Bool(isVisible));
    }

    public CGDrawable isVisible(Bool isVisible) {
        this.isVisible = isVisible;
        this.isVisible.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                needsRelayout(getCGFrame());
            }
        });
        return this;
    }

    public Bool isVisible() {
        return this.isVisible;
    }

    public CGDrawable setCanvas(CGCanvas canvas) {
        this.canvas = canvas;
        this.startHandlingKeyboard();
        canvas.needsRepaint().setBool(true);
        return this;
    }
    
    public CGCanvas getCanvas() {
        return canvas;
    }

    public CGDrawable sizeToFit() {
        return this;
    }

    public Size intrinsicContentSize() {
        return this.intrinsicContentSizeBinding;
    }

    private GeometryReader geometryReader;
    public CGDrawable readGeometry(GeometryReader reader) {
        this.geometryReader = reader;
        return this;
    }
    
    public GeometryReader getGeometryReader() {
        return geometryReader;
    }

    private KeyboardHandler keyboardHandler;
    public CGDrawable handleKeyboard(KeyboardHandler handler) {
        this.keyboardHandler = handler;
        this.startHandlingKeyboard();
        return this;
    }
    
    public KeyboardHandler getKeyboardHandler() {
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

    public CGDrawable setOffset(int x, int y) {
        this.offsetBinding.setCGPoint(new CGPoint(x, y));
        return this;
    }

    public Point getOffset() {
        return this.offsetBinding;
    }

    public CGDrawable setContentOffset(Point offset) {
        offset.route(this.contentOffsetBinding);
        return this;
    }

    public CGDrawable setContentOffset(int x, int y) {
        this.contentOffsetBinding.setCGPoint(new CGPoint(x, y));
        return this;
    }

    public CGDrawable setContentInset(Insets inset) {
        inset.route(this.contentInsetBinding);
        return this;
    }

    public CGDrawable setContentInset(int top, int left, int bottom, int right) {
        this.contentInsetBinding.setCGInsets(new CGInsets(top, left, bottom, right));
        return this;
    }

    public Point getContentOffset() {
        return this.contentOffsetBinding;
    }

    public Insets getContentInset() {
        return this.contentInsetBinding;
    }

    public CGDrawable stroke(int strokeStyle) {
        return this.stroke(new Int(strokeStyle));
    }

    public CGDrawable stroke(Int strokeStyle) {
        this.strokeStyle = strokeStyle;
        this.strokeStyle.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }
    
    protected int getStrokeStyle() {
        if (strokeStyle != null) {
           return strokeStyle.getInt();
        }
        return Graphics.SOLID;
    }

    public CGDrawable cornerRaduis(CGSize cornerRadius) {
        return this.cornerRaduis(new Size(cornerRadius));
    }

    public CGDrawable cornerRaduis(Size cornerRadiusBinding) {
        this.cornerRadiusBinding = cornerRadiusBinding;
        this.cornerRadiusBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGSize getCornerRadius() {
        if (this.cornerRadiusBinding != null) {
            return this.cornerRadiusBinding.getCGSize();
        }
        return CGSize.zero();
    }

    public CGDrawable strokeColor(int strokeColorHex) {
        return this.strokeColor(new Int(strokeColorHex));
    }

    public CGDrawable strokeColor(Int strokeColorHex) {
        this.strokeColor = strokeColorHex;
        this.strokeColor.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }
}