/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import ru.asolovyov.combime.bindings.StringBinding;

/**
 *
 * @author Администратор
 */
public interface UINavigatable extends CommandListener {
    public StringBinding title();
    public Displayable displayable();
    
    public UINavigatable backCommand(UICommand command);
    public UICommandsProxy commands();
}
