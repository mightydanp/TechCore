package com.mightydanp.techcore.api.materials.properties;

public enum Subscript {
    _0(0, '\u2080'),
    _1(1, '\u2081'),
    _2(2, '\u2082'),
    _3(3, '\u2083'),
    _4(4, '\u2084'),
    _5(5, '\u2085'),
    _6(6, '\u2086'),
    _7(7, '\u2087'),
    _8(8, '\u2088'),
    _9(9, '\u2089');

    private final int value;
    private final char unicode;

    Subscript(int value, char unicode) {
        this.value = value;
        this.unicode = unicode;
    }

    public char getUnicode() {
        return unicode;
    }

    public static String convertNumber(int number) {
        return String.valueOf(number).chars()                          // Convert number to stream of chars
                .mapToObj(c -> Subscript.values()[c - '0'].unicode)    // Map each char to subscript unicode
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)  // Collect to string
                .toString();
    }
}
