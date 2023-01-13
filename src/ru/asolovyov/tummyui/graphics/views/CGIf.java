/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

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
                if (canvas() != null) {
                    canvas().setNeedsRepaint();
                }
                //TODO вычислить рект, описывающий старый и новый фоейм, и перерисовывать именно его
            }
        });
    }

    public CGDrawable canvas(CGCanvas canvas) {
        this.ifItem.canvas(canvas);
        this.elseItem.canvas(canvas);
        
        return super.canvas(canvas);
    }

    //TODO Хитрая обработка инсетов и офсетов
    public void draw(Graphics g) {
        super.draw(g);
        if(this.conditionBinding.getBoolean()) {
            this.ifItem.draw(g);
        } else {
            this.elseItem.draw(g);
        }
    }
}