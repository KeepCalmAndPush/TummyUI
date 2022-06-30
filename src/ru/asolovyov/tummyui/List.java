/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui;

import java.util.Vector;

/**
 *
 * @author Администратор
 */
public class List extends Vector {
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

    public void appendArray(Object[] array) {
        this.insertArrayAt(this.size(), array);
    }

    public void insertArrayAt(int position, Object[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            Object object = array[i];
            this.insertElementAt(object, position);
        }
    }

    public void deleteRange(int start, int length) {
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
    }

    public void replaceRange(int start, int length, Object[] replacement) {
        this.deleteRange(start, length);
        this.insertArrayAt(start, replacement);
    }
}
