/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import java.io.IOException;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.Binding;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.bindings.StringBinding;

/**
 *
 * @author Администратор
 */
public class UI {
    public static class IfFactory {
        private Bool binding;
        private UIGroup thenGroup;

        IfFactory(Bool binding) {
            this.binding = binding;
        }

        IfFactory Then(UIGroup group) {
            this.thenGroup = group;
            return this;
        }

        UIIfItem Else(UIGroup elseGroup) {
            return new UIIfItem(binding, thenGroup, elseGroup);
        }
    }

    public static UIForm Form(StringBinding title, UIItem[] items) {
        return new UIForm(title, items);
    }

    public static IfFactory If(Bool condition) {
        return new IfFactory(condition);
    }

    public static UIAlert Alert(String title, String alertText, Image alertImage, AlertType alertType) {
        return Alert(
                Binding.String(title),
                Binding.String(alertText),
                Binding.Object(alertImage),
                Binding.Object(alertType)
                );
    }

    public static UIAlert Alert(StringBinding title, StringBinding alertText, ObjectBinding alertImage, ObjectBinding alertType) {
        UIAlert alert = new UIAlert(title, alertText, alertImage, alertType);
        alert.setTimeout(UIAlert.FOREVER);
        return alert;
    }

    public static UICommand Command(String label, UICommand.Handler handler) {
        return new UICommand(label, handler);
    }

    public static UIList List(ArrayBinding dataSource, final UIList.ItemFactory itemFactory) {
        return new UIList(dataSource, itemFactory);
    }

    public static UIPlainItemWrapper Wrapper(Item item) {
        return new UIPlainItemWrapper(item);
    }

    public static UITextField TextField(StringBinding label, StringBinding text) {
        return new UITextField(label, text);
    }

    public static UIStringItem StringItem(String label, String text) {
        return new UIStringItem(Binding.String(label), Binding.String(text));
    }

    public static UIStringItem StringItem(StringBinding textBinding) {
        return StringItem(null, textBinding);
    }

    public static UIStringItem StringItem(StringBinding labelBinding, StringBinding textBinding) {
        labelBinding = labelBinding == null ? new StringBinding("") : labelBinding;
        textBinding = textBinding == null ? new StringBinding("") : textBinding;
        return new UIStringItem(labelBinding, textBinding);
    }

    public static UITextField TextField(String label, String text) {
        label = label == null ? "" : label;
        text = text == null ? "" : text;
        return new UITextField(Binding.String(label), Binding.String(text));
    }

    public static Image Image(String path) {
        try {
            return Image.createImage(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static UIImageItem ImageItem(String label, String imageName, int layout, String altText) {
        return new UIImageItem(Binding.String(label), Binding.String(imageName), Binding.Int(layout), Binding.String(altText));
    }

    public static UIImageItem ImageItem(StringBinding label, StringBinding imageName, int layout, StringBinding altText) {
        return new UIImageItem(label, imageName, Binding.Int(layout), altText);
    }

    public static UIImageItem ImageItem(StringBinding label, ObjectBinding image, IntBinding layout, StringBinding altText) {
        return new UIImageItem(label, image, layout, altText);
    }
    
    public static UIGroup Group() {
        return new UIGroup(new UIItem[]{});
    }

    public static UIGroup Group(UIItem i1) {
        return new UIGroup(new UIItem[]{i1});
    }

    public static UIGroup Group(UIItem i1, UIItem i2) {
        return new UIGroup(new UIItem[]{i1, i2});
    }

    public static UIGroup Group(UIItem i1, UIItem i2, UIItem i3) {
        return new UIGroup(new UIItem[]{i1, i2, i3});
    }

    public static UIGroup Group(UIItem i1, UIItem i2, UIItem i3, UIItem i4) {
        return new UIGroup(new UIItem[]{i1, i2, i3, i4});
    }

    public static UIGroup Group(UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5) {
        return new UIGroup(new UIItem[]{i1, i2, i3, i4, i5});
    }

    public static UIGroup Group(UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6) {
        return new UIGroup(new UIItem[]{i1, i2, i3, i4, i5, i6});
    }

    public static UIGroup Group(UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6, UIItem i7) {
        return new UIGroup(new UIItem[]{i1, i2, i3, i4, i5, i6, i7});
    }

    public static UIForm Form(StringBinding title) {
        return new UIForm(title, new UIItem[]{});
    }

    public static UIForm Form(StringBinding title, UIItem i1) {
        return new UIForm(title, new UIItem[]{i1});
    }

    public static UIForm Form(StringBinding title, UIItem i1, UIItem i2) {
        return new UIForm(title, new UIItem[]{i1, i2});
    }

    public static UIForm Form(StringBinding title, UIItem i1, UIItem i2, UIItem i3) {
        return new UIForm(title, new UIItem[]{i1, i2, i3});
    }

    public static UIForm Form(StringBinding title, UIItem i1, UIItem i2, UIItem i3, UIItem i4) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4});
    }

    public static UIForm Form(StringBinding title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4, i5});
    }

    public static UIForm Form(StringBinding title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4, i5, i6});
    }

    public static UIForm Form(StringBinding title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6, UIItem i7) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4, i5, i6, i7});
    }

    public static UIForm Form(String title) {
        return new UIForm(Binding.String(title), new UIItem[]{});
    }

    public static UIForm Form(String title, UIItem i1) {
        return new UIForm(Binding.String(title), new UIItem[]{i1});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2) {
        return new UIForm(Binding.String(title), new UIItem[]{i1, i2});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3) {
        return new UIForm(Binding.String(title), new UIItem[]{i1, i2, i3});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4) {
        return new UIForm(Binding.String(title), new UIItem[]{i1, i2, i3, i4});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5) {
        return new UIForm(Binding.String(title), new UIItem[]{i1, i2, i3, i4, i5});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6) {
        return new UIForm(Binding.String(title), new UIItem[]{i1, i2, i3, i4, i5, i6});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6, UIItem i7) {
        return new UIForm(Binding.String(title), new UIItem[]{i1, i2, i3, i4, i5, i6, i7});
    }
}
