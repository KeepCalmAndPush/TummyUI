/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics;

import java.util.Enumeration;
import java.util.Vector;
import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.subjects.PassthroughSubject;
import ru.asolovyov.threading.Clock;

/**
 *
 * @author Администратор
 */
public class CGDisplayLink {
    public static final CGDisplayLink shared = new CGDisplayLink();
    public IPublisher ticks = new PassthroughSubject();
    private Vector animations = new Vector();

    private Clock clock;
    private CGDisplayLink() {
        this.clock = new Clock(CG.FRAME_MILLIS);
        this.clock.add(new Runnable() {
            public void run() {
                //TODO ПРОВЕРИТЬ А ТОЧНО ЛИ ВСЯ ЭТА ХРЕНЬ ВЫПОЛНИТСЯ ЗА 33мс
                ((PassthroughSubject)ticks).sendValue(CGDisplayLink.this);
                handleAnimations();
            }
        });
    }

    public void addAnimation(CGAnimation animation) {
        this.animations.addElement(animation);
    }

    private void handleAnimations() {
        Enumeration e = animations.elements();
        while (e.hasMoreElements()) {
            CGAnimation animation = (CGAnimation) e.nextElement();
            if (animation.isFinished()) {
                animations.removeElement(animation);
                continue;
            }
            animation.animateNextFrame();
        }
    }
}
