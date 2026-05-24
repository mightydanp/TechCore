package com.mightydanp.techcore.api.helpers;

public record ValueBounds<T extends Number>(T defaultValue, T maxValue) {
    public static <T extends Number> ValueBounds<T> between(T defaultValue, T maxValue) {
        return new ValueBounds<>(defaultValue, maxValue);
    }

    public static <T extends Number> ValueBounds<T> atMost(T maxValue) {
        return new ValueBounds<>(null, maxValue);
    }

    public static <T extends Number> ValueBounds<T> atLeast(T defaultValue) {
        return new ValueBounds<>(defaultValue, null);
    }

    public static <T extends Number> ValueBounds<T> exact(T value) {
        return new ValueBounds<>(value, value);
    }

    public static <T extends Number> ValueBounds<T> any() {
        return new ValueBounds<>(null, null);
    }

    public boolean hasValues() {
        return defaultValue != null && maxValue != null;
    }
}
