/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;
import ru.asolovyov.combime.bindings.StringBinding;
import ru.asolovyov.combime.common.S;

/**
 * @author Администратор
 */
public class Tests extends MIDlet {

    private Display display;
    private Form form = new Form("TummyUI");

    private StringBinding label = new StringBinding("Label");
    private StringBinding text = new StringBinding("Text");
    
    public void startApp() {
        display = Display.getDisplay(this);
        display.setCurrent(form);
        form.append(new UIStringItem(label, text));
        form.append(new UITextField(label, text));
        form.setItemStateListener(new ItemStateListener() {

            public void itemStateChanged(Item item) {
                S.println("KEK!");
            }
        });
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
