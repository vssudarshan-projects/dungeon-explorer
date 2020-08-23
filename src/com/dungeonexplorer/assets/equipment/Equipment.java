package com.dungeonexplorer.assets.equipment;

public class Equipment {

    private final int id;
    private final String name;
    private final double baseModifier;
    private final int modifierType;

    public Equipment(int id, String name,int modifierType, double baseModifier) {
        this.id = id;
        this.name = name;
        this.baseModifier = baseModifier;
        this.modifierType = modifierType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBaseModifier() {
        return baseModifier;
    }

    public int getModifierType() {
        return modifierType;
    }

}
