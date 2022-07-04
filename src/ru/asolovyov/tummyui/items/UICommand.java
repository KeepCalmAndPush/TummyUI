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
        
        this.subscribeOnStartIfPossible();
        this.subscribeOnPauseIfPossible();
        this.subscribeOnDestroyIfPossible();
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

    private UIMIDlet.PauseHandler pauseHandler;
    public UICommand onPause(UIMIDlet.PauseHandler handler) {
        this.pauseHandler = handler;
        subscribeOnPauseIfPossible();
        return this;
    }

    private void subscribeOnPauseIfPossible() {
        if (this.form != null && this.form.getMidlet() != null) {
            this.form.getMidlet().getPauseEventPublisher().sink(new Sink() {
                protected void onValue(Object value) {
                    pauseHandler.handle();
                }
            });
        }
    }

    private UIMIDlet.DestroyHandler destroyHandler;
    public UICommand onDestroy(UIMIDlet.DestroyHandler handler) {
        this.destroyHandler = handler;
        this.subscribeOnDestroyIfPossible();
        return this;
    }

    private void subscribeOnDestroyIfPossible() {
        if (this.form != null && this.form.getMidlet() != null) {
            this.form.getMidlet().getDestroyEventPublisher().sink(new Sink() {
                protected void onValue(Object value) {
                    boolean unconditional = ((Boolean)value).booleanValue();
                    destroyHandler.handle(unconditional);
                }
            });
        }
    }

    private UIMIDlet.StartHandler startHandler;
    public UICommand onStart(UIMIDlet.StartHandler handler) {
        this.startHandler = handler;
        this.subscribeOnStartIfPossible();
        return this;
    }

    private void subscribeOnStartIfPossible() {
        if (this.form != null && this.form.getMidlet() != null) {
            this.form.getMidlet().getDestroyEventPublisher().sink(new Sink() {
                protected void onValue(Object value) {
                    boolean isResume = ((Boolean)value).booleanValue();
                    startHandler.handle(isResume);
                }
            });
        }
    }
}
