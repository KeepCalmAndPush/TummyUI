/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

/**
 *
 * @author Администратор
 */
public class CGStack extends CGSomeDrawable {
    protected CGDrawable drawables[];
    public CGStack(CGDrawable drawables[]) {
        super();
        this.drawables = drawables;
    }
}

class HStack extends CGStack{
    public HStack(CGDrawable drawables[]) {
        super(drawables);
    }
}

class VStack extends CGStack {
    public VStack(CGDrawable drawables[]) {
        super(drawables);
    }
}

class ZStack extends CGStack {
    public ZStack(CGDrawable drawables[]) {
        super(drawables);
    }
}
