/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms.views;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.forms.UICommand;
import ru.asolovyov.tummyui.forms.UICommand.Handler;
import ru.asolovyov.tummyui.forms.UICommandsProxy;
import ru.asolovyov.tummyui.forms.UINavigatable;

/**
 *
 * @author Администратор
 */
public class UITextBox extends TextBox implements UINavigatable {
    private Str titleBinding;
    private Str textBinding;
    
    public UITextBox(Str titleBinding, Str textBinding) {
        this(titleBinding, textBinding, new Int(255), new Int(TextField.ANY));
    }

    public UITextBox(Str titleBinding, Str textBinding, Int maxSize) {
        this(titleBinding, textBinding, maxSize, new Int(TextField.ANY));
    }

    public UITextBox(
            final Str titleBinding,
            final Str textBinding,
            final Int maxSize,
            final Int constraints
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
    
    public Str title() {
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
}

