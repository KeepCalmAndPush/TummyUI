/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.bindings;

import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.bindings.CurrentValueSubjectWrapper;
import ru.asolovyov.combime.operators.Operator;
import ru.asolovyov.combime.subjects.CurrentValueSubject;
import ru.asolovyov.tummyui.graphics.CGPoint;

/**
 *
 * @author Администратор
 */
public class Point extends CurrentValueSubjectWrapper {
    public Point(CGPoint value) {
        super(new CurrentValueSubject(value));
    }

    public Point(int x, int y) {
        this(new CGPoint(x, y));
    }

    public Point(IPublisher source) {
        super(source);
    }

    private Point(Point source) {
        super(source);
        this.sendValue(source.getCGPoint());
    }

    public CGPoint getCGPoint() {
        return ((CGPoint)this.getValue()).copy();
    }

    public void setCGPoint(CGPoint value) {
        this.sendValue(value);
    }

    public Point to(Operator operator) {
        return new Point(super.to(operator));
    }
}