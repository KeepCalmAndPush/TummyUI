/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.utils;

import java.util.Vector;

/**
 *
 * @author Администратор
 */
public class List extends Vector {
    public static abstract class Enumerator {
        public abstract void onElement(Object element);
    }

    public List(Object[] array) {
        super();

        for (int i = 0; i < array.length; i++) {
            Object object = array[i];
            this.addElement(object);
        }
    }

    public List() {
        this(new Object[]{});
    }

    public Object[] toArray() {
        Object[] array = new Object[this.size()];
        this.copyInto(array);
        return array;
    }

    public List append(Object[] array) {
        this.insertArrayAt(this.size(), array);
        return this;
    }

    public List append(Vector vector) {
        for (int i = 0; i < vector.size(); i++) {
            Object object = vector.elementAt(i);
            this.addElement(object);
        }
        return this;
    }

    public List insertArrayAt(int position, Object[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            Object object = array[i];
            this.insertElementAt(object, position);
        }
        return this;
    }

    public List deleteRange(int start, int length) {
        int j = start;
        for (int i = start; i < start + length; i++) {
            Object object = this.elementAt(i);
            this.setElementAt(object, j);
            j++;
        }
        int targetSize = this.size() - length;
        while(this.size() > targetSize) {
            this.removeElementAt(this.size() - 1);
        }
        this.trimToSize();

        return this;
    }

    public List replaceRange(int start, int length, Object[] replacement) {
        this.deleteRange(start, length);
        this.insertArrayAt(start, replacement);

        return this;
    }

    public void forEach(Enumerator e) {
        for (int i = 0; i < this.size(); i++) {
            Object object = this.elementAt(i);
            e.onElement(object);
        }
    }
}
