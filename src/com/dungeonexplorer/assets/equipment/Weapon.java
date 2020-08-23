package com.dungeonexplorer.assets.equipment;

import com.dungeonexplorer.assets.GameObject;

public class Weapon extends Equipment implements GameObject {

    private final String description;
    private final int gameObjectId;

    public Weapon(int gameObjectId, int weaponId, String name, String description, int modifierType, double baseModifier) {
        super(weaponId, name, modifierType, baseModifier);
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
