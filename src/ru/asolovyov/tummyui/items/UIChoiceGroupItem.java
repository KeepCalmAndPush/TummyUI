/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.ChoiceGroup;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.StringBinding;

/**
 *
 * @author Администратор
 */
public class UIChoiceGroupItem extends UIItem {
    private ChoiceGroup plainItem = new ChoiceGroup(null, ChoiceGroup.MULTIPLE);

    public UIChoiceGroupItem(StringBinding label, IntBinding choiceType, ArrayBinding stringElements, ArrayBinding imageElements) {
        super();
    }
}
