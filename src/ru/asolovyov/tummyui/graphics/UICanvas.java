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

/**
 *
 * @author Администратор
 */
public class UICanvas extends Canvas {

    protected void paint(Graphics g) {
        /*
         * arc(+fill), image, line(+dashed/dotted etc), rect(+rounded, fill), string(+substring)
         */
Font f;
        //color-int, font(face 3, style 3, size 3), stroke-style(solid/dotted)

    }

}

class UIFrame {
    final static int AUTOMATIC_DIMENSION = 0;

    int x = 0,
        y = 0,
        width = AUTOMATIC_DIMENSION,
        height= AUTOMATIC_DIMENSION;
}

interface UIDrawable {
    void draw(Graphics g);
    void needsRedraw();

    UIDrawable color(int colorHex);
    UIDrawable color(IntBinding colorHex);

    UIDrawable frame(UIFrame frame);
    UIDrawable frame(ObjectBinding frame);

    UIDrawable canvas(Canvas canvas);
}

interface UIStrokable extends UIDrawable {
    UIStrokable stroke(int strokeStyle);
    UIStrokable stroke(IntBinding strokeStyle);
}

interface UIFontSupporting extends UIDrawable {
    UIFontSupporting font(Font font);
    UIFontSupporting font(ObjectBinding font);
}


// fа нужно ли мне такое разделение интерфейсов? ДА!
class UISomeDrawable implements UIDrawable {
    protected IntBinding color;
    protected ObjectBinding frame;
    
    private Canvas canvas;
    
    public void draw(Graphics g) {
    }

    public UIDrawable color(int colorHex) {
        return this.color(Binding.Int(colorHex));
    }

    public UIDrawable color(IntBinding colorHex) {
        this.color = colorHex;
        return this;
    }

    public UIDrawable frame(UIFrame frame) {
        return this.frame(Binding.Object(frame));
    }

    public UIDrawable frame(ObjectBinding frame) {
        this.frame = frame;
        return this;
    }

    public void needsRedraw() {
    }

    public UIDrawable canvas(Canvas canvas) {
        this.canvas = canvas;
        return this;
    }
}

// fа нужно ли мне такое разделение интерфейсов? ДА!

class UIRect extends UISomeDrawable implements UIStrokable {
    public void draw(Graphics g) {
    }

    public UIStrokable stroke(int strokeStyle) {
        return this;
    }

    public UIStrokable stroke(IntBinding strokeStyle) {
        return this;
    }

}

class HStack extends UISomeDrawable {

}

class VStack extends UISomeDrawable {

}

class ZStack extends UISomeDrawable {
    
}