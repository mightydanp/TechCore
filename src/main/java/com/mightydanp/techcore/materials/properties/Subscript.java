package com.mightydanp.techcore.materials.properties;

public enum Subscript {
    ZERO(0, '\u2080'),
    ONE(1, '\u2081'),
    TWO(2, '\u2082'),
    THREE(3, '\u2083'),
    FOUR(4, '\u2084'),
    FIVE(5, '\u2085'),
    SIX(6, '\u2086'),
    SEVEN(7, '\u2087'),
    EIGHT(8, '\u2088'),
    NINE(9, '\u2089');

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
