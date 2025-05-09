package com.mightydanp.techcore.api.materials;

import com.mightydanp.techcore.api.materials.properties.MaterialFlags;
import com.mightydanp.techcore.api.materials.properties.PureSubstances;
import com.mightydanp.techcore.api.materials.properties.Temperature;

import java.util.ArrayList;
import java.util.List;

public class Material {
    public String name;

    public PureSubstances type;
    public int atomicNumber;
    public String symbol;
    public float molecularWeight;
    public float atomicWeight;

    public double meltingPoint;
    public double boilingPoint;

    

    public List<MaterialFlags.Codec> flags = new ArrayList<>();




}
