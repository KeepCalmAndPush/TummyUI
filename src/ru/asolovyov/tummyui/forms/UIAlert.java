/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.asolovyov.tummyui.forms;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.data.List;

/**
 *
 * @author Администратор
 */
public class UIAlert extends Alert implements CommandListener {

    private List uiCommands = new List();
    private List commandListeners = new List();

    public UIAlert(Str title, Str alertText, Obj alertImage, Obj alertType) {
        super(title.getString());

        title.removeDuplicates().sink(new Sink() {

            protected void onValue(Object value) {
                setTitle((String) value);
            }
        });

        if (alertText != null) {
            alertText.removeDuplicates().sink(new Sink() {

                protected void onValue(Object value) {
                    setString((String) value);
                }
            });
        }

        if (alertImage != null) {
            alertImage.removeDuplicates().sink(new Sink() {
                protected void onValue(Object value) {
                    setImage((Image) value);
                }
            });
        }

        if (alertType != null) {
            alertType.removeDuplicates().sink(new Sink() {

                protected void onValue(Object value) {
                    setType((AlertType) value);
                }
            });
        }

        setCommandListener(this);
    }

    public void addCommand(Command cmd) {
        final Command command = cmd;
        UICommand.Handler handler = new UICommand.Handler() {
            public void handle() {
                for (int i = 0; i < commandListeners.size(); i++) {
                    CommandListener listener = (CommandListener) commandListeners.elementAt(i);
                    listener.commandAction(command, UIAlert.this);
                }
            }
        };

        UICommand uiCommand = new UICommand(
                new Str(cmd.getLabel()),
                cmd.getCommandType(),
                cmd.getPriority(),
                handler);

        this.command(uiCommand);
    }

    public UIAlert command(UICommand cmd) {
        super.addCommand(cmd);
        this.uiCommands.addElement(cmd);
        return this;
    }

    public void setCommandListener(CommandListener listener) {
        if (listener == this) {
            return;
        }
        this.commandListeners.removeElement(listener);
        this.commandListeners.addElement(listener);
    }

    public void commandAction(Command c, Displayable d) {
        for (int i = 0; i < this.commandListeners.size(); i++) {
            CommandListener listener = (CommandListener) this.commandListeners.elementAt(i);
            listener.commandAction(c, d);
        }

        UICommand command = (UICommand) c;
        for (int i = 0; i < this.uiCommands.size(); i++) {
            if (command == this.uiCommands.elementAt(i)) {
                command.handle();
                return;
            }
        }
    }
}
