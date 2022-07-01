/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.operators.filtering.RemoveDuplicates;

/**
 *
 * @author Администратор
 */
public class UITextField extends TextField implements UIItem {
    private StringBinding labelBinding;
    private StringBinding stringBinding;
    private UIForm form;

    public UITextField(String label, String text, int maxSize, int constraints) {
        super(label, text, maxSize, constraints);
    }

    public UITextField(String label, String text) {
        super(label, text, 255, UITextField.ANY);
    }

    public UITextField(StringBinding labelBinding, StringBinding textBinding) {
        super(labelBinding.getString(), textBinding.getString(), 255, UITextField.ANY);
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
        this.labelBinding.getPublisher().removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String) value;
                UITextField.this.setLabel(string);
            }
        });

        this.stringBinding.getPublisher().removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                UITextField.this.setString(string);
            }
        });
    }

    public void setForm(UIForm form) {
        this.form = form;
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
    public UITextField setVisible(BoolBinding binding) {
        binding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                isVisible = ((Boolean)value).booleanValue();
                if (form == null) { return; }
                form.layoutChanged(UITextField.this);
            }
        });
        return this;
    }
}
