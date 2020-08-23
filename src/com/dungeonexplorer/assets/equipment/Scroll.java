package com.dungeonexplorer.assets.equipment;

import com.dungeonexplorer.assets.GameObject;

public class Scroll extends Equipment implements GameObject {

    private final String description;
    private final int gameObjectId;

    public Scroll(int gameObjectId, int scrollId, String name, String description, int modifierType, double baseModifier) {
        super(scrollId, name, modifierType, baseModifier);
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
