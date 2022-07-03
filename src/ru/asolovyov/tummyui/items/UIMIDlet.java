/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import ru.asolovyov.tummyui.utils.List;

/**
 *
 * @author Администратор
 */
public abstract class UIMIDlet extends MIDlet {
    public static abstract class PauseHandler {
        public abstract void handle();
    }

    public static abstract class DestroyHandler {
        public abstract void handle(boolean unconditional);
    }

    public UIMIDlet() {
        super();

        UIForm form = this.form();
        form.setMidlet(this);
        display = Display.getDisplay(this);
        display.setCurrent(this.form());
    }

    List pauseHandlers = new List();
    List destroyHandlers = new List();

    private Display display;

    protected abstract UIForm form();

    public void addPauseHandler(PauseHandler handler) {
        pauseHandlers.addElement(handler);
    }

    public void addDestroyHandler(DestroyHandler handler) {
        destroyHandlers.addElement(handler);
    }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        for (int i = 0; i < destroyHandlers.size(); i++) {
            DestroyHandler handler = (DestroyHandler) destroyHandlers.elementAt(i);
            handler.handle(unconditional);
        }
    }

    protected void pauseApp() {
        for (int i = 0; i < pauseHandlers.size(); i++) {
            PauseHandler handler = (PauseHandler) pauseHandlers.elementAt(i);
            handler.handle();
        }
    }

    protected void startApp() throws MIDletStateChangeException {
        
    }

}
