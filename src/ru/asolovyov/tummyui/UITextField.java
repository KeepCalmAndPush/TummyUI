/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import javax.microedition.lcdui.TextField;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UITextField extends TextField {
    private StringBinding labelBinding;
    private StringBinding stringBinding;

    public UITextField(String label, String text, int maxSize, int constraints) {
        super(label, text, maxSize, constraints);
    }

    public UITextField(StringBinding labelBinding, StringBinding textBinding) {
        super(labelBinding.getString(), textBinding.getString(), 255, UITextField.ANY);
        this.labelBinding = labelBinding;
        this.stringBinding = textBinding;
        this.subscribeToBindings();
    }

    public String getString() {
        return this.stringBinding.getString();
    }

    public void setString(String text) {
        S.println("SET STRING " + text);
        this.stringBinding.setString(text);
    }

    public String getLabel() {
        return this.labelBinding.getString();
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
}
