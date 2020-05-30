package me.cps.root.server;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceInfoUpdateEvent;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;

/**
 * Curious Productions Root
 * Server Manager - Cloud Service Info Update Handler
 *
 * Listens for, and handles when a server gets more players and when a server posts it's version.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-07
 */
public class CloudServiceInfoUpdateHandler{

    @EventListener
    public void handle(CloudServiceInfoUpdateEvent event) {
        if (!ServerManager.getInstance().getServiceId().getName().equalsIgnoreCase(event.getServiceInfo().getServiceId().getName()))
            return;

        ServerManager.getInstance().setOnlineCount(ServiceInfoSnapshotUtil.getOnlineCount(event.getServiceInfo()));

        ServerManager.getInstance().setGameVersion(ServiceInfoSnapshotUtil.getVersion(event.getServiceInfo()));
    }

}
