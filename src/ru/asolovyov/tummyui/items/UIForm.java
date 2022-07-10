/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Displayable;
import ru.asolovyov.tummyui.utils.List;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import ru.asolovyov.combime.common.Sink;

/**
 *
 * @author Администратор
 */
public class UIForm extends Form implements ItemStateListener, CommandListener {
    private List uiItems = new List();
    private List formCommands = new List();
    private List itemsCommands = new List();

    private List itemStateListeners = new List();
    private List commandListeners = new List();

    private UIMIDlet midlet;
    
    public UIForm(String title) {
       this(title, new Item[] {});
    }

    public UIForm(String title, Item[] items) {
        super(title, items);
        super.setItemStateListener(this);
        super.setCommandListener(this);
    }

    public UIForm(String title, UIItem[] items) {
        this(title, new Item[]{});
        for (int i = 0; i < items.length; i++) {
            UIItem uiItem = items[i];
            this.appendUI(uiItem);
        }
    }

    public void appendUI(UIItem uiItem) {
        uiItem.setForm(this);
        
        this.uiItems.addElement(uiItem);
        
        uiItem.getUICommands().forEach(new List.Enumerator() {
            public void onElement(Object element) {
                command((UICommand)element);
            }
        });
        
        uiItem.onChanged.sink(new Sink() {
            protected void onValue(Object value) {
                didChangeLayout((UIItem)value);
            }
        });
        
        Item[] plainItems = uiItem.getPlainItems();
        
        for (int i = 0; i < plainItems.length; i++) {
            super.append(plainItems[i]);
        }
        
        this.addItemStateListener(uiItem);
    }

    public void addItemStateListener(ItemStateListener listener) {
        this.itemStateListeners.addElement(listener);
    }

    public void removeItemStateListener(ItemStateListener listener) {
        this.itemStateListeners.removeElement(listener);
    }

    public void setItemStateListener(ItemStateListener listener) {
        if (listener == this) { return; }
        this.itemStateListeners.removeElement(listener);
        this.itemStateListeners.addElement(listener);
    }

    public void itemStateChanged(Item item) {
        for (int i = 0; i < this.itemStateListeners.size(); i++) {
            ItemStateListener listener = (ItemStateListener)this.itemStateListeners.elementAt(i);
            listener.itemStateChanged(item);
        }
    }

    private void didChangeLayout(UIItem uiItem) {
        this.deleteAll();
        this.itemsCommands = new List();
        for (int i = 0; i < this.uiItems.size(); i++) {
            UIItem item = (UIItem) this.uiItems.elementAt(i);
            Item[] plainItems = item.getPlainItems();
            
            for (int j = 0; j < plainItems.length; j++) {
                Item plainItem = plainItems[j];
                this.append(plainItem);
            }
        }

        List commands = new List()
                .append(formCommands)
                .append(itemsCommands);
        
        commands.forEach(new List.Enumerator() {
            public void onElement(Object element) {
                command((UICommand)element);
            }
        });
    }
    
    private void deleteAll() {
        int size = this.size();
        for (int i = size - 1; i >= 0; i--) {
            this.delete(i);
        }

        this.formCommands.forEach(new List.Enumerator() {
            public void onElement(Object element) {
                UIForm.this.removeCommand((Command) element);
            }
        });
    }
    
    public void addCommand(Command cmd) {
        final Command command = cmd;
        UICommand.Handler handler = new UICommand.Handler() {
            public void handle() {
                for (int i = 0; i < commandListeners.size(); i++) {
                    CommandListener listener = (CommandListener) commandListeners.elementAt(i);
                    listener.commandAction(command, UIForm.this);
                }
            }
        };
        
        UICommand uiCommand = new UICommand(
                cmd.getLabel(),
                cmd.getCommandType(),
                cmd.getPriority(),
                handler);
        
        this.command(uiCommand);
    }

    public UIForm command(UICommand cmd) {
        cmd.onChanged.sink(new Sink() {
            protected void onValue(Object value) {
                commandRequestsRelayout((UICommand)value);
            }
        });
        cmd.setForm(this);
        this.formCommands.addElement(cmd);
        if (cmd.isVisible()) {
            super.addCommand(cmd);
        }
        return this;
    }
    
    public void setCommandListener(CommandListener listener) {
        if (listener == this) { return; }
        this.commandListeners.removeElement(listener);
        this.commandListeners.addElement(listener);
    }

    public void commandAction(Command c, Displayable d) {
        for (int i = 0; i < this.commandListeners.size(); i++) {
            CommandListener listener = (CommandListener) this.commandListeners.elementAt(i);
            listener.commandAction(c, d);
        }

        final UICommand command = (UICommand)c;
        new List().append(this.formCommands).append(this.itemsCommands).forEach(new List.Enumerator() {
            public void onElement(Object element) {
                if (command == element) {
                    command.handle();
                }
            }
        });
    }

    private void commandRequestsRelayout(UICommand command) {
        for (int i = 0; i < formCommands.size(); i++) {
            this.removeCommand((UICommand) formCommands.elementAt(i));
        }

        for (int i = 0; i < formCommands.size(); i++) {
            UICommand uiCommand = (UICommand) formCommands.elementAt(i);
            if (uiCommand.isVisible()) {
                super.addCommand(uiCommand);
            }
        }
    }
    
    public UIMIDlet getMidlet() { return this.midlet; }
    public void setMidlet(UIMIDlet midlet) {
        this.midlet = midlet;
        for (int i = 0; i < uiItems.size(); i++) {
            UIItem uiItem = (UIItem) uiItems.elementAt(i);
            uiItem.setForm(this);
        }
    }
}
