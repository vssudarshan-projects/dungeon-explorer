package com.dungeonexplorer.assets.stage;

import com.dungeonexplorer.assets.GameObject;
import com.dungeonexplorer.assets.character.Monster;
import com.dungeonexplorer.assets.character.PlayerAvatar;
import com.dungeonexplorer.manager.GameObjectManager;
import com.dungeonexplorer.middleware.GameDriver;
import com.dungeonexplorer.middleware.Player;

import java.util.*;

public class Zone {

    /*                Example 3x3 zone
                    zone has navigable points

                    *(0,10)   *(5,10)  *(10,10)
                    *(0,5)    *(5,5)   *(10,5)
                    *(0,0)    *(5,0)   *(10,0)

                    MOVE_STEP = 5;
      */

    private final int zoneId;
    private final String name;
    private final String description;
    private final XY position;
    // possible expansion of zoneObject system: introduce coordinates for zoneObjects
    private final Map<String, ZoneObject> zoneObjects;
    private ZoneObjectOptions zoneObjectOptions;
    private final ZoneProperty zoneProperty;

    public Zone(int zoneId, String name, String description, XY position, Map<String, ZoneObject> zoneObjects, ZoneProperty zoneProperty) {
        this.zoneId = zoneId;
        this.name = name;
        this.description = description;
        this.position = position;
        //Objects.requireNonNullElseGet(zoneObjects, HashMap::new);
        this.zoneObjects = zoneObjects == null ? new HashMap<>() : new HashMap<>(zoneObjects);
        this.zoneProperty = zoneProperty;
    }

    public int getZoneId() {
        return zoneId;
    }

    String getName() {
        return name;
    }

    XY getPosition() {
        return position;
    }

    String getDescription() {
        return description;
    }

    int getType() {
        return zoneProperty.getType();
    }

    ZoneProperty getZoneProperty() {
        return zoneProperty;
    }

    void generateZoneObjects() {

        if (zoneObjectOptions == null)
            return;

        if (zoneObjectOptions.monsterList.size() != 0)
            generateMonsters(zoneObjectOptions.monsterList);
        if (zoneObjectOptions.treasureFound < zoneObjectOptions.MAX_TREASURE_COUNT - 1 && zoneObjectOptions.treasureList.size() != 0)
            generateTreasure(zoneObjectOptions.treasureList);
    }


    private void generateMonsters(List<Integer> monsters) {


        int monsterNumber = randomize(zoneObjectOptions.MAX_MONSTER_COUNT);

        for (int i = 0; i < monsterNumber; i++) {
            boolean isHidden = false;
            if (randomize(zoneObjectOptions.HIDDEN_MONSTER_CHANCE) == 1)
                isHidden = true;

            int monsterId = monsters.get(randomize(monsters.size()));
            Monster monster = GameObjectManager.monsterLookUp(monsterId);
            monster = new Monster(monster);
            String key = "monster" + i;
            ZoneObject zoneMonster = new ZoneObject(key, monster, isHidden, false);

            zoneObjects.put(key, zoneMonster);
        }
    }

    private void generateTreasure(List<Integer> treasures) {

        if (randomize(zoneObjectOptions.TREASURE_CHANCE) == 1) {
            int treasureNumber = randomize(zoneObjectOptions.MAX_TREASURE_COUNT - zoneObjectOptions.treasureFound);

            for (int i = 0; i < treasureNumber; i++) {
                int gameObjectId = treasures.get(randomize(treasures.size()));
                GameObject treasure = GameObjectManager.treasureLookUp(gameObjectId);
                String key = "treasure" + i;
                ZoneObject zoneTreasure = new ZoneObject(key, treasure, true, false);
                zoneObjects.put(key, zoneTreasure);
            }
        }
    }

    void removeNonPersistentZoneObjects() {
        if (zoneObjectOptions == null)
            return;

        ArrayList<String> keys = new ArrayList<>();

        for (Map.Entry<String, ZoneObject> entry : zoneObjects.entrySet()) {
            if (!entry.getValue().isPersistent()) {
                keys.add(entry.getKey());
            }
        }
        for (String key : keys)
            zoneObjects.remove(key);
    }

    List<String[]> getVisibleZoneObjects() {

        String[] zoneObjectString;
        ArrayList<String[]> zoneObjectStrArray = new ArrayList<>();

        for (Map.Entry<String, ZoneObject> entry : zoneObjects.entrySet())

            if (!entry.getValue().isHidden()) {
                zoneObjectString = new String[2];
                zoneObjectString[0] = entry.getValue().getGameObject().getName();
                zoneObjectString[1] = entry.getValue().getGameObject().getDescription();
                zoneObjectStrArray.add(zoneObjectString);
            }

        return zoneObjectStrArray;
    }

    String[] unHideZoneObject(String keySubString) {
        if (zoneObjectOptions == null)
            return null;

        LinkedList<ZoneObject> hiddenObjects = new LinkedList<>();
        int[] treasureIndex = new int[zoneObjectOptions.MAX_TREASURE_COUNT];
        int index = 0, i = 0;
        boolean treasure = false;
        for (Map.Entry<String, ZoneObject> entry : zoneObjects.entrySet())
            if (entry.getValue().isHidden() && ((keySubString.equals("") || entry.getKey().startsWith(keySubString)))) {
                hiddenObjects.add(entry.getValue());
                if (entry.getKey().startsWith("treasure")) {
                    treasureIndex[i++] = index;
                    treasure = true;
                }
                index++;
            }

        if (hiddenObjects.size() != 0) {
            index = randomize(hiddenObjects.size());
            ZoneObject zoneObject = hiddenObjects.get(index);
            zoneObject.setHidden(false);

            if (treasure && isTreasure(index, treasureIndex))
                zoneObjectOptions.treasureFound++;

            System.out.println(zoneObjectOptions.treasureFound); //console output for test

            String[] zoneObjectString = new String[2];
            zoneObjectString[0] = zoneObject.getGameObject().getName();
            zoneObjectString[1] = zoneObject.getGameObject().getDescription();

            return zoneObjectString;
        } else
            return null;
    }

    Player.PlayerXY navigateTo(int x, int y, Player.PlayerXY playerXY) {

        if (playerXY.x + x >= 0 && playerXY.x + x <= zoneProperty.getZoneSize().x && playerXY.y + y >= 0 && playerXY.y + y <= zoneProperty.getZoneSize().y) {
            for (Map.Entry<Integer, ZonePortal> entry : zoneProperty.getZonePortals().entrySet())
                if (playerXY.x + x == entry.getValue().getPosition().x && playerXY.y + y == entry.getValue().getPosition().y) {
                    playerXY.setZoneId(entry.getValue().getDestZonePortal().getZoneId());
                    playerXY.x = x != 0 ? entry.getValue().getDestZonePortal().getPosition().x + x : entry.getValue().getDestZonePortal().getPosition().x;
                    playerXY.y = y != 0 ? entry.getValue().getDestZonePortal().getPosition().y + y : entry.getValue().getDestZonePortal().getPosition().y;
                    return playerXY;
                }
            playerXY.x += x;
            playerXY.y += y;

        }
        return playerXY;
    }

    int startPVECombat(PlayerAvatar playerAvatar, Player.PlayerXY playerXY, String name) {

        if (!playerAvatar.isVulnerable())
            return -1;

        ZoneObject zoneObjectMonster = selectMonster(name);

        if (zoneObjectMonster == null)
            return -1;

        Monster monster = (Monster) zoneObjectMonster.getGameObject();
        //    showAllObjects();
        GameDriver.message("====================\nEntering Combat Mode\n====================\n\n");
        GameDriver.stringMessage(monster.getCharDetails());
        GameDriver.message("\n\n============================================== \n\n");
        GameDriver.stringMessage(playerAvatar.getCharDetails());
        GameDriver.message("\n\n============================================== \n");


        int monsterDieValue, playerDieValue;

        do {
            monsterDieValue = rollDie();
            playerDieValue = rollDie();
        } while (monsterDieValue == playerDieValue);

        boolean flee;
        while (playerAvatar.getStats().getHealth() > 0 && monster.getStats().getHealth() > 0) {
            if (playerDieValue > monsterDieValue) {
                flee = playerAvatar.attack(monster);
                monster.attack(playerAvatar);
            } else {
                monster.attack(playerAvatar);
                flee = playerAvatar.attack(monster);
            }
            if (flee) {
                monster.revive();
                break;
            }
            GameDriver.message(playerAvatar.getCombatStatus());
            GameDriver.message(monster.getCombatStatus());
        }
        int result = -1;
        if (monster.getStats().getHealth() == 0) {
            zoneObjects.remove(zoneObjectMonster.getKey());
            result = 1;
        }
        //  System.out.println("result = 1 \n"); //system output for test of health points
        //  showAllObjects();

        if (playerAvatar.getStats().getHealth() == 0) {
            removeNonPersistentZoneObjects();
            result = 0;
        }
        //  System.out.println("result = 0 \n");
        //   showAllObjects();

        return result;
    }

    private ZoneObject selectMonster(String name) {
        ArrayList<String> monsterList = new ArrayList<>();
        for (Map.Entry<String, ZoneObject> entry : zoneObjects.entrySet())
            if (entry.getKey().startsWith("monster") && !entry.getValue().isHidden() && entry.getValue().getGameObject().getName().toLowerCase().equals(name))
                monsterList.add(entry.getKey());

        if (monsterList.size() != 0) {
            int index = randomize(monsterList.size());
            return zoneObjects.get(monsterList.get(index));
        } else
            return null;
    }

    private int rollDie() {
        int value;
        while (true) {
            value = randomize(Constants.DIE_SIZE + 1);
            if (value != 0)
                return value;
        }
    }

    public List<GameObject> getTreasures(String name) {
        ArrayList<GameObject> gameObjects = new ArrayList<>();
        for (Map.Entry<String, ZoneObject> entry : zoneObjects.entrySet())
            if (((entry.getKey().startsWith("treasure")) && !entry.getValue().isHidden()) &&
                    (name.equals("") || entry.getValue().getGameObject().
                            getName().toLowerCase().equals(name))) {
                gameObjects.add(entry.getValue().getGameObject());
            }
        return gameObjects;
    }

    public int removeTreasures(String name, int count) {

        int removed = 0;
        ArrayList<String> keys = new ArrayList<>();

        for (Map.Entry<String, ZoneObject> entry : zoneObjects.entrySet()) {
            if (count > 0) {
                if (((entry.getKey().startsWith("treasure")) && !entry.getValue().isHidden()) &&
                        (name.equals("") || entry.getValue().getGameObject().getName().toLowerCase().
                                equals(name))) {
                    keys.add(entry.getKey());
                    count--;
                    removed++;
                }
            } else
                break;
        }

        for (String key : keys)
            zoneObjects.remove(key);

        return removed;
    }

    private boolean isTreasure(int index, int[] treasureIndex) {

        for (int value : treasureIndex) {
            if (value == index)
                return true;
        }
        return false;
    }


    private int randomize(int upper) {
        return (int) (upper * Math.random());
    }

    //console output for tests
    void showAllObjects() {
        System.out.println("=========\nZone Object");
        int count = 0;
        for (Map.Entry<String, ZoneObject> entry : zoneObjects.entrySet()) {
            System.out.println(entry.getValue().getGameObject().getName());
            count++;
        }
        System.out.println("Item count: " + count);
    }

    public void setObjectOptions(List<Integer> monsterList, List<Integer> treasureList, int MAX_MONSTER_COUNT, int HIDDEN_MONSTER_CHANCE, int MAX_TREASURE_COUNT, int TREASURE_CHANCE) {
        zoneObjectOptions = new ZoneObjectOptions(monsterList, treasureList, MAX_MONSTER_COUNT, HIDDEN_MONSTER_CHANCE, MAX_TREASURE_COUNT, TREASURE_CHANCE);
    }

    XY getSavePoint() {  //Implement ZoneObject coordinates
        //zoneObjects.get("revive point").getXY();
        return new XY(0, 5);
    }


    private class ZoneObjectOptions {

        private final List<Integer> monsterList; //stored with monsterId
        private final List<Integer> treasureList; //stored with gameObjectId
        private final int MAX_MONSTER_COUNT; // 0 to 4 monsters
        private final int HIDDEN_MONSTER_CHANCE; //1 in 5
        private final int TREASURE_CHANCE; // 1 in 10
        private final int MAX_TREASURE_COUNT;
        private int treasureFound;

        private ZoneObjectOptions(List<Integer> monsterList, List<Integer> treasureList, int MAX_MONSTER_COUNT, int HIDDEN_MONSTER_CHANCE, int MAX_TREASURE_COUNT, int TREASURE_CHANCE) {

            this.monsterList = monsterList == null ? new ArrayList<>() : new ArrayList<>(monsterList);
            this.treasureList = treasureList == null ? new ArrayList<>() : new ArrayList<>(treasureList);
            this.MAX_MONSTER_COUNT = MAX_MONSTER_COUNT + 1;
            if (HIDDEN_MONSTER_CHANCE == 1)
                HIDDEN_MONSTER_CHANCE = 2;
            this.HIDDEN_MONSTER_CHANCE = HIDDEN_MONSTER_CHANCE;
            if (TREASURE_CHANCE == 1)
                TREASURE_CHANCE = 2;
            this.TREASURE_CHANCE = TREASURE_CHANCE;
            this.MAX_TREASURE_COUNT = MAX_TREASURE_COUNT + 1;
            this.treasureFound = 0;
        }

    }

}