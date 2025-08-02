package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.properties.PureSubstances;

public class ChemicalComponent extends Component {
    public PureSubstances.PureSubstance type;
    public int atomicNumber;
    public String symbol;
    public float molecularWeight;
    public float atomicWeight;

    public ChemicalComponent() {
        super("chemical", "component");
    }

    public ChemicalComponent setType(PureSubstances.PureSubstance type) {
        this.type = type;
        return this;
    }

    public ChemicalComponent setAtomicWeight(float atomicWeight) {
        this.atomicWeight = atomicWeight;
        return this;
    }

    public ChemicalComponent setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public ChemicalComponent setMolecularWeight(float molecularWeight) {
        this.molecularWeight = molecularWeight;
        return this;
    }

    public ChemicalComponent setAtomicNumber(int atomicNumber) {
        this.atomicNumber = atomicNumber;
        return this;
    }
}
