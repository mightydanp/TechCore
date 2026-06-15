package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;
import com.mightydanp.techcore.materials.properties.PureSubstances;

public class ChemicalComponent<A extends Material> extends Component<A, ChemicalComponent<A>> {
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

    public PureSubstances.PureSubstance getType() {
        return type;
    }

    public ChemicalComponent<A> setType(PureSubstances.PureSubstance type) {
        this.type = type;
        return this;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    public ChemicalComponent<A> setAtomicNumber(int atomicNumber) {
        this.atomicNumber = atomicNumber;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public ChemicalComponent<A> setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public float getMolecularWeight() {
        return molecularWeight;
    }

    public ChemicalComponent<A> setMolecularWeight(float molecularWeight) {
        this.molecularWeight = molecularWeight;
        return this;
    }

    public float getMolecularMass() {
        return molecularMass;
    }

    public ChemicalComponent<A> setMolecularMass(float molecularMass) {
        this.molecularMass = molecularMass;
        return this;
    }

    public float getAtomicMass() {
        return atomicMass;
    }

    public ChemicalComponent<A> setAtomicMass(float atomicMass) {
        this.atomicMass = atomicMass;
        return this;
    }

    public float getAtomicWeight() {
        return atomicWeight;
    }

    public ChemicalComponent<A> setAtomicWeight(float atomicWeight) {
        this.atomicWeight = atomicWeight;
        return this;
    }

    public String getChemicalFormula() {
        return chemicalFormula;
    }

    public ChemicalComponent<A> setChemicalFormula(String chemicalFormula) {
        this.chemicalFormula = chemicalFormula;
        return this;
    }
}
