package com.dungeonexplorer.assets.character;

import com.dungeonexplorer.assets.GameObject;
import com.dungeonexplorer.assets.equipment.Armour;
import com.dungeonexplorer.assets.equipment.Scroll;
import com.dungeonexplorer.assets.equipment.Weapon;
import com.dungeonexplorer.assets.stage.Zone;
import com.dungeonexplorer.middleware.GameDriver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PlayerAvatar extends Character {

    private final Inventory inventory;

//    public PlayerAvatar() {
//
//        this.inventory = new Inventory();
//
//    }

    public PlayerAvatar(int playerAvatarId, String name, Stats stats, Weapon weapon, Armour armour, List<Scroll> scrolls, Inventory inventory, boolean isVulnerable) {
        super(playerAvatarId, name, stats, weapon, armour, scrolls, isVulnerable);
        this.inventory = inventory == null ? new Inventory() : inventory;
    }

    public int getInventoryCount() {
        return inventory.currentInventoryCount;
    }

    public int getInventorySize() {
        return inventory.getInventorySize();
    }

    public List<String> getCharDetails() {
        return super.getCharDetails("");
    }

    public ArrayList<String[]> getEquipped() {

        ArrayList<String[]> equippedList = new ArrayList<>();

        if (getArmour() != null)
            equippedList.add(new String[]{getArmour().getName(), getArmour().getDescription()});

        if (getWeapon() != null)
            equippedList.add(new String[]{getWeapon().getName(), getWeapon().getDescription()});

        List<Scroll> scrolls = getScrolls();
        int index = 0;
        for (String string : getSlotScrolls()) {
            equippedList.add(new String[]{string, scrolls.get(index++).getDescription()});
        }

        return equippedList;
    }

    public ArrayList<String[]> getInventoryList() {

        ArrayList<String[]> inventoryList = new ArrayList<>();
        String[] inventoryObjectString;

        for (InventoryObject inventoryObject : inventory.inventoryObjects) {
            inventoryObjectString = new String[2];
            inventoryObjectString[0] = inventoryObject.getGameObject().getName();
            inventoryObjectString[1] = String.valueOf(inventoryObject.getCount());
            inventoryList.add(inventoryObjectString);
        }

        return inventoryList;
    }

    public List<String[]> getCombatStatus() {

        List<String[]> status = new ArrayList<>();
        status.add(super.getVitals());
        status.add(getSlotScrolls());

        return status;
    }




    public int pickUpTreasure(Zone zone, String name) {
        List<GameObject> gameObjects;
        gameObjects = zone.getTreasures(name);
        int count = 0;
        if (gameObjects.size() != 0) {
            for (GameObject gameObject : gameObjects) {
                if (addToInventory(gameObject))
                    count++;
                else
                    break;
            }
        }
        return zone.removeTreasures(name, count);
    }

    public boolean equip(String name) {

        ListIterator<InventoryObject> li = inventory.inventoryObjects.listIterator();
        boolean equipped = false;
        while (li.hasNext()) {
            InventoryObject inventoryObject = li.next();
            if (inventoryObject.getGameObject().getName().toLowerCase().equals(name)) {
                removeInventory(inventoryObject, 1);
                if (inventoryObject.getGameObject() instanceof Armour) {
                    if (super.getArmour() != null) {
                        addToInventory(super.getArmour());
                    }
                    super.setArmour((Armour) inventoryObject.getGameObject());
                    equipped = true;
                } else if (inventoryObject.getGameObject() instanceof Weapon) {
                    if (super.getWeapon() != null) {
                        addToInventory(super.getWeapon());
                    }
                    super.setWeapon((Weapon) inventoryObject.getGameObject());
                    equipped = true;

                } else if (inventoryObject.getGameObject() instanceof Scroll) {
                    if (super.getScrolls().size() >= Constants.MAX_EQUIPPED_SCROLL) {
                        addToInventory(super.getScrolls().get(0));
                        super.removeTopScroll();
                    }
                    super.addScrolls((Scroll) inventoryObject.getGameObject());
                    equipped = true;
                }
                break;
            }
        }
        return equipped;

    }

    public boolean unequip(String name) {

        boolean unequipped = false;
        if (name.equals("")) {
            if (getArmour() != null) {
                addToInventory(getArmour());
                setArmour(null);
                unequipped = true;
            }
            if (getWeapon() != null) {
                addToInventory(getWeapon());
                setWeapon(null);
                unequipped = true;
            }
            for (Scroll scroll : getScrolls()) {
                addToInventory(scroll);
                unequipped = true;
            }
            removeScrolls();
        }

        GameObject gameObject = null;

        switch (name) {
            case "armour":
                gameObject = getArmour();
                setArmour(null);
                break;

            case "weapon":
                gameObject = getWeapon();
                setWeapon(null);
                break;

            default:
                if (name.startsWith("scroll")) {
                    int slotNumber;
                    try {
                        slotNumber = Integer.parseInt(name.substring(6));
                    } catch (NumberFormatException exception) {
                        slotNumber = -1;
                    }
                    if (slotNumber > 0 && slotNumber <= getScrolls().size() && getScrolls().size() != 0) {
                        gameObject = getScrolls().get(slotNumber - 1);
                        removeScroll(slotNumber);
                    }
                }

        }

        if (gameObject != null) {
            addToInventory(gameObject);
            unequipped = true;
        }

        return unequipped;
    }

    public boolean attack(Character character) {

        if (character instanceof Monster) {
            Monster target = (Monster) character;

            showActions();

            String playerAction = GameDriver.readUserAction();

            if (playerAction.toLowerCase().equals("attack")) {
                useWeapon(target);

            } else if (playerAction.toLowerCase().startsWith("scroll")) {
                int slotNumber;

                try {
                    slotNumber = Integer.parseInt(playerAction.substring(6));
                } catch (NumberFormatException exception) {
                    slotNumber = -1;
                }
                if (slotNumber > 0 && this.getScrolls().size() > 0)
                    useScroll(slotNumber, target);
            }else if(playerAction.toLowerCase().equals("flee")){
                return Constants.FLEE == randomize(Constants.FLEE_CHANCE);
            }

        }
        return false;
    }

    private boolean addToInventory(GameObject gameObject) {

        if (gameObject != null) {
            if (inventory.currentInventoryCount < inventory.getInventorySize()) {
                InventoryObject inventoryObject = new InventoryObject(gameObject);
                int index = inventory.inventoryObjects.indexOf(inventoryObject);

                if (index == -1) {
                    inventory.currentInventoryCount++;
                    return inventory.inventoryObjects.add(inventoryObject);

                } else {

                    if (inventoryObject.getGameObject() instanceof Armour ||
                            inventoryObject.getGameObject() instanceof Weapon ||
                            inventoryObject.getCount() % Constants.MAX_STACKABLE_COUNT == 0)
                        inventory.currentInventoryCount++;

                    return inventory.inventoryObjects.get(index).increaseCount();
                }
            }
        }

        return false;
    }

    //later allow to specify number of specified item to remove
    private void removeInventory(InventoryObject inventoryObject, int count) {

        int index = inventory.inventoryObjects.indexOf(inventoryObject);
        InventoryObject indexedInventory = inventory.inventoryObjects.get(index);
        while (count-- > 0) {
            if (inventoryObject.getGameObject() instanceof Armour || inventoryObject.getGameObject() instanceof Weapon) {

                inventory.currentInventoryCount--;

            } else if (inventoryObject.getGameObject() instanceof Scroll) {

                if (indexedInventory.getCount() - 1 % Constants.MAX_STACKABLE_COUNT == 0)
                    inventory.currentInventoryCount--;
            }

            indexedInventory.decreaseCount();

            if (indexedInventory.getCount() == 0)
                inventory.inventoryObjects.remove(index);

        }
    }




    private void showActions() {

        GameDriver.message("\nAvailable Actions\n" +
                "1. attack => attack with equipped weapon.\n" +
                "2. scroll => use scroll => scroll1 | scroll2\n");
    }


    private static class Inventory {

        private final LinkedList<InventoryObject> inventoryObjects;
        private final int baseInventorySize;
        private int currentInventoryCount;
        private int expansionCount;

        private Inventory() {
            this.inventoryObjects = new LinkedList<>();
            this.baseInventorySize = Constants.BASE_INVENTORY_SIZE;
            currentInventoryCount = 0;
            expansionCount = 0;
        }

        int getInventorySize() {
            return baseInventorySize + Constants.INVENTORY_EXPANSION_SIZE * expansionCount;
        }

    }


}
