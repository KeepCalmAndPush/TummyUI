/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.forms;

import java.util.Hashtable;
import ru.asolovyov.combime.bindings.ArrayBinding;
import ru.asolovyov.combime.bindings.BoolBinding;
import ru.asolovyov.combime.bindings.IntBinding;
import ru.asolovyov.combime.bindings.ObjectBinding;
import ru.asolovyov.combime.bindings.StringBinding;

/**
 *
 * @author Администратор
 */
public final class Environment {
    private Hashtable data = new Hashtable();
    private static Environment shared = new Environment();

    private Environment() {}

    public static StringBinding string(Object key) {
        StringBinding value = (StringBinding) shared.data.get(key);
        if (value == null) {
            value = new StringBinding((String)null);
            shared.data.put(key, value);
        }
        return value;
    }

    public static ObjectBinding object(Object key) {
        ObjectBinding value = (ObjectBinding) shared.data.get(key);
        if (value == null) {
            value = new ObjectBinding((Object)null);
            shared.data.put(key, value);
        }
        return value;
    }

    public static IntBinding integer(Object key) {
        IntBinding value = (IntBinding) shared.data.get(key);
        if (value == null) {
            value = new IntBinding((Integer)null);
            shared.data.put(key, value);
        }
        return value;
    }

    public static BoolBinding bool(Object key) {
        BoolBinding value = (BoolBinding) shared.data.get(key);
        if (value == null) {
            value = new BoolBinding((Boolean)null);
            shared.data.put(key, value);
        }
        return value;
    }

    public static ArrayBinding array(Object key) {
        ArrayBinding value = (ArrayBinding) shared.data.get(key);
        if (value == null) {
            value = new ArrayBinding((Object[])null);
            shared.data.put(key, value);
        }
        return value;
    }

    public static ObjectBinding put(Object key, Object value) {
        ObjectBinding binding = object(key);
        binding.setObject(value);
        return binding;
    }

    public static StringBinding put(Object key, String value) {
        StringBinding binding = string(key);
        binding.setString(value);
        return binding;
    }

    public static IntBinding put(Object key, int value) {
        IntBinding binding = integer(key);
        binding.setInt(value);
        return binding;
    }

    public static BoolBinding put(Object key, boolean value) {
        BoolBinding binding = bool(key);
        binding.setBool(value);
        return binding;
    }

    public static ArrayBinding put(Object key, Object[] value) {
        ArrayBinding binding = array(key);
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
