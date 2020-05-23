package me.cps.root.server;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceInfoUpdateEvent;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;

public class CloudServiceInfoUpdateHandler{

    @EventListener
    public void handle(CloudServiceInfoUpdateEvent event) {
        if (!ServerManager.getInstance().getServiceId().getName().equalsIgnoreCase(event.getServiceInfo().getServiceId().getName()))
            return;

        ServerManager.getInstance().setOnlineCount(ServiceInfoSnapshotUtil.getOnlineCount(event.getServiceInfo()));

        ServerManager.getInstance().setGameVersion(ServiceInfoSnapshotUtil.getVersion(event.getServiceInfo()));
    }

}
