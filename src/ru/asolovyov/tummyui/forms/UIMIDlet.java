/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Screen;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import ru.asolovyov.combime.api.IPublisher;
import ru.asolovyov.combime.subjects.PassthroughSubject;

/**
 *
 * @author Администратор
 */
public abstract class UIMIDlet extends MIDlet {
    public static abstract class StartHandler {
        public abstract void handle(boolean isResume);
    }

    public static abstract class PauseHandler {
        public abstract void handle();
    }

    public static abstract class DestroyHandler {
        public abstract void handle(boolean unconditional);
    }

    private PassthroughSubject startEventPublisher = new PassthroughSubject();
    private PassthroughSubject pauseEventPublisher = new PassthroughSubject();
    private PassthroughSubject destroyEventPublisher = new PassthroughSubject();

    public IPublisher getStartEventPublisher() { return startEventPublisher; }
    public IPublisher getPauseEventPublisher() { return pauseEventPublisher; }
    public IPublisher getDestroyEventPublisher() { return destroyEventPublisher; }

    private Display display;

    private Displayable content;
    
    protected abstract Displayable content();

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        this.destroyEventPublisher.sendValue(new Boolean(unconditional));
        Environment.clear();
    }

    protected void pauseApp() {
        this.pauseEventPublisher.sendValue(null);
    }

    protected void startApp() throws MIDletStateChangeException {
        Environment.midlet = this;
        
        Boolean isResume = new Boolean(this.content != null);
        
        if (isResume.booleanValue()) {
            this.startEventPublisher.sendValue(isResume);
            return;
        }
        
        this.content = content();
        display = Display.getDisplay(this);
        display.setCurrent(this.content);
        
        this.startEventPublisher.sendValue(isResume);
    }

    public Display getDisplay() {
        return display;
    }
}
