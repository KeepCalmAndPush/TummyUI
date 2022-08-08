/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import javax.microedition.lcdui.Graphics;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class CGIf extends CGSomeDrawable {
    private Bool conditionBinding;
    private CGDrawable ifItem;
    private CGDrawable elseItem;

    public CGIf(Bool condition, CGDrawable ifItem, CGDrawable elseItem) {
        super();
        this.conditionBinding = condition;
        this.ifItem = ifItem;
        this.elseItem = elseItem;

        this.conditionBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                if (getCanvas() != null) {
                    getCanvas().needsRepaint().setBool(true);
                }
                //TODO вычислить рект, описывающий старый и новый фоейм, и перерисовывать именно его
            }
        });
    }

    public CGDrawable canvas(CGCanvas canvas) {
        this.ifItem.setCanvas(canvas);
        this.elseItem.setCanvas(canvas);
        
        return super.setCanvas(canvas);
    }


    public void draw(Graphics g) {
        super.draw(g);
        if(this.conditionBinding.getBool()) {
            this.ifItem.draw(g);
        } else {
            this.elseItem.draw(g);
        }
    }
}