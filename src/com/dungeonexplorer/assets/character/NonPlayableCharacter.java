package com.dungeonexplorer.assets.character;

import com.dungeonexplorer.assets.GameObject;
import com.dungeonexplorer.assets.equipment.Armour;
import com.dungeonexplorer.assets.equipment.Scroll;
import com.dungeonexplorer.assets.equipment.Weapon;

import java.util.List;

public class NonPlayableCharacter extends Character implements GameObject {

    private final String description;
    private final int gameObjectId;

    public NonPlayableCharacter(int gameObjectId, int npcId, String name, String description, Stats stats, Weapon weapon, Armour armour, List<Scroll> scrolls, boolean isVulnerable) {
        super(npcId, name, stats, weapon, armour, scrolls, isVulnerable);
        this.description = description;
        this.gameObjectId = gameObjectId;
    }

    public List<String> getCharDetails() {
        return super.getCharDetails(this.description);
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
