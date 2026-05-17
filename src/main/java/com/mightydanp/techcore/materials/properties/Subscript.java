package com.mightydanp.techcore.materials.properties;

public enum Subscript {
    ZERO(0, '₀'),
    ONE(1, '₁'),
    TWO(2, '₂'),
    THREE(3, '₃'),
    FOUR(4, '₄'),
    FIVE(5, '₅'),
    SIX(6, '₆'),
    SEVEN(7, '₇'),
    EIGHT(8, '₈'),
    NINE(9, '₉');

    private final int value;
    private final char unicode;

    Subscript(int value, char unicode) {
        this.value = value;
        this.unicode = unicode;
    }


    public int getValue() {
        return value;
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
