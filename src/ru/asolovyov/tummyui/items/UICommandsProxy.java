/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.utils.List;

/**
 *
 * @author Администратор
 */
public class UICommandsProxy implements CommandListener {
    private Displayable displayable;

    private List commands = new List();
    private List commandListeners = new List();

    public UICommandsProxy(Displayable displayable) {
        super();
        this.displayable = displayable;
        this.displayable.setCommandListener(this);
    }

    public Displayable command(UICommand cmd) {
        if (cmd.isVisible()) {
            displayable.addCommand(cmd);
        }

        this.commands.addElement(cmd);
        cmd.onChanged.sink(new Sink() {
            protected void onValue(Object value) {
                commandRequestsRelayout((UICommand) value);
            }
        });

        return displayable;
    }

    public void commandAction(Command c, Displayable d) {
        for (int i = 0; i < this.commandListeners.size(); i++) {
            CommandListener listener = (CommandListener) this.commandListeners.elementAt(i);
            listener.commandAction(c, d);
        }

        final UICommand command = (UICommand)c;
        this.commands.forEach(new List.Enumerator() {
            public void onElement(Object element) {
                if (command == element) {
                    command.handle();
                }
            }
        });
    }

    private void commandRequestsRelayout(UICommand command) {
        this.commands.forEach(new List.Enumerator() {
            public void onElement(Object element) {
                UICommand command = (UICommand)element;
                displayable.removeCommand(command);
                if (command.isVisible()) {
                    command(command);
                }
            }
        });
    }

    private UICommand backCommand;
    public void setBackCommand(UICommand command) {
        if (this.backCommand != null) {
            this.displayable.removeCommand(this.backCommand);
        }

        this.backCommand = command;
        this.command(command);
    }
}