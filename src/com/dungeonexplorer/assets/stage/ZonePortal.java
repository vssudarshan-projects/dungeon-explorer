package com.dungeonexplorer.assets.stage;

import com.dungeonexplorer.middleware.GameDriver;

public class ZonePortal {
    private final int zoneId;
    private final int portalId;
    private final XY position;
    private ZonePortal destZonePortal;

    public ZonePortal(int zoneId, int portalId, XY position, ZonePortal destZonePortal) {
        this.zoneId = zoneId;
        this.portalId = portalId;
        this.position = position;
        this.destZonePortal = destZonePortal;
    }

    int getZoneId() {
        return zoneId;
    }

    int getPortalId() {
        return portalId;
    }

    XY getPosition() {
        return position;
    }

    ZonePortal getDestZonePortal() {
        return destZonePortal;
    }

    public void setDestZonePortal(ZonePortal destZonePortal) {
        if (destZonePortal == null) {
            GameDriver.message("Warning: " + "[" + this.getClass().getName() + "]" + " [ZoneId: " + zoneId + "] " + "[PortalId: "  + portalId + "]" + " new destination is null.");
            System.exit(-1);
        } else
            this.destZonePortal = destZonePortal;
    }
}
