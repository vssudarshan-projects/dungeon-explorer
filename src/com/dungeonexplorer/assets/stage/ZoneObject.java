package com.dungeonexplorer.assets.stage;

import com.dungeonexplorer.assets.GameObject;

public class ZoneObject {

    private final String key;
    private final GameObject gameObject;
    private boolean isHidden;
    private final boolean isPersistent;

    public ZoneObject(String key, GameObject gameObject, boolean isHidden, boolean isPersistent) {
        this.key = key;
        this.gameObject = gameObject;
        this.isHidden = isHidden;
        this.isPersistent = isPersistent;
    }

    String getKey() {
        return key;
    }

    GameObject getGameObject() {
        return gameObject;
    }

    boolean isHidden() {
        return isHidden;
    }

    boolean isPersistent() {
        return isPersistent;
    }


    void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

}
