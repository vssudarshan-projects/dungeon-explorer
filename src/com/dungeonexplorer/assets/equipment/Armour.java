package com.dungeonexplorer.assets.equipment;

import com.dungeonexplorer.assets.GameObject;

public class Armour extends Equipment implements GameObject {

    private final String description;
    private final int gameObjectId;

    public Armour(int gameObjectId, int armourId, String name, String description, int modifierType, double baseModifier) {
        super(armourId, name, modifierType, baseModifier);
        this.description = description;
        this.gameObjectId = gameObjectId;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getGameObjectId() {
        return this.gameObjectId;
    }
}
