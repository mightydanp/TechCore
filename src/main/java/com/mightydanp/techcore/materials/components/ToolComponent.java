package com.mightydanp.techcore.materials.components;

public class ToolComponent extends Component {
    public Integer durability = null;
    public Integer attackSpeed = null;
    public Float attackDamage = null;

    public ToolComponent() {
        super("tool", "component",
                m -> { //server

            },  m -> { //client

        });
    }

    public ToolComponent setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    public ToolComponent setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
        return this;
    }

    public ToolComponent setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
        return this;
    }
}