package com.mightydanp.techcore.api.materials;

import com.mightydanp.techcore.api.materials.properties.MaterialFlags;
import com.mightydanp.techcore.api.materials.properties.PureSubstances;

import java.util.ArrayList;
import java.util.List;

public class Material {
    public String name;

    public PureSubstances type;
    public int atomicNumber;
    public String symbol;
    public float molecularWeight;
    public float atomicWeight;

    public List<MaterialFlags.Codec> flags = new ArrayList<>();



}
