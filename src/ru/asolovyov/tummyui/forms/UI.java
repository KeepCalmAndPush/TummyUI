/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import java.io.IOException;
import java.util.TimeZone;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;
import ru.asolovyov.tummyui.data.ListItem;

/**
 *
 * @author Администратор
 */
public class UI {
    public static class IfFactory {
        private Bool binding;
        private UIGroup thenGroup;

        public IfFactory(Bool binding) {
            this.binding = binding;
        }

        public IfFactory Then(UIGroup group) {
            this.thenGroup = group;
            return this;
        }

        public UIIf Else(UIGroup elseGroup) {
            return new UIIf(binding, thenGroup, elseGroup);
        }
    }

    public static UIForm Form(Str title, UIItem[] items) {
        return new UIForm(title, items);
    }

    public static IfFactory If(Bool condition) {
        return new IfFactory(condition);
    }

    public static UIAlert Alert(String title, String alertText, Image alertImage, AlertType alertType) {
        return Alert(
                new Str(title),
                new Str(alertText),
                new Obj(alertImage),
                new Obj(alertType)
                );
    }

    public static UIGauge Gauge(String label, boolean isInteractive, int maxValue, int value) {
        return new UIGauge(new Str(label), isInteractive, new Int(maxValue), new Int(value));
    }

    public static UIGauge Gauge(String label, boolean isInteractive, int maxValue, Int value) {
        return new UIGauge(new Str(label), isInteractive, new Int(maxValue), value);
    }

    public static UIGauge Gauge(boolean isInteractive, int maxValue, Int value) {
        return new UIGauge(null, isInteractive, new Int(maxValue), value);
    }

    public static UIGauge Gauge(Str label, boolean isInteractive, int maxValue, Int value) {
        return new UIGauge(label, isInteractive, new Int(maxValue), value);
    }

    public static UIGauge Gauge(Str label, boolean isInteractive, Int maxValue, Int value) {
        return new UIGauge(label, isInteractive, maxValue, value);
    }

    public static UINavigatable Navigatable(Displayable d) {
        return new UIDisplayableNavigationWrapper(d);
    }

    public static UIDateField DateField(Str label, int mode, Obj date) {
        return new UIDateField(label, new Int(mode), date, TimeZone.getDefault());
    }

    public static UIDateField DateField(int mode, Obj date) {
        return new UIDateField(null, new Int(mode), date, TimeZone.getDefault());
    }

    public static UIDateField DateField(int mode, Obj date, TimeZone timeZone) {
        return new UIDateField(null, new Int(mode), date, timeZone);
    }

    public static UIDateField DateField(String label, int mode, Obj date, TimeZone timeZone) {
        return new UIDateField(new Str(label), new Int(mode), date, timeZone);
    }

    public static UIDateField DateField(final Int mode, final Obj date, TimeZone timeZone) {
        return new UIDateField(null, mode, date, timeZone);
    }

    public static UIDateField DateField(final Str label, final Int mode, final Obj date, TimeZone timeZone) {
        return new UIDateField(label, mode, date, timeZone);
    }

    public static UIList List(String title, int type, ListItem[] items) {
        return new UIList(new Str(title), type, new Arr(items));
    }

    public static UIList List(Str title, int type, Arr items) {
        return new UIList(title, type, items);
    }

    public static UIChoiceGroup ChoiceGroup(String label, int type, ListItem[] items) {
        return new UIChoiceGroup(new Str(label), type, new Arr(items));
    }

    public static UIChoiceGroup ChoiceGroup(Str label, int type, Arr items) {
        return new UIChoiceGroup(label, type, items);
    }

    public static UIAlert Alert(Str title, Str alertText, Obj alertImage, Obj alertType) {
        UIAlert alert = new UIAlert(title, alertText, alertImage, alertType);
        alert.setTimeout(UIAlert.FOREVER);
        return alert;
    }

    public static UICommand Command(String label, UICommand.Handler handler) {
        return new UICommand(label, handler);
    }

    public static UICommand Command(String label, int type, UICommand.Handler handler) {
        return new UICommand(label, handler);
    }

    public static UIForEach ForEach(Arr dataSource, final UIForEach.ItemFactory itemFactory) {
        return new UIForEach(dataSource, itemFactory);
    }

    public static UIPlainItemWrapper Wrapper(Item item) {
        return new UIPlainItemWrapper(item);
    }

    public static UITextField TextField(Str text) {
        return new UITextField(null, text);
    }

    public static UITextField TextField(Str label, Str text) {
        return new UITextField(label, text);
    }

    public static UIStringItem StringItem(String label, String text) {
        return new UIStringItem(new Str(label), new Str(text));
    }

    public static UIStringItem StringItem(Str textBinding) {
        return StringItem(null, textBinding);
    }

    public static UIStringItem StringItem(Str labelBinding, Str textBinding) {
        labelBinding = labelBinding == null ? new Str("") : labelBinding;
        textBinding = textBinding == null ? new Str("") : textBinding;
        return new UIStringItem(labelBinding, textBinding);
    }

    public static UITextField TextField(String label, String text) {
        label = label == null ? "" : label;
        text = text == null ? "" : text;
        return new UITextField(new Str(label), new Str(text));
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
        return new UIImageItem(new Str(label), new Str(imageName), new Int(layout), new Str(altText));
    }

    public static UIImageItem ImageItem(Str label, Str imageName, int layout, Str altText) {
        return new UIImageItem(label, imageName, new Int(layout), altText);
    }

    public static UIImageItem ImageItem(Str label, Obj image, Int layout, Str altText) {
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

    public static UIForm Form(Str title) {
        return new UIForm(title, new UIItem[]{});
    }

    public static UIForm Form(Str title, UIItem i1) {
        return new UIForm(title, new UIItem[]{i1});
    }

    public static UIForm Form(Str title, UIItem i1, UIItem i2) {
        return new UIForm(title, new UIItem[]{i1, i2});
    }

    public static UIForm Form(Str title, UIItem i1, UIItem i2, UIItem i3) {
        return new UIForm(title, new UIItem[]{i1, i2, i3});
    }

    public static UIForm Form(Str title, UIItem i1, UIItem i2, UIItem i3, UIItem i4) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4});
    }

    public static UIForm Form(Str title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4, i5});
    }

    public static UIForm Form(Str title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4, i5, i6});
    }

    public static UIForm Form(Str title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6, UIItem i7) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4, i5, i6, i7});
    }

    public static UIForm Form(String title) {
        return new UIForm(new Str(title), new UIItem[]{});
    }

    public static UIForm Form(String title, UIItem i1) {
        return new UIForm(new Str(title), new UIItem[]{i1});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2) {
        return new UIForm(new Str(title), new UIItem[]{i1, i2});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3) {
        return new UIForm(new Str(title), new UIItem[]{i1, i2, i3});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4) {
        return new UIForm(new Str(title), new UIItem[]{i1, i2, i3, i4});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5) {
        return new UIForm(new Str(title), new UIItem[]{i1, i2, i3, i4, i5});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6) {
        return new UIForm(new Str(title), new UIItem[]{i1, i2, i3, i4, i5, i6});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6, UIItem i7) {
        return new UIForm(new Str(title), new UIItem[]{i1, i2, i3, i4, i5, i6, i7});
    }
}
