package com.dungeonexplorer.assets.character;

import com.dungeonexplorer.assets.GameObject;

class InventoryObject {

    private final GameObject gameObject;
    private int count;

    InventoryObject(GameObject gameObject) {
        this.gameObject = gameObject;
        count = 1;
    }

    GameObject getGameObject() {
        return gameObject;
    }

    int getCount() {
        return count;
    }


    boolean increaseCount() {
        this.count++;
        return true;
    }

    void decreaseCount() {
        this.count--;
    }

    public boolean equals(Object object) {
        if (object instanceof InventoryObject) {
            InventoryObject inventoryObject = (InventoryObject) object;
            return inventoryObject.getGameObject().getGameObjectId() == this.gameObject.getGameObjectId();
        } else
            return false;
    }

}
