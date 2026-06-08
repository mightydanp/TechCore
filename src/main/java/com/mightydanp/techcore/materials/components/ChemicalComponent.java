package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.PureSubstances;

public class ChemicalComponent<A extends Material> extends Component<A, ChemicalComponent<A>>{
    private PureSubstances.PureSubstance type;
    private int atomicNumber;
    private String symbol;
    private float molecularWeight;
    private float molecularMass;
    private float atomicWeight;
    private float atomicMass;
    private String chemicalFormula;

    public ChemicalComponent(A material) {
        super("chemical", "component", material);
    }

    public ChemicalComponent<A> setType(PureSubstances.PureSubstance type) {
        this.type = type;
        return this;
    }

    public ChemicalComponent<A> setAtomicWeight(float atomicWeight) {
        this.atomicWeight = atomicWeight;
        return this;
    }

    public ChemicalComponent<A> setAtomicMass(float atomicMass) {
        this.atomicMass = atomicMass;
        return this;
    }

    public ChemicalComponent<A> setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public ChemicalComponent<A> setMolecularWeight(float molecularWeight) {
        this.molecularWeight = molecularWeight;
        return this;
    }

    public ChemicalComponent<A> setMolecularMass(float molecularMass) {
        this.molecularMass = molecularMass;
        return this;
    }

    public ChemicalComponent<A> setAtomicNumber(int atomicNumber) {
        this.atomicNumber = atomicNumber;
        return this;
    }

    public ChemicalComponent<A> setChemicalFormula(String chemicalFormula) {
        this.chemicalFormula = chemicalFormula;
        return this;
    }

    public PureSubstances.PureSubstance getType() {
        return type;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public float getMolecularWeight() {
        return molecularWeight;
    }

    public float getMolecularMass() {
        return molecularMass;
    }

    public float getAtomicMass() {
        return atomicMass;
    }

    public float getAtomicWeight() {
        return atomicWeight;
    }

    public String getChemicalFormula() {
        return chemicalFormula;
    }
}
