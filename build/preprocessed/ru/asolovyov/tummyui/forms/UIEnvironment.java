/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import java.util.Hashtable;
import ru.asolovyov.combime.bindings.Arr;
import ru.asolovyov.combime.bindings.Bool;
import ru.asolovyov.combime.bindings.Int;
import ru.asolovyov.combime.bindings.Obj;
import ru.asolovyov.combime.bindings.Str;

/**
 *
 * @author Администратор
 */
public final class UIEnvironment {
    private Hashtable data = new Hashtable();
    private static UIEnvironment shared = new UIEnvironment();

    private UIEnvironment() {}

    public static Str string(Object key) {
        Str value = (Str) shared.data.get(key);
        if (value == null) {
            value = new Str((String)null);
            shared.data.put(key, value);
        }
        return value;
    }

    public static Obj object(Object key) {
        Obj value = (Obj) shared.data.get(key);
        if (value == null) {
            value = new Obj((Object)null);
            shared.data.put(key, value);
        }
        return value;
    }

    public static Int integer(Object key) {
        Int value = (Int) shared.data.get(key);
        if (value == null) {
            value = new Int((Integer)null);
            shared.data.put(key, value);
        }
        return value;
    }

    public static Bool bool(Object key) {
        Bool value = (Bool) shared.data.get(key);
        if (value == null) {
            value = new Bool((Boolean)null);
            shared.data.put(key, value);
        }
        return value;
    }

    public static Arr array(Object key) {
        Arr value = (Arr) shared.data.get(key);
        if (value == null) {
            value = new Arr((Object[])null);
            shared.data.put(key, value);
        }
        return value;
    }

    public static Obj put(Object key, Object value) {
        Obj binding = object(key);
        binding.setObject(value);
        return binding;
    }

    public static Str put(Object key, String value) {
        Str binding = string(key);
        binding.setString(value);
        return binding;
    }

    public static Int put(Object key, int value) {
        Int binding = integer(key);
        binding.setInt(value);
        return binding;
    }

    public static Bool put(Object key, boolean value) {
        Bool binding = bool(key);
        binding.setBool(value);
        return binding;
    }

    public static Arr put(Object key, Object[] value) {
        Arr binding = array(key);
        binding.setArray(value);
        return binding;
    }

    public static Object remove(Object key) {
        return shared.data.remove(key);
    }

    static UIMIDlet midlet;
    public static UIMIDlet midlet() {
        return midlet;
    }

    static void clear() {
        shared.data.clear();
    }
}
