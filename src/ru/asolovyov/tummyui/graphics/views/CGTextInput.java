/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.asolovyov.tummyui.graphics.views;

import java.util.Hashtable;

/**
 *
 * @author Администратор
 */
public class CGTextInput extends CGText {
//    public static final int LANGUAGE_RUSSIAN = 0;
//    public static final int LANGUAGE_ENGLISH = 1;
//    public static final Hashtable KEYSET_RUSSIAN = new Hashtable();
//    public static final Hashtable KEYSET_ENGLISH = new Hashtable();
//
//    static {
//        KEYSET_RUSSIAN.put(new In, KEYSET_RUSSIAN)
//    }

//    private Hashtable[] keysets = new Hashtable[] {
//
//    };

    public static final char[][] KEYSET_RUSSIAN;
    public static final char[][] KEYSET_ENGLISH;

    static {
        KEYSET_RUSSIAN = new char[][] {
            "*".toCharArray(),
            "0 -+=*/".toCharArray(),
            "#".toCharArray(),
            "1,-?.!:;".toCharArray(),
            "2абвг".toCharArray(),
            "3дежз".toCharArray(),
            "4ийкл".toCharArray(),
            "5мноп".toCharArray(),
            "6рсту".toCharArray(),
            "7фхцч".toCharArray(),
            "8шщъы".toCharArray(),
            "9ьэюя".toCharArray(),
        };

        KEYSET_ENGLISH = new char[][] {
            "*".toCharArray(),
            "0 -+=*/".toCharArray(),
            "#".toCharArray(),
            "1,-?.!:;".toCharArray(),
            "2abc".toCharArray(),
            "3def".toCharArray(),
            "4ghi".toCharArray(),
            "5jkl".toCharArray(),
            "6mno".toCharArray(),
            "7pqrs".toCharArray(),
            "8tuv".toCharArray(),
            "9wxyz".toCharArray(),
        };
    }

}
