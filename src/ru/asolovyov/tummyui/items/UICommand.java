/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Command;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.S;
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
        
        this.subscribeOnStartIfPossible();
        this.subscribeOnPauseIfPossible();
        this.subscribeOnDestroyIfPossible();
    }

    public boolean needsRelayout = false;
    private boolean isVisible = true;
    public boolean isVisible() {
        return isVisible;
    }

    final PassthroughSubject onChanged = new PassthroughSubject();

    private Handler handler;
    private static int availablePriority = Integer.MIN_VALUE;

    public UICommand(StringBinding label, int commandType, int priority, Handler handler) {
        super(label.getString(), commandType, priority);
        this.handler = handler;
    }

    public UICommand(StringBinding label, Handler handler) {
        this(label, Command.SCREEN, availablePriority++, handler);
    }

    public UICommand(String label, Handler handler) {
        this(Binding.String(label), handler);
    }

    void handle() {
        if (this.handler != null) {
            this.handler.handle();
        }
    }

    public UICommand isVisible(final Bool binding) {
        binding.sink(new Sink() {
            protected void onValue(Object value) {
                if (isVisible == binding.getBool()) {
                    return;
                }
                isVisible = binding.getBool();
                S.print("UICOMMAND VIS");
                UICommand.this.needsRelayout = true;
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
                    if (pauseHandler != null) {
                        pauseHandler.handle();
                    }
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
                    if (destroyHandler != null) {
                        destroyHandler.handle(unconditional);
                    }
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
                    if (startHandler != null) {
                        startHandler.handle(isResume);
                    }
                }
            });
        }
    }
}
