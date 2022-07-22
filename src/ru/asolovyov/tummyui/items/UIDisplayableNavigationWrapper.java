/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.StringBinding;

/**
 *
 * @author Администратор
 */
public class UIDisplayableNavigationWrapper implements UINavigatable {
    private Displayable displayable;
    private UICommandsProxy commands;

    public UIDisplayableNavigationWrapper(Displayable displayable) {
        this.displayable = displayable;
        this.commands = new UICommandsProxy(displayable);
    }

    public StringBinding title() {
        return Binding.String("");
    }

    public Displayable displayable() {
        return this.displayable;
    }

    public UINavigatable backCommand(UICommand command) {
        this.commands.setBackCommand(command);
        return this;
    }

    public UICommandsProxy commands() {
        return this.commands;
    }

    public void commandAction(Command c, Displayable d) {
        this.commands.commandAction(c, d);
    }
}
