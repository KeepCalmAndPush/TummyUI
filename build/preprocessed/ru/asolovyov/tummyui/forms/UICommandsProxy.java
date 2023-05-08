/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import ru.asolovyov.combime.common.S;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.data.List;

/**
 *
 * @author Администратор
 */
public class UICommandsProxy implements CommandListener {
    private Displayable displayable;

    private List commands = new List();
    private List commandListeners = new List();

    private UICommand backCommand;

    public UICommandsProxy(Displayable displayable) {
        super();
        this.displayable = displayable;
        this.displayable.setCommandListener(this);
    }

    public void addCommandListener(CommandListener listener) {
        this.commandListeners.removeElement(listener);
        this.commandListeners.addElement(listener);
    }

    public Displayable addCommand(UICommand cmd) {
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

        try {
            final UICommand command = (UICommand) c;
            this.commands.forEach(new List.Enumerator() {
                public void onElement(Object element) {
                    if (command == element) {
                        command.handle();
                    }
                }
            });
        } catch(ClassCastException e) {
            S.debugln("UICommandsProxy found non-UI command. Falling back to displayable to handle it.");
        }
    }

    private void commandRequestsRelayout(UICommand command) {
        this.commands.forEach(new List.Enumerator() {
            public void onElement(Object element) {
                UICommand command = (UICommand)element;
                displayable.removeCommand(command);
                if (command.isVisible()) {
                    addCommand(command);
                }
            }
        });
    }
    
    public void setBackCommand(UICommand command) {
        if (this.backCommand != null) {
            this.displayable.removeCommand(this.backCommand);
        }

        this.backCommand = command;
        this.addCommand(command);
    }
}