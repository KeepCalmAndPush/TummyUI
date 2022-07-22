/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.items.UICommand.Handler;

/**
 *
 * @author Администратор
 */
public class UITextBox extends TextBox implements UINavigatable {
    private StringBinding titleBinding;
    private StringBinding textBinding;
    
    public UITextBox(StringBinding titleBinding, StringBinding textBinding) {
        this(titleBinding, textBinding, Binding.Int(255), Binding.Int(TextField.ANY));
    }

    public UITextBox(StringBinding titleBinding, StringBinding textBinding, IntBinding maxSize) {
        this(titleBinding, textBinding, maxSize, Binding.Int(TextField.ANY));
    }

    public UITextBox(
            final StringBinding titleBinding,
            final StringBinding textBinding,
            final IntBinding maxSize,
            final IntBinding constraints
            ) {

        super(titleBinding.getString(), textBinding.getString(), maxSize.getInt(), constraints.getInt());

        this.commands = new UICommandsProxy(this);
        this.titleBinding = titleBinding;
        this.textBinding = textBinding;

        titleBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                setTitle((String)value);
            }
        });

        textBinding.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                String string = (String)value;
                char[] characters = string.toCharArray();
                setChars(characters, 0, characters.length);
            }
        });

        maxSize.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                setMaxSize(maxSize.getInt());
            }
        });

        constraints.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                setConstraints(constraints.getInt());
            }
        });
    }

    private String getCurrentString() {
        char[] chars = new char[this.size()];
        this.getChars(chars);
        return new String(chars);
    }
    
    public StringBinding title() {
        return this.titleBinding;
    }

    public UINavigatable backCommand(UICommand command) {
        final Handler oldHandler = command.getHandler();
        command.setHandler(new UICommand.Handler() {
            public void handle() {
                textBinding.setString(getCurrentString());
                oldHandler.handle();
            }
        });
        this.commands.setBackCommand(command);
        return this;
    }

    public void commandAction(Command c, Displayable d) {
    }

    private UICommandsProxy commands;
    public UICommandsProxy commands() {
        return commands;
    }

    public Displayable displayable() {
        return this;
    }

//    public void title(StringBinding title) { }
}

