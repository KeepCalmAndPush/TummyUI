/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author Администратор
 */

public abstract class CGCanvas extends Canvas {
    
    public abstract CGDrawable drawable();

    public void repaint(CGFrame frame) {
        if (frame == null) {
            this.repaint();
            return;
        }
        this.repaint(frame.x, frame.y, frame.width, frame.height);
    }

    protected void paint(Graphics g) {
        this.drawable().draw(g);
    }
}

class Stack extends CGSomeDrawable {
    protected CGDrawable drawables[];
    public Stack(CGDrawable drawables[]) {
        super();
        this.drawables = drawables;
    }
}

class HStack extends Stack{
    public HStack(CGDrawable drawables[]) {
        super(drawables);
    }
}

class VStack extends Stack {
    public VStack(CGDrawable drawables[]) {
        super(drawables);
    }
}

class ZStack extends Stack {
    public ZStack(CGDrawable drawables[]) {
        super(drawables);
    }
}

class CGIf extends CGSomeDrawable {

}

class CGForEach extends CGSomeDrawable {

}

