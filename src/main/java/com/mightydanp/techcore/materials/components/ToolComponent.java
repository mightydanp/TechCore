package com.mightydanp.techcore.materials.components;

import com.mightydanp.techcore.materials.Material;

public class ToolComponent<A extends Material> extends Component<A, ToolComponent<A>> {
    private Integer durability = null;
    private Integer attackSpeed = null;
    private Float attackDamage = null;

    public ToolComponent(A material) {
        super("tool", "component", material);
    }

    public Integer getDurability() {
        return durability;
    }

    public ToolComponent<A> setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    public Integer getAttackSpeed() {
        return attackSpeed;
    }

    public ToolComponent<A> setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
        return this;
    }

    public Float getAttackDamage() {
        return attackDamage;
    }

    public ToolComponent<A> setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
        return this;
    }
}