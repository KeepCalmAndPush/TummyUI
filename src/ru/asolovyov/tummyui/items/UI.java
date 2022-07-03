/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.items;

import javax.microedition.lcdui.Item;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.StringBinding;

/**
 *
 * @author Администратор
 */
public class UI {
    public static class IfFactory {
        private BoolBinding binding;
        private UIGroup thenGroup;

        IfFactory(BoolBinding binding) {
            this.binding = binding;
        }

        IfFactory Then(UIGroup group) {
            this.thenGroup = group;
            return this;
        }

        UIIfItem Else(UIGroup group) {
            return new UIIfItem(binding, thenGroup, group);
        }
    }

    public static UIForm Form(String title, UIItem[] items) {
        return new UIForm(title, items);
    }

    public static IfFactory If(BoolBinding condition) {
        return new IfFactory(condition);
    }

    public static UIList List(ArrayBinding dataSource, final UIList.ItemFactory itemFactory) {
        return new UIList(dataSource, itemFactory);
    }

    public static UIPlainItemWrapper Wrapper(Item item) {
        return new UIPlainItemWrapper(item);
    }

    public static UIStringItem StringItem(String labelBinding, String textBinding) {
        return new UIStringItem(labelBinding, textBinding);
    }

    public static UITextField TextField(String labelBinding, String textBinding) {
        return new UITextField(labelBinding, textBinding);
    }

    public static UIStringItem StringItem(StringBinding labelBinding, StringBinding textBinding) {
        return new UIStringItem(labelBinding, textBinding);
    }

    public static UITextField TextField(StringBinding labelBinding, StringBinding textBinding) {
        return new UITextField(labelBinding, textBinding);
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

    public static UIForm Form(String title) {
        return new UIForm(title, new UIItem[]{});
    }

    public static UIForm Form(String title, UIItem i1) {
        return new UIForm(title, new UIItem[]{i1});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2) {
        return new UIForm(title, new UIItem[]{i1, i2});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3) {
        return new UIForm(title, new UIItem[]{i1, i2, i3});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4, i5});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4, i5, i6});
    }

    public static UIForm Form(String title, UIItem i1, UIItem i2, UIItem i3, UIItem i4, UIItem i5, UIItem i6, UIItem i7) {
        return new UIForm(title, new UIItem[]{i1, i2, i3, i4, i5, i6, i7});
    }
}
