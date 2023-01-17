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
    public static final IPublisher ticks = new PassthroughSubject();
    private static final Vector animations = new Vector();
    private static final Clock clock = new Clock(CG.FRAME_MILLIS);

    static {
        clock.add(new Runnable() {
            public void run() {
                //TODO ПРОВЕРИТЬ А ТОЧНО ЛИ ВСЯ ЭТА ХРЕНЬ ВЫПОЛНИТСЯ ЗА 33мс
                ((PassthroughSubject)ticks).sendValue(null);
                handleAnimations();
            }
        });
    }

    private CGDisplayLink() {}

    public static void addAnimation(CGAnimation animation) {
        animations.addElement(animation);
    }

    private static void handleAnimations() {
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
