/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
final class CGFrame {
    final static int AUTOMATIC_DIMENSION = -1;

    public static CGFrame zero = new CGFrame();

    int x = 0,
        y = 0,
        width = 0,
        height = 0;
}

final class CGSize {
    public static CGSize zero = new CGSize();
    int width = 0, height = 0;
}

public class CGCanvas extends Canvas {

    public void repaint(CGFrame frame) {
        if (frame == null) {
            this.repaint();
            return;
        }
        this.repaint(frame.x, frame.y, frame.width, frame.height);
    }

    protected void paint(Graphics g) {
        /*
         * arc(+fill), image, line(+dashed/dotted etc), rect(+rounded, fill), string(+substring)
         */
Font f;
        //color-int, font(face 3, style 3, size 3), stroke-style(solid/dotted)

    }

}

interface CGDrawable {
    void draw(Graphics g);

    void needsRedraw();
    void needsRelayout();

    CGDrawable color(int colorHex);
    CGDrawable color(IntBinding colorHex);

    CGDrawable frame(CGFrame frame);
    CGDrawable frame(ObjectBinding frame);

    CGDrawable canvas(CGCanvas canvas);
}

interface CGStrokable extends CGDrawable {
    CGStrokable stroke(int strokeStyle);
    CGStrokable stroke(IntBinding strokeStyle);
}

interface CGFontSupporting extends CGDrawable {
    CGFontSupporting font(Font font);
    CGFontSupporting font(ObjectBinding font);
}

abstract class CGSomeDrawable implements CGDrawable {
    protected IntBinding color;
    protected ObjectBinding frameBinding;
    
    protected CGCanvas canvas;

    public void draw(Graphics g) {
        g.setColor(this.getColor());
    }

    public CGDrawable color(int colorHex) {
        return this.color(Binding.Int(colorHex));
    }

    public CGDrawable color(IntBinding colorHex) {
        this.color = colorHex;
        this.color.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public CGDrawable frame(CGFrame frame) {
        return this.frame(Binding.Object(frame));
    }

    public CGDrawable frame(ObjectBinding frame) {
        this.frameBinding = frame;
        return this;
    }

    public void needsRedraw() {
        canvas.repaint(getFrame());
    }

    public void needsRelayout() {
        
    }

    public CGDrawable canvas(CGCanvas canvas) {
        this.canvas = canvas;
        return this;
    }

    protected CGFrame getFrame() {
        if (frameBinding != null) {
           return (CGFrame) frameBinding.getObject();
        }
        return null;
    }

    protected int getColor() {
        if (color != null) {
           return color.getInt();
        }
        return 0x00000000;
    }
}
abstract class CGSomeStrokable extends CGSomeDrawable {
    protected IntBinding strokeStyle;
    
    public CGSomeStrokable stroke(int strokeStyle) {
        return this.stroke(Binding.Int(strokeStyle));
    }

    public CGSomeStrokable stroke(IntBinding strokeStyle) {
        this.strokeStyle = strokeStyle;
        this.strokeStyle.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    public void draw(Graphics g) {
        super.draw(g);
        g.setStrokeStyle(getStrokeStyle());
    }

    protected int getStrokeStyle() {
        if (strokeStyle != null) {
           return strokeStyle.getInt();
        }
        return Graphics.SOLID;
    }
}

class CGRect extends CGSomeStrokable {
    private ObjectBinding cornerRadiusBinding;

    public CGRect cornerRaduis(CGSize cornerRadius) {
        return this.cornerRaduis(Binding.Object(cornerRadius));
    }

    public CGRect cornerRaduis(ObjectBinding cornerRadiusBinding) {
        this.cornerRadiusBinding = cornerRadiusBinding;
        this.cornerRadiusBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    protected CGSize getCornerRadius() {
        if (this.cornerRadiusBinding != null) {
            return (CGSize)this.cornerRadiusBinding.getObject();
        }
        return new CGSize();
    }

    public void draw(Graphics g) {
        super.draw(g);
        
        CGFrame frame = getFrame();
        if (frame == null) {
            return;
        }

        g.fillRoundRect(
                frame.x,
                frame.y,
                frame.width,
                frame.height,
                getCornerRadius().width,
                getCornerRadius().height
                );
    }
}

class HStack extends CGSomeDrawable {

}

class VStack extends CGSomeDrawable {

}

class ZStack extends CGSomeDrawable {
    
}

class CGIf extends CGSomeDrawable {

}

class CGForEach extends CGSomeDrawable {

}

//arc(+fill), image, line(+dashed/dotted etc), rect(+rounded, fill), string(+substring)

class CGArc extends CGSomeStrokable {
    private ObjectBinding cornerRadiusBinding;

    public CGArc cornerRaduis(CGSize cornerRadius) {
        return this.cornerRaduis(Binding.Object(cornerRadius));
    }

    public CGArc cornerRaduis(ObjectBinding cornerRadiusBinding) {
        this.cornerRadiusBinding = cornerRadiusBinding;
        this.cornerRadiusBinding.sink(new Sink() {
            protected void onValue(Object value) {
                needsRedraw();
            }
        });
        return this;
    }

    protected CGSize getCornerRadius() {
        if (this.cornerRadiusBinding != null) {
            return (CGSize)this.cornerRadiusBinding.getObject();
        }
        return new CGSize();
    }

    public void draw(Graphics g) {
        super.draw(g);

        CGFrame frame = getFrame();
        if (frame == null) {
            return;
        }

        g.fillArc(
                frame.x,
                frame.y,
                frame.width,
                frame.height,
                getCornerRadius().width,
                getCornerRadius().height
                );
    }
}