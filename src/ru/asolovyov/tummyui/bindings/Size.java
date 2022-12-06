/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.bindings;

import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.bindings.PassthroughSubjectValueWrapper;
import ru.asolovyov.combime.operators.Operator;
import ru.asolovyov.combime.subjects.CurrentValueSubject;
import ru.asolovyov.tummyui.graphics.CGSize;

/**
 *
 * @author Администратор
 */
public class Size extends PassthroughSubjectValueWrapper {
    public Size(CGSize value) {
        super(new CurrentValueSubject(value));
    }

    public Size(int width, int height) {
        this(new CGSize(width, height));
    }

    public Size(IPublisher source) {
        super(source);
    }

    private Size(Size source) {
        super(source);
        this.sendValue(source.getCGSize());
    }

    public CGSize getCGSize() {
        return ((CGSize)this.getValue()).copy();
    }

    public void setCGSize(CGSize value) {
        this.sendValue(value);
    }

    public Size to(Operator operator) {
        return new Size(super.to(operator));
    }
}
