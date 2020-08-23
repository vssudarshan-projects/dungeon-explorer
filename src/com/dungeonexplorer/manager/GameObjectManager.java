package com.dungeonexplorer.manager;

import com.dungeonexplorer.assets.GameObject;
import com.dungeonexplorer.assets.character.Monster;

import java.util.Map;

public class GameObjectManager {
    public static GameObject gameObjectLookUp(int gameObjectId) {

        return null;
    }

    public static Monster monsterLookUp(int monsterId) {

        Map<Integer, Monster> monsters = new DataManager().getMonsterList();
        return monsters.get(monsterId);
    }

    public static GameObject treasureLookUp(int gameObjectId) {
        Map<Integer, GameObject> treasures = new DataManager().getTreasureList();
        return treasures.get(gameObjectId);
    }
}
