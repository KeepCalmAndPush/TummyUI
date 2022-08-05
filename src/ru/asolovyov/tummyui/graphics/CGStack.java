/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

/**
 *
 * @author Администратор
 */
public abstract class CGStack extends CGSomeDrawable {
    public final static int AXIS_HORIZONTAL = 0;
    public final static int AXIS_VERTICAL = 1;
    public final static int AXIS_Z = 2;

    protected CGDrawable drawables[];
    public CGStack(CGDrawable drawables[]) {
        super();
        this.drawables = drawables;
    }

    public abstract int axis();
}

class HStack extends CGStack{
    public HStack(CGDrawable drawables[]) {
        super(drawables);
    }

    public int axis() {
        return AXIS_HORIZONTAL;
    }
}

class VStack extends CGStack {
    public VStack(CGDrawable drawables[]) {
        super(drawables);
    }
    
    public int axis() {
        return AXIS_VERTICAL;
    }
}

class ZStack extends CGStack {
    public ZStack(CGDrawable drawables[]) {
        super(drawables);
    }

    public int axis() {
        return AXIS_Z;
    }
}
