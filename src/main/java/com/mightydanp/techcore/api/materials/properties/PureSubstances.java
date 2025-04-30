package com.mightydanp.techcore.api.materials.properties;

public enum PureSubstances {
    compound("compound"),
    element("element");

    public final String name;

    PureSubstances(String name) {
        this.name = name;
    }
}
