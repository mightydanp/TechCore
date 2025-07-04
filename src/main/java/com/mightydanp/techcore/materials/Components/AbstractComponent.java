package com.mightydanp.techcore.materials.Components;

import com.mightydanp.techcore.materials.Material;

import java.util.function.Consumer;

public abstract class AbstractComponent<A extends AbstractComponent<A>> {
    private final String prefix;
    private final String suffix;
    private final Consumer<Material> onServerApply;
    private final Consumer<Material> onClientApply;

    protected AbstractComponent(String prefix, String suffix, Consumer<Material> onServerApply, Consumer<Material> onClientApply) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.onServerApply = onServerApply;
        this.onClientApply = onClientApply;
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
        onServerApply.accept(material);
        return (A)this;
    }

    @SuppressWarnings("unchecked")
    public A initClient(Material material) {
        onClientApply.accept(material);
        return (A)this;
    }
}