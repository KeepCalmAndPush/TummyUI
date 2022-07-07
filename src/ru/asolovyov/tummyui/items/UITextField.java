/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UITextField extends TextField implements UIItem {
    private StringBinding labelBinding;
    private StringBinding stringBinding;
    private UIForm form;

    public UITextField(String label, String text, int maxSize, int constraints) {
       this(new StringBinding(label == null ? "" : label),
            new StringBinding(text == null ? "" : text),
            maxSize,
            constraints);
    }

    public UITextField(String label, String text) {
        this(label, text, 255, UITextField.ANY);
    }

    public UITextField(StringBinding labelBinding, StringBinding textBinding) {
        this(labelBinding, textBinding, 255, UITextField.ANY);
    }

    public UITextField(StringBinding labelBinding, StringBinding textBinding, int maxSize, int constraints) {
        super(labelBinding.getString(), textBinding.getString(), maxSize, constraints);
        
        this.labelBinding = labelBinding;
        this.stringBinding = textBinding;
        this.subscribeToBindings();
    }

    public void setString(String text) {
        this.stringBinding.setString(text);
    }

    public void setLabel(String text) {
        this.labelBinding.setString(text);
    }

    private void subscribeToBindings() {
        this.labelBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String) value;
                UITextField.this.setLabel(string);
            }
        });

        this.stringBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                UITextField.this.setString(string);
            }
        });
    }

    public void setForm(UIForm form) {
        this.form = form;
        
        this.subscribeOnStartIfPossible();
        this.subscribeOnPauseIfPossible();
        this.subscribeOnDestroyIfPossible();
    }

    public String getCurrentString() {
        char[] ch = new char[this.size()];
        this.getChars(ch);
        return new String(ch);
    }

    public void itemStateChanged(Item item) {
        if (item != this) {
            return;
        }

        if (false == this.labelBinding.getString().equals(this.getLabel())) {
            this.labelBinding.setString(this.getLabel());
        }

        String currentString = getCurrentString();
        if (false == this.stringBinding.getString().equals(currentString)) {
            this.stringBinding.setString(currentString);
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
    public UITextField isVisible(BoolBinding binding) {
        binding.sink(new Sink() {
            protected void onValue(Object value) {
                boolean visible = ((Boolean)value).booleanValue();
                if (form == null) {
                    isVisible = visible;
                    return;
                }
                
                isVisible = visible;
                form.didChangeLayout(UITextField.this);
            }
        });
        return this;
    }

    private UIItem parent;
    public UIItem getParent() { return parent; }
    public void setParent(UIItem parent) { this.parent = parent; }

    private UIMIDlet.PauseHandler pauseHandler;
    public UITextField onPause(UIMIDlet.PauseHandler handler) {
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
    public UITextField onDestroy(UIMIDlet.DestroyHandler handler) {
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
    public UITextField onStart(UIMIDlet.StartHandler handler) {
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
