/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Command;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UICommand extends Command {

    public interface Handler {
        public abstract void handle();
    }
    
    private UIForm form;
    public UIForm getForm() { return form; }
    public void setForm(UIForm form) {
        this.form = form;
        this.form.commandVisibilityChanged(this);
    }
    
    private boolean isVisible = true;
    public boolean isVisible() {
        return isVisible;
    }

    private Handler handler;
    private static int availablePriority = Integer.MIN_VALUE;

    public UICommand(String label, int commandType, int priority, Handler handler) {
        super(label, commandType, priority);
        this.handler = handler;
    }

    public UICommand(String label, Handler handler) {
        super(label, Command.SCREEN, availablePriority++);
        this.handler = handler;
    }

    void handle() {
        if (this.handler != null) {
            this.handler.handle();
        }
    }

    public UICommand visible(BoolBinding binding) {
        final UICommand self = this;
        binding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                boolean visible = ((Boolean) value).booleanValue();
                if (isVisible == visible) {
                    return;
                }
                isVisible = visible;
                if (form != null) {
                    form.commandVisibilityChanged(self);
                }
            }
        });
        return this;
    }

    public void onPause(UIMIDlet.PauseHandler handler) {
        this.form.getMidlet().addPauseHandler(handler);
    }

    public void onDestroy(UIMIDlet.DestroyHandler handler) {
        this.form.getMidlet().addDestroyHandler(handler);
    }
}
