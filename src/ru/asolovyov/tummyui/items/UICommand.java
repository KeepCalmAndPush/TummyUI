/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Command;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.subjects.PassthroughSubject;

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
//        this.form.commandRequestsRelayout(this);
        this.onChanged.sendValue(UICommand.this);
        
        this.subscribeOnStartIfPossible();
        this.subscribeOnPauseIfPossible();
        this.subscribeOnDestroyIfPossible();
    }
    
    private boolean isVisible = true;
    public boolean isVisible() {
        return isVisible;
    }

    final PassthroughSubject onChanged = new PassthroughSubject();

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

    public UICommand isVisible(final BoolBinding binding) {
        binding.sink(new Sink() {
            protected void onValue(Object value) {
                isVisible = binding.getBool();
                onChanged.sendValue(UICommand.this);
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
