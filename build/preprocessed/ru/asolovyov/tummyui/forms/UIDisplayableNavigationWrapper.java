/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import ru.asolovyov.combime.bindings.Str;

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

    public Str title() {
        return new Str("");
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
