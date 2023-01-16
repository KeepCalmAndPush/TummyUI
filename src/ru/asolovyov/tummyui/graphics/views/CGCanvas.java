/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.operators.sequence.Drop;
import ru.asolovyov.combime.operators.timing.Debounce;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.views.CGDrawable.KeyboardHandler;

/**
 *
 * @author Администратор
 */

public class CGCanvas extends Canvas {
    private CGDrawable[] content = new CGDrawable[]{};
    private Int backgroundColor;
    private Bool needsRepaint = new Bool(false);

    public void setNeedsRepaint() {
        this.needsRepaint.setBool(true);
    }

    private Int keyPressed = (Int) new Int(null).to(new Drop(1));
    private Int keyReleased = (Int) new Int(null).to(new Drop(1));
    private Int keyRepeated = (Int) new Int(null).to(new Drop(1));

    private KeyboardHandler keyboardHandler = null;

    public CGCanvas(CGDrawable content) {
        this(new CGDrawable[] { content });
    }

    public CGCanvas(CGDrawable[] content) {
        super();
        
        this.setDrawables(content);
        
        S.debugln("CANVAS HAS " + content.length + " CHILDren");
        
        this.needsRepaint.to(new Debounce(33)).sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });

        this.keyPressed.throttle(100).sink(new Sink() {
            protected void onValue(Object value) {
                KeyboardHandler handler = getKeyboardHandler();
                if (handler == null) {
                    return;
                }
                handler.keyPressed(null, ((Integer)value).intValue());
            }
        });

        this.keyReleased.throttle(100).sink(new Sink() {
            protected void onValue(Object value) {
                KeyboardHandler handler = getKeyboardHandler();
                if (handler == null) {
                    return;
                }
                handler.keyReleased(null, ((Integer)value).intValue());
            }
        });

        this.keyRepeated.throttle(100).sink(new Sink() {
            protected void onValue(Object value) {
                KeyboardHandler handler = getKeyboardHandler();
                if (handler == null) {
                    return;
                }
                handler.keyRepeated(null, ((Integer)value).intValue());
            }
        });
    }

    //TODO ОТРЕФАЧИТЬ ИМПЕРАТИВЩИНУ
    public void setDrawable(CGDrawable content) {
        this.setDrawables(new CGDrawable[]{ content });
    }

    public void setDrawables(CGDrawable[] content) {
        for (int i = 0; i < this.content.length; i++) {
            CGDrawable drawable = content[i];
            drawable.canvas(null);
        }

        this.content = content;

        for (int i = 0; i < this.content.length; i++) {
            CGDrawable drawable = content[i];
            drawable.canvas(this);
        }

        if (content.length == 1) {
            CGSomeDrawable child = (CGSomeDrawable) content[0];

            int widthToSet = Math.min(this.getWidth(), child.maxWidth());
            int heightToSet = Math.min(this.getHeight(), child.maxHeight());

            S.debugln("CANVAS WILL SET CHILD Width: " + widthToSet + ", Height: " + heightToSet);
            S.debugln(child + " w: {" + child.minWidth() + "-" + child.maxWidth() + "}; {" + child.minHeight() + "-" + child.maxHeight() + "}");

            child.widthBinding.sendValue(new Int(widthToSet));
            child.heightBinding.sendValue(new Int(heightToSet));
        }

        this.setNeedsRepaint();
    }

    public void repaint(CGFrame frame) {
        if (frame == null) {
            this.repaint();
            return;
        }
        this.repaint(frame.x, frame.y, frame.width, frame.height);
    }

    protected void paint(Graphics g) {
        g.setColor(this.backgroundColor.getInt());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (int i = 0; i < this.content.length; i++) {
            CGDrawable drawable = this.content[i];
            drawable.draw(g);
        }
    }

    public CGCanvas backgroundColor(int colorHex) {
        return this.backgroundColor(new Int(colorHex));
    }

    public CGCanvas backgroundColor(Int backgroundColorHex) {
        this.backgroundColor = backgroundColorHex;
        this.backgroundColor.sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });
        return this;
    }

    protected void keyPressed(int keyCode) {
        super.keyPressed(keyCode);
        this.getKeyPressed().setInt(keyCode);
    }

    protected void keyReleased(int keyCode) {
        super.keyReleased(keyCode);
        this.getKeyReleased().setInt(keyCode);
    }

    protected void keyRepeated(int keyCode) {
        super.keyRepeated(keyCode);
        this.getKeyRepeated().setInt(keyCode);
    }
    
    public Int getKeyPressed() {
        return keyPressed;
    }

    public Int getKeyReleased() {
        return keyReleased;
    }

    public Int getKeyRepeated() {
        return keyRepeated;
    }
    
    private KeyboardHandler getKeyboardHandler() {
        return keyboardHandler;
    }

    public CGCanvas handleKeyboard(KeyboardHandler handler) {
        this.keyboardHandler = handler;
        return this;
    }
}
