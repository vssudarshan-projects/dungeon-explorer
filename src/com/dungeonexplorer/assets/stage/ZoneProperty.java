package com.dungeonexplorer.assets.stage;

import com.dungeonexplorer.middleware.GameDriver;

import java.util.HashMap;
import java.util.Map;

public class ZoneProperty {

    private final int type;
    private final XY zoneSize;
    private final Map<Integer, ZonePortal> zonePortals;

    public ZoneProperty(int type, XY zoneSize) {
        this.type = type;
        if (zoneSize.x <= 0)
            zoneSize.x = 1;

        zoneSize.x = (zoneSize.x - 1) * Constants.MOVE_STEP;

        if (zoneSize.y <= 0)
            zoneSize.y = 1;

        zoneSize.y = (zoneSize.y - 1) * Constants.MOVE_STEP;
        this.zoneSize = zoneSize;
        this.zonePortals = new HashMap<>();
    }

    int getType() {
        return type;
    }

    public XY getZoneSize() {
        return zoneSize;
    }

    Map<Integer, ZonePortal> getZonePortals() {
        return zonePortals;
    }

    public void addZonePortal(ZonePortal zonePortal) {
        if (zonePortal.getPortalId() != 0 && zonePortal.getDestZonePortal() == null) {
            GameDriver.message("Warning: " + "[" + this.getClass().getName() + "]" + " [ZoneId: " + zonePortal.getZoneId() + "]" + " [PortalId: " + zonePortal.getPortalId() + "]" + " portal destination is null.");
            System.exit(-1);
        } else
            this.zonePortals.put(zonePortal.getPortalId(), zonePortal);

    }
}

