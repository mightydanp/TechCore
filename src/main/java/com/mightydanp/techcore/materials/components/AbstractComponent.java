package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;

import java.util.function.Consumer;

public abstract class AbstractComponent<A extends AbstractComponent<A>> {
    private final String prefix;
    private final String suffix;

    protected AbstractComponent(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String prefix() {
        return prefix;
    }

    public String suffix() {
        return suffix;
    }

    public String toString() {
        return prefix + "_" + suffix;
    }

    @SuppressWarnings("unchecked")
    public A initServer(Material material) {
        return (A)this;
    }

    @SuppressWarnings("unchecked")
    public A initClient(Material material) {
        return (A)this;
    }
}