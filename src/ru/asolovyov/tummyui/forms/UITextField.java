/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UITextField extends UIItem {
    private TextField textField = new TextField("", "", 255, TextField.ANY);
    private StringBinding textBinding;

    public UITextField(StringBinding labelBinding, StringBinding textBinding) {
        this(labelBinding, textBinding, Binding.Int(255), Binding.Int(TextField.ANY));
    }

    public UITextField(StringBinding labelBinding, StringBinding textBinding, IntBinding maxSize) {
        this(labelBinding, textBinding, maxSize, Binding.Int(TextField.ANY));
    }

    public UITextField(
            final StringBinding labelBinding,
            final StringBinding textBinding,
            final IntBinding maxSize,
            final IntBinding constraints
            ) {
        
        super();

        this.textBinding = textBinding;

        if (labelBinding != null) {
            labelBinding.removeDuplicates().sink(new Sink() {
                protected void onValue(Object value) {
                    textField.setLabel((String) value);
                }
            });
        }

        textBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                textField.setString((String)value);
            }
        });

        maxSize.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                textField.setMaxSize(maxSize.getInt());
            }
        });

        constraints.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                textField.setConstraints(constraints.getInt());
            }
        });
    }

    public void itemStateChanged(Item item) {
        if (item != this.textField) {
            return;
        }

        String currentString = getCurrentString();
        this.textBinding.setString(currentString);
    }

    public UIItem[] getUIItems() {
        return new UIItem[]{ this };
    }

    public Item[] getPlainItems() {
        if (!isVisible) { return new Item[]{}; }
        return new Item[] { this.textField };
    }

    private String getCurrentString() {
        char[] chars = new char[this.textField.size()];
        this.textField.getChars(chars);
        return new String(chars);
    }
}
