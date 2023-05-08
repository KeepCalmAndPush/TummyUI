/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.bindings;

import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.bindings.CurrentValueSubjectWrapper;
import ru.asolovyov.combime.operators.Operator;
import ru.asolovyov.combime.subjects.CurrentValueSubject;
import ru.asolovyov.tummyui.graphics.CGFrame;

/**
 *
 * @author Администратор
 */
public class Frame extends CurrentValueSubjectWrapper {
    public Frame(CGFrame value) {
        super(new CurrentValueSubject(value));
    }

    public Frame(int x, int y, int width, int height) {
        this(new CGFrame(x, y, width, height));
    }

    public Frame(IPublisher source) {
        super(source);
    }

    private Frame(Frame source) {
        super(source);
        this.sendValue(source.getCGFrame());
    }

    public CGFrame getCGFrame() {
        return ((CGFrame)this.getValue()).copy();
    }

    public void setCGFrame(CGFrame value) {
        this.sendValue(value);
    }

    public Frame to(Operator operator) {
        return new Frame(super.to(operator));
    }
}
