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
import ru.asolovyov.tummyui.graphics.CG;
import ru.asolovyov.tummyui.graphics.CGDisplayLink;
import ru.asolovyov.tummyui.graphics.CGFrame;
import ru.asolovyov.tummyui.graphics.views.CGDrawable.KeyboardHandler;

/**
 *
 * @author Администратор
 */

public class CGCanvas extends Canvas {
    private CGDrawable[] content = new CGDrawable[]{};
    private Int backgroundColor = new Int(CG.NULL);
    private Bool needsRepaint = new Bool(false);

    public void setNeedsRepaint() {
        S.println("CANVAS SET NEEDS REPAINT!");
        this.needsRepaint.setBool(true);
    }

    private Int keyPressed = (Int) new Int(0).next();
    private Int keyReleased = (Int) new Int(0).next();
    private Int keyRepeated = (Int) new Int(0).next();

    private KeyboardHandler keyboardHandler = null;

    public CGCanvas(CGDrawable content) {
        this(new CGDrawable[] { content });
    }

    public CGCanvas(CGDrawable[] content) {
        super();
        
        this.setDrawables(content);
        
        S.println("CANVAS HAS " + content.length + " CHILDren");

        this.backgroundColor.sink(new Sink() {
            protected void onValue(Object value) {
                repaint();
            }
        });
        
        this.needsRepaint.zip(CGDisplayLink.ticks).sink(new Sink() {
            protected void onValue(Object value) {
                S.println("CANVAS REPAINT!!!");
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
        S.println("1 CANVAS");
        for (int i = 0; i < this.content.length; i++) {
            S.println("2 CANVAS");
            CGDrawable drawable = content[i];
            drawable.canvas(null);
        }

        this.content = content;

        S.println("3 CANVAS");

        for (int i = 0; i < this.content.length; i++) {
            S.println("4 CANVAS");
            CGDrawable drawable = content[i];
            drawable.canvas(this);
        }

        if (content.length == 1) {
            S.println("5 CANVAS");
            
            CGSomeDrawable child = (CGSomeDrawable) content[0];

            int widthToSet = Math.min(this.getWidth(), child.maxWidth());
            int heightToSet = Math.min(this.getHeight(), child.maxHeight());

            S.println("CANVAS WILL SET CHILD Width: " + widthToSet + ", Height: " + heightToSet);
            S.println(child + " w: {" + child.minWidth() + "-" + child.maxWidth() + "}; {" + child.minHeight() + "-" + child.maxHeight() + "}");

            child.width(widthToSet);
            child.height(heightToSet);
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
        backgroundColorHex.route(this.backgroundColor);
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
