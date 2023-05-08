/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.bindings;

import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.bindings.CurrentValueSubjectWrapper;
import ru.asolovyov.combime.operators.Operator;
import ru.asolovyov.combime.subjects.CurrentValueSubject;
import ru.asolovyov.tummyui.graphics.CGInsets;

/**
 *
 * @author Администратор
 */
public class Insets extends CurrentValueSubjectWrapper {
    public Insets(CGInsets value) {
        super(new CurrentValueSubject(value));
    }

    public Insets(int top, int left, int bottom, int right) {
        this(new CGInsets(top, left, bottom, right));
    }

    public Insets(IPublisher source) {
        super(source);
    }

    private Insets(Insets source) {
        super(source);
        this.sendValue(source.getCGInsets());
    }

    public CGInsets getCGInsets() {
        return ((CGInsets)this.getValue()).copy();
    }

    public void setCGInsets(CGInsets value) {
        this.sendValue(value);
    }

    public Insets to(Operator operator) {
        return new Insets(super.to(operator));
    }
}
