/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIStringItem extends StringItem implements UIItem {
    private StringBinding labelBinding;
    private StringBinding textBinding;
    private UIForm form;

    public UIStringItem(String label, String text) {
        super(label, text);
        
        label = label == null ? "" : label;
        text = text == null ? "" : text;

        this.labelBinding = new StringBinding(label);
        this.textBinding = new StringBinding(text);
        this.subscribeToBindings();
    }

    public UIStringItem(StringBinding labelBinding, StringBinding textBinding) {
        super(labelBinding == null ? "" : labelBinding.getString(),
              textBinding == null ? "" : textBinding.getString());

        this.labelBinding = labelBinding == null ? new StringBinding("") : labelBinding;
        this.textBinding = textBinding == null ? new StringBinding("") : textBinding;
        
        this.subscribeToBindings();
    }

    public void setText(String text) {
        super.setText(text);
        this.textBinding.setString(text);
    }

    public void setLabel(String text) {
        super.setLabel(text);
        this.labelBinding.setString(text);
    }

    private void subscribeToBindings() {
        this.labelBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                UIStringItem.this.setLabel(string);
            }
        });

        this.textBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                UIStringItem.this.setText(string);
            }
        });
    }

    public void setForm(UIForm form) {
        this.form = form;
        this.subscribeOnStartIfPossible();
        this.subscribeOnPauseIfPossible();
        this.subscribeOnDestroyIfPossible();
    }

    public void itemStateChanged(Item item) {
        if (item != this) {
            return;
        }

        if (false == this.labelBinding.getString().equals(this.getLabel())) {
            this.labelBinding.setString(this.getLabel());
        }

        if (false == this.textBinding.getString().equals(this.getText())) {
            this.textBinding.setString(this.getText());
        }
    }

    public UIItem[] getUIItems() {
        return new UIItem[]{ this };
    }

    public Item[] getPlainItems() {
        if (!isVisible) {
            return new Item[]{};
        }
        return new Item[] { this };
    }

    private boolean isVisible = true;
    public UIStringItem visible(BoolBinding binding) {
        binding.sink(new Sink() {
            protected void onValue(Object value) {
                boolean visible = ((Boolean)value).booleanValue();
                if (form == null) {
                    isVisible = visible;
                    return;
                }
                
                isVisible = visible;
                form.didChangeLayout(UIStringItem.this);
            }
        });
        return this;
    }

    private UIItem parent;
    public UIItem getParent() { return parent; }
    public void setParent(UIItem parent) { this.parent = parent; }

    private boolean maySubscribeOnStart = true;
    private boolean maySubscribeOnPause = true;
    private boolean maySubscribeOnDestroy = true;

    private UIMIDlet.PauseHandler pauseHandler;
    public UIStringItem onPause(UIMIDlet.PauseHandler handler) {
        this.pauseHandler = handler;
        subscribeOnPauseIfPossible();
        return this;
    }

    private void subscribeOnPauseIfPossible() {
        if (this.pauseHandler != null && this.form != null && this.form.getMidlet() != null && this.maySubscribeOnPause) {
            this.maySubscribeOnPause = false;
            this.form.getMidlet().getPauseEventPublisher().sink(new Sink() {
                protected void onValue(Object value) {
                    pauseHandler.handle();
                }
            });
        }
    }

    private UIMIDlet.DestroyHandler destroyHandler;
    public UIStringItem onDestroy(UIMIDlet.DestroyHandler handler) {
        this.destroyHandler = handler;
        this.subscribeOnDestroyIfPossible();
        return this;
    }

    private void subscribeOnDestroyIfPossible() {
        if (this.destroyHandler != null && this.form != null && this.form.getMidlet() != null && this.maySubscribeOnDestroy) {
            this.maySubscribeOnDestroy = false;
            this.form.getMidlet().getDestroyEventPublisher().sink(new Sink() {
                protected void onValue(Object value) {
                    boolean unconditional = ((Boolean)value).booleanValue();
                    destroyHandler.handle(unconditional);
                }
            });
        }
    }

    private UIMIDlet.StartHandler startHandler;
    public UIStringItem onStart(UIMIDlet.StartHandler handler) {
        this.startHandler = handler;
        this.subscribeOnStartIfPossible();
        return this;
    }

    private void subscribeOnStartIfPossible() {
        if (this.startHandler != null && this.form != null && this.form.getMidlet() != null && this.maySubscribeOnStart) {
            this.maySubscribeOnStart = false;
            this.form.getMidlet().getStartEventPublisher().sink(new Sink() {
                protected void onValue(Object value) {
                    boolean isResume = ((Boolean)value).booleanValue();
                    startHandler.handle(isResume);
                }
            }).connect();
        }
    }
}
