/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import ru.asolovyov.combime.bindings.Obj;

/**
 *
 * @author Администратор
 */
public abstract class CGGeometryReader extends CGSomeDrawable {
//    public void onWidthChanged(int width){}
//    public void onHeightChanged(int width){}
//
//    public void needsRelayout(CGFrame frame) {
//        super.needsRelayout(frame);
//        if (frame.width != CGFrame.AUTOMATIC_DIMENSION) {
//            this.onWidthChanged(frame.width);
//        }
//        if (frame.height != CGFrame.AUTOMATIC_DIMENSION) {
//            this.onWidthChanged(frame.height);
//        }
//    }
    public abstract CGDrawable content(Obj frame);
}
