/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.S;
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
        super(label, text, maxSize, constraints);
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
        this.labelBinding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                if (string.equals(getLabel())) {
                    return;
                }
                UITextField.this.setLabel(string);
            }
        });

        this.stringBinding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                if (string.equals(getString())) {
                    return;
                }
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
        S.println("1");
        if (item != this) {
            return;
        }

        S.println("2");
        if (false == this.labelBinding.getString().equals(this.getLabel())) {
            S.println("3");
            this.labelBinding.setString(this.getLabel());
        }

        S.println("4");
        String currentString = getCurrentString();
        if (false == this.stringBinding.getString().equals(currentString)) {
            S.println("5");
            this.stringBinding.setString(currentString);
        }
    }
}
