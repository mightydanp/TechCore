package com.mightydanp.techcore.materials.components;

import net.minecraftforge.client.event.RegisterColorHandlersEvent;

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
    public A init() {
        return (A)this;
    }

    @SuppressWarnings("unchecked")
    public A initClient() {
        return (A)this;
    }

    @SuppressWarnings("unchecked")
    public A initLanguages() {
        return (A)this;
    }

    @SuppressWarnings("unchecked")
    public A initItemProperties() {
        return (A)this;
    }

    @SuppressWarnings("unchecked")
    public A initClientRenderLayers(RegisterColorHandlersEvent.Item event) {
        return (A)this;
    }
}