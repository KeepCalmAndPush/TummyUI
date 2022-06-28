/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import javax.microedition.lcdui.StringItem;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIStringItem extends StringItem {
    private StringBinding labelBinding;
    private StringBinding textBinding;

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

    public String getText() {
        return this.textBinding.getString();
    }

    public void setText(String text) {
        this.textBinding.setString(text);
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
                UIStringItem.this.setLabel(string);
            }
        });

        this.textBinding.getPublisher().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                if (string.equals(getText())) {
                    return;
                }
                UIStringItem.this.setText(string);
            }
        });
    }
}
