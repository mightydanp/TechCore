package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;

public class ToolComponent<A extends Material> extends Component<ToolComponent<A>> {
    private Integer durability = null;
    private Integer attackSpeed = null;
    private Float attackDamage = null;

    private final A material;

    public ToolComponent(A material) {
        super("tool", "component");
        this.material = material;
    }

    public ToolComponent<A> setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    public ToolComponent<A> setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
        return this;
    }

    public ToolComponent<A> setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
        return this;
    }

    public Integer getDurability() {
        return durability;
    }

    public Integer getAttackSpeed() {
        return attackSpeed;
    }

    public Float getAttackDamage() {
        return attackDamage;
    }

    public A end(){
        return material;
    }
}