/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import ru.asolovyov.combime.bindings.Str;

/**
 *
 * @author Администратор
 */
public interface UINavigatable extends CommandListener {
    public Str title();
    public Displayable displayable();
    
    public UINavigatable backCommand(UICommand command);
    public UICommandsProxy commands();
}
