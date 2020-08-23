package com.dungeonexplorer.middleware;

import com.dungeonexplorer.assets.character.PlayerAvatar;
import com.dungeonexplorer.assets.stage.Constants;
import com.dungeonexplorer.assets.stage.Stage;
import com.dungeonexplorer.assets.stage.XY;

import java.util.Collections;
import java.util.List;

public class Player {
    private final int id;
    private String name;
    private PlayerAvatar playerAvatar;
    private Stage currentStage;
    private PlayerXY currentPosition;


    public Player(int id, String name, PlayerAvatar playerAvatar, Stage currentStage, XY currentPosition) {
        this.id = id;
        this.name = name;
        this.playerAvatar = playerAvatar;
        this.currentStage = currentStage;
        this.currentPosition = new PlayerXY(currentPosition.getX(), currentPosition.getY());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PlayerAvatar getPlayerAvatar() {
        return playerAvatar;
    }

    public Stage getStage() {
        return currentStage;
    }

    public XY getPosition() {
        return currentPosition;
    }

    int getZoneId() {
        return currentPosition.zoneId;
    }

    String getZoneName() {
        return currentStage.getZoneName(currentPosition.zoneId);
    }

    String getZoneDescription() {
        return currentStage.getZoneDescription(currentPosition.zoneId);
    }

    boolean move(String[] commandString) {

        if (commandString.length != 2) {
            return false;
        }

        PlayerXY oldPosition = new PlayerXY(currentPosition);
        boolean movedZone = false;
        switch (commandString[1].toLowerCase()) {

            case "north":
            case "n":
                currentPosition = currentStage.navigate(0, Constants.MOVE_STEP, currentPosition);
                if (oldPosition.zoneId != currentPosition.zoneId)
                    movedZone = true;
                break;

            case "east":
            case "e":
                currentPosition = currentStage.navigate(Constants.MOVE_STEP, 0, currentPosition);
                if (oldPosition.zoneId != currentPosition.zoneId)
                    movedZone = true;
                break;

            case "south":
            case "s":
                currentPosition = currentStage.navigate(0, -Constants.MOVE_STEP, currentPosition);
                if (oldPosition.zoneId != currentPosition.zoneId)
                    movedZone = true;
                break;

            case "west":
            case "w":
                currentPosition = currentStage.navigate(-Constants.MOVE_STEP, 0, currentPosition);
                if (oldPosition.zoneId != currentPosition.zoneId)
                    movedZone = true;
        }

        if (oldPosition.zoneId == currentPosition.zoneId && oldPosition.getX() == currentPosition.getX() && oldPosition.getY() == currentPosition.getY())
            GameDriver.message("Huh?! What was that? Strange...");

        return movedZone;
    }

    List<String[]> look(String[] commandString) {


        if (commandString.length == 1) {
            return currentStage.getVisibleZoneObjects(currentPosition.zoneId);
        }

        int zoneId = -1;

        if (commandString.length != 2) {
            return null;
        }

        switch (commandString[1].toLowerCase()) {

            case "north":
            case "n":
                zoneId = currentStage.getZoneIdByDistance(currentPosition.zoneId, 0, Constants.MOVE_STEP);
                break;

            case "east":
            case "e":
                zoneId = currentStage.getZoneIdByDistance(currentPosition.zoneId, Constants.MOVE_STEP, 0);
                break;

            case "south":
            case "s":
                zoneId = currentStage.getZoneIdByDistance(currentPosition.zoneId, 0, -Constants.MOVE_STEP);
                break;

            case "west":
            case "w":
                zoneId = currentStage.getZoneIdByDistance(currentPosition.zoneId, -Constants.MOVE_STEP, 0);
                break;

            case "inv":
            case "inventory":

                List<String[]> inventory = playerAvatar.getInventoryList();
                if (inventory.size() != 0) {
                    inventory.add(0, new String[]{"Inventory List", ""});
                    inventory.add(new String[]{"Inventory Count: ", playerAvatar.getInventoryCount() + "/" + playerAvatar.getInventorySize()});
                } else
                    inventory.add(new String[]{"Inventory List", "Inventory is empty."});

                return inventory;

            case "equip":
            case "equipment":

                List<String[]> equipped = playerAvatar.getEquipped();
                if (equipped.size() != 0)
                    equipped.add(0, new String[]{"Equipped Items", ""});
                else
                    equipped.add(new String[]{"Equipped Items", "Nothing is equipped."});

                return equipped;
        }

        if (zoneId != -1) {
            String[] string = new String[2];
            string[0] = currentStage.getZoneName(zoneId);
            string[1] = currentStage.getZoneDescription(zoneId);
            return Collections.singletonList(string);
        }

        return null;

    }

    String[] search(String[] commandString) {

        if (commandString.length == 1)
            return currentStage.findZoneObject(currentPosition.zoneId, "");


        if (commandString.length != 2)
            return null;

        switch (commandString[1].toLowerCase()) {
            case "monster":
            case "treasure":
                return currentStage.findZoneObject(currentPosition.zoneId, commandString[1].toLowerCase());
        }

        return null;
    }

    int pickUp(String[] commandString) {

        String name = "";

        if (commandString.length > 1) {
            name = toValidName(commandString);
        }
        return playerAvatar.pickUpTreasure(currentStage.getZoneById(currentPosition.zoneId), name.toLowerCase());

    }

    public boolean equip(String[] commandString) {
        if (commandString.length == 1)
            return false;

        return playerAvatar.equip(toValidName(commandString).toLowerCase());


    }

    public boolean unequip(String[] commandString) {

        String name = "";
        if (commandString.length > 1)
            name = toValidName(commandString);

        return playerAvatar.unequip(name.toLowerCase());

    }

    public int attack(String[] commandString) {

        if (commandString.length <= 1)
            return -2;

        String name = toValidName(commandString);
        return currentStage.startPVECombat(playerAvatar, currentPosition, name.toLowerCase());
    }

    private String toValidName(String[] commandString) {
        String name = "";
        for (int i = 1; i < commandString.length; i++)
            name = name.concat(commandString[i] + " ");
        name = name.trim();
        return name;
    }


    public static class PlayerXY extends XY {

        private int zoneId;

        private PlayerXY(int x, int y) {
            super(x, y);
        }

        private PlayerXY(PlayerXY playerXY) {
            super(playerXY.x, playerXY.y);
            this.zoneId = playerXY.zoneId;
        }

        public void setZoneId(int zoneId) {
            if (zoneId < 0)
                GameDriver.message("Warning: " + this.getClass() + " : Trying to set zoneId to " + zoneId);
            this.zoneId = zoneId;
        }

        public int getZoneId() {
            return zoneId;
        }
    }

}

