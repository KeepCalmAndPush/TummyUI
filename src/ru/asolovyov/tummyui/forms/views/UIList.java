/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms.views;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.combime.common.Sink;
import ru.asolovyov.tummyui.forms.UICommand.Handler;
import ru.asolovyov.tummyui.data.ListItem;
import ru.asolovyov.tummyui.forms.UICommand;
import ru.asolovyov.tummyui.forms.UICommandsProxy;
import ru.asolovyov.tummyui.forms.UINavigatable;

/**
 *
 * @author Администратор
 */
public class UIList extends List implements UINavigatable {
    private Str titleBinding;
    private Arr itemsBinding;

    public UIList(
            Str title,
            int choiceType,
            Arr itemsBinding)
    {
        super(title.getString(), choiceType);
        this.commands = new UICommandsProxy(this);
        this.titleBinding = title;

        title.removeDuplicates().sink(new Sink() {
            protected void onValue(Object value) {
                setTitle((String) value);
            }
        });

        this.itemsBinding = itemsBinding;

        this.itemsBinding.sink(new Sink() {
            protected void onValue(Object value) {
                Object[] array = (Object[]) value;
                for (int i = 0; i < array.length; i++) {
                    ListItem item = (ListItem) array[i];
                    if (i >= size()) {
                        append(item.getStringPart(), item.getImagePart());
                    } else {
                        set(i, item.getStringPart(), item.getImagePart());
                    }
                    setSelectedIndex(i, item.isSelected());
                }
            }
        });
    }

    private void sendSelectionChanged() {
        boolean flags[] = new boolean[this.size()];
        this.getSelectedFlags(flags);

        ListItem[] items = (ListItem[]) this.itemsBinding.getArray();
        for (int i = 0; i < this.size(); i++) {
            items[i].setSelected(flags[i]);
        }

        this.itemsBinding.sendValue(items);
    }

    private UICommandsProxy commands;
    public UICommandsProxy commands() {
        return commands;
    }

    public Displayable displayable() {
        return this;
    }

    public Str title() {
        return this.titleBinding;
    }

    public UINavigatable backCommand(UICommand command) {
        final Handler oldHandler = command.getHandler();
        command.setHandler(new UICommand.Handler() {
            public void handle() {
                sendSelectionChanged();
                oldHandler.handle();
            }
        });
        this.commands.setBackCommand(command);
        return this;
    }

    public void commandAction(Command c, Displayable d) {
        if (d == this && c == SELECT_COMMAND) {
            this.sendSelectionChanged();
        }
    }
}
