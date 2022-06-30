/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.S;
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
        this.labelBinding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                if (string.equals(UIStringItem.this.getLabel())) {
                    return;
                }
                UIStringItem.this.setLabel(string);
            }
        });

        this.textBinding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                S.println(string + " vs " + UIStringItem.this.getText());
                if (string.equals(UIStringItem.this.getText())) {
                    return;
                }
                UIStringItem.this.setText(string);
            }
        });
    }

    public void setForm(UIForm form) {
        this.form = form;
    }

    public void itemStateChanged(Item item) {
//        S.println("1");
        if (item != this) {
            return;
        }

//        S.println("2");
        if (false == this.labelBinding.getString().equals(this.getLabel())) {
//            S.println("3");
            this.labelBinding.setString(this.getLabel());
        }

//        S.println("4");
        if (false == this.textBinding.getString().equals(this.getText())) {
//            S.println("5");
            this.textBinding.setString(this.getText());
        }
    }

    public UIItem[] getUIItems() {
//        if (!isVisible) {
//            return new UIItem[]{};
//        }
        return new UIItem[]{ this };
    }

    public Item[] getPlainItems() {
        if (!isVisible) {
            return new Item[]{};
        }
        return new Item[] { this };
    }

    private boolean isVisible = true;
    public UIStringItem setVisible(BoolBinding binding) {
        binding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                isVisible = ((Boolean)value).booleanValue();
                if (form == null) { return; }
                form.layoutChanged(UIStringItem.this);
            }
        });
        return this;
    }
}
