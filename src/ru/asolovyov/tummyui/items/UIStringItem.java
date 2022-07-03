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
        this.labelBinding = new StringBinding(label);
        this.textBinding = new StringBinding(text);
        this.subscribeToBindings();
    }

    public UIStringItem(StringBinding labelBinding, StringBinding textBinding) {
        super(labelBinding.getString(), textBinding.getString());
        this.labelBinding = labelBinding;
        this.textBinding = textBinding;
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
        this.labelBinding.getPublisher().removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                UIStringItem.this.setLabel(string);
            }
        });

        this.textBinding.getPublisher().removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                UIStringItem.this.setText(string);
            }
        });
    }

    public void setForm(UIForm form) {
        this.form = form;
        if (this.pauseHandler != null) { this.form.getMidlet().addPauseHandler(this.pauseHandler); }
        if (this.destroyHandler != null) { this.form.getMidlet().addDestroyHandler(this.destroyHandler); }
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
        binding.getPublisher().sink(new Sink() {
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

    private UIMIDlet.PauseHandler pauseHandler;
    public UIStringItem onPause(UIMIDlet.PauseHandler handler) {
        this.pauseHandler = handler;
        if (this.form != null) {
            this.form.getMidlet().addPauseHandler(handler);
        }
        return this;
    }

    private UIMIDlet.DestroyHandler destroyHandler;
    public UIStringItem onDestroy(UIMIDlet.DestroyHandler handler) {
        this.destroyHandler = handler;
        if (this.form != null) {
            this.form.getMidlet().addDestroyHandler(handler);
        }
        return this;
    }
}
