package com.dungeonexplorer.assets.stage;


import com.dungeonexplorer.assets.character.PlayerAvatar;
import com.dungeonexplorer.middleware.GameDriver;
import com.dungeonexplorer.middleware.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stage {

    /*
     *              Example Stage Map 10x10
     *
     *              zone7(0,10)  zone8(5,10)  zone0(10,10)
     *              zone4(0,5)   zone5(5,5)   zone6(10,5)
     *              zone1(0,0)   zone2(5,0)   zone3(10,0)
     *
     *              zone0 is entry zone
     *              MOVE_STEP = 5;
     *
     * */


    private final int stageNumber;
    private final String stageName;
    private final XY stageSize;
    private final Map<Integer, Zone> stageMap;

    public Stage(int stageNumber, String stageName, XY stageSize, Map<Integer, Zone> stageMap) {
        this.stageNumber = stageNumber;
        this.stageName = stageName;
        this.stageSize = stageSize;
        if (stageMap == null || stageMap.size() == 0) {
            GameDriver.message("Warning: " + "[" + this.getClass().getName() + "]" + " [Stage Number: " + stageNumber + "]" + " [Stage Name: " + stageName + "]"
                    + " Stage map cannot be null or empty.");
            this.stageMap = new HashMap<>();
            System.exit(-1);
        } else
            this.stageMap = new HashMap<>(stageMap);
        //Objects.requireNonNullElseGet(stageMap, HashMap::new);
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public String getStageName() {
        return stageName;
    }

    public XY getStageSize() {
        return stageSize;
    }

    public Map<Integer, Zone> getStageMap() {
        return new HashMap<>(stageMap);
    }


    public int getZoneId(XY position) {

        for (Map.Entry<Integer, Zone> entry : stageMap.entrySet())
            if (entry.getValue().getPosition().getX() == position.getX() &&
                    entry.getValue().getPosition().getY() == position.getY())
                return entry.getKey();
        return -1;
    }

    public int getZoneIdByDistance(int zoneId, int x, int y) {

        for (Map.Entry<Integer, Zone> entry : stageMap.entrySet())
            if (entry.getValue().getPosition().x == stageMap.get(zoneId).getPosition().x + x &&
                    entry.getValue().getPosition().y == stageMap.get(zoneId).getPosition().y + y)
                return entry.getKey();
        return -1;
    }

    public String getZoneName(XY position) {
        for (Map.Entry<Integer, Zone> entry : stageMap.entrySet())
            if (entry.getValue().getPosition().getX() == position.getX() &&
                    entry.getValue().getPosition().getY() == position.getY())
                return entry.getValue().getName();

        return null;
    }

    public String getZoneName(int zoneId) {

        return stageMap.get(zoneId).getName();
    }

    public String getZoneDescription(XY position) {
        for (Map.Entry<Integer, Zone> entry : stageMap.entrySet())
            if (entry.getValue().getPosition().getX() == position.getX() &&
                    entry.getValue().getPosition().getY() == position.getY())
                return entry.getValue().getDescription();

        return null;
    }

    public String getZoneDescription(int zoneId) {

        return stageMap.get(zoneId).getDescription();
    }

    public void removeZoneObjects(int zoneId) {
        stageMap.get(zoneId).removeNonPersistentZoneObjects();
    }

    public void generateZoneObjects(int zoneId) {

        Zone zone = stageMap.get(zoneId);

        switch (zone.getType()) {
            case Constants.SAFE_ZONE:
                break;
            case Constants.PVP_ZONE:
            case Constants.PVE_ZONE:
                zone.generateZoneObjects();
        }
    }

    public List<String[]> getVisibleZoneObjects(int zoneId) {
        return stageMap.get(zoneId).getVisibleZoneObjects();
    }

    public String[] findZoneObject(int zoneId, String keySubString) {
        return stageMap.get(zoneId).unHideZoneObject(keySubString);
    }

    public Player.PlayerXY navigate(int x, int y, Player.PlayerXY playerXY) {
        Player.PlayerXY newPlayerXY = stageMap.get(playerXY.getZoneId()).navigateTo(x, y, playerXY);
        Zone zone = stageMap.get(newPlayerXY.getZoneId());

        if (zone.getZoneProperty().getZoneSize().x < newPlayerXY.x)
            newPlayerXY.x = zone.getZoneProperty().getZoneSize().x;
        else if (newPlayerXY.x < 0)
            newPlayerXY.x = 0;

        if (zone.getZoneProperty().getZoneSize().y < newPlayerXY.y)
            newPlayerXY.y = zone.getZoneProperty().getZoneSize().y;
        else if (newPlayerXY.y < 0)
            newPlayerXY.y = 0;

        return newPlayerXY;
    }

    public Zone getZoneById(int zoneId) {
        return stageMap.get(zoneId);
    }

    public void showAllZoneObjects(int zoneId) {
        stageMap.get(zoneId).showAllObjects();

    }

    public int startPVECombat(PlayerAvatar playerAvatar, Player.PlayerXY currentPosition, String opponentName) {
        int result = stageMap.get(currentPosition.getZoneId()).startPVECombat(playerAvatar, currentPosition, opponentName);

        if (result == 0) {
            currentPosition.setZoneId(0);
            XY xy = stageMap.get(0).getSavePoint();
            currentPosition.x = xy.x;
            currentPosition.y = xy.y;
            playerAvatar.revive();
        }

        return result;
    }
}
