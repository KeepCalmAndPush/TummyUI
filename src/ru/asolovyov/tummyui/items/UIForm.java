/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import java.util.Hashtable;
import javax.microedition.lcdui.Displayable;
import ru.asolovyov.tummyui.utils.List;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.combime.common.S;

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

    private StringBinding titleBinding;

    public UIForm(StringBinding title, Item[] items) {
        super(title.getString(), items);
        super.setItemStateListener(this);
        super.setCommandListener(this);

        if (title != null) {
            title.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                setTitle((String)value);
            }
        });
        }

        this.titleBinding = title;
    }

    public UIForm(StringBinding title) {
       this(title, new Item[] {});
    }

    public UIForm(StringBinding title, UIItem[] items) {
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

        final UIItem item = uiItem;
        item.onChanged.sink(new Sink() {
            protected void onValue(Object value) {
                if (item.needsRelayout) {
                    didChangeLayout((UIItem)value);
                    item.needsRelayout = false;
                }
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
                .append(formCommands);
                //.append(itemsCommands);
        
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
                Binding.String(cmd.getLabel()),
                cmd.getCommandType(),
                cmd.getPriority(),
                handler
                );
        
        this.command(uiCommand);
    }

    public UIForm command(UICommand cmd) {
        if (cmd.isVisible()) {
            super.addCommand(cmd);
        }

        if (this.formCommands.contains(cmd)) {
            return this;
        }

        if (cmd.getForm() != this) {
            cmd.setForm(this);
        }
        
        this.formCommands.addElement(cmd);
        cmd.onChanged.sink(new Sink() {
            protected void onValue(Object value) {
                commandRequestsRelayout((UICommand) value);
            }
        });

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
        this.formCommands.forEach(new List.Enumerator() {
            public void onElement(Object element) {
                UICommand command = (UICommand)element;
                removeCommand(command);
                if (command.isVisible()) {
                    command(command);
                }
            }
        });
    }
    
    public UIMIDlet getMidlet() { return this.midlet; }
    public void setMidlet(UIMIDlet midlet) {
        this.midlet = midlet;
        for (int i = 0; i < uiItems.size(); i++) {
            UIItem uiItem = (UIItem) uiItems.elementAt(i);
            uiItem.setForm(this);
        }
    }

    public UIForm alert(final Bool isVisible, final UIAlert alert) {
        isVisible.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                if (isVisible.getBool()) {
                    midlet.getDisplay().setCurrent(alert);
                }
            }
        });

        return this;
    }
    
    public UIForm navigationLink(
            StringBinding linkTitle,
            StringBinding backTitle,
            StringBinding formTitle,
            UIItem content
            ) {
        UIForm form = new UIForm(formTitle, new UIItem[]{ content });
        return this.navigationLink(linkTitle, backTitle, form);
    }

    private Hashtable displayableListeners = new Hashtable();

    public UIForm navigationLink(StringBinding linkTitle, Displayable displayable) {
        final Displayable content = displayable;
        return this.command(new UICommand(linkTitle, new UICommand.Handler() {
            public void handle() {
                command(new UICommand("", new UICommand.Handler() {
                    public void handle() {
                        getMidlet().getDisplay().setCurrent(UIForm.this);
                    }
                }));
                getMidlet().getDisplay().setCurrent(content);
            }
        }));
    }

    public UIForm navigationLink(
            final StringBinding linkTitle,
            final StringBinding backTitle,
            final UIForm form
            ) {
        final StringBinding back = backTitle != null ? backTitle : this.titleBinding;
        final StringBinding link = linkTitle != null ? linkTitle : form.titleBinding;

        form.command(new UICommand(back, new UICommand.Handler() {
            public void handle() {
                getMidlet().getDisplay().setCurrent(UIForm.this);
            }
        }));
        return this.command(new UICommand(link, new UICommand.Handler() {
            public void handle() {
                getMidlet().getDisplay().setCurrent(form);
            }
        }));
    }
}
