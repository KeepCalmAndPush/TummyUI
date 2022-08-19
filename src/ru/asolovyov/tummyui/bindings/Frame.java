/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.bindings;

import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.bindings.PassthroughSubjectValueWrapper;
import ru.asolovyov.combime.operators.Operator;
import ru.asolovyov.combime.subjects.CurrentValueSubject;
import ru.asolovyov.tummyui.graphics.CGFrame;

/**
 *
 * @author Администратор
 */
public class Frame extends PassthroughSubjectValueWrapper {
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
        return (CGFrame)this.getValue();
    }

    public void setCGFrame(CGFrame value) {
        this.sendValue(value);
    }

    public Frame to(Operator operator) {
        return new Frame(super.to(operator));
    }
}
