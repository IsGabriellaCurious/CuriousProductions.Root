package me.cps.root.punish;

import me.cps.root.account.AccountHub;
import me.cps.root.punish.gui.BanGUI;
import me.cps.root.punish.gui.DurationGUI;
import me.cps.root.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Curious Productions Root
 * Punish Manager - Punish GUI Handlers (1.8)
 *
 * Handles all the clicking inside the punish GUIs
 * TODO redo this code, i really hate it
 *
 * @author  Gabriella Hotten
 * @since   2020-05-10
 */
public class PunishInventoryHandlers18 implements Listener {

    //Punish GUI
    @EventHandler
    public void handlePG(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            ClickType clickType = event.getClick();
            Inventory inventory = event.getClickedInventory();
            ItemStack item = event.getCurrentItem();
            PunishData data = PunishManager.getInstance().getPlayerPunishing().get(player);

            if (inventory == null)
                return;

            if (!inventory.getName().equalsIgnoreCase("§cPunish " + data.getTarget()) || inventory.getName() == null)
                return;

            if (item == null || !item.hasItemMeta()) {
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, (float) 0.6);
                event.setCancelled(true);
                return;
            }

            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§cBan " + data.getTarget())) {
                event.setCancelled(true);
                PunishManager.getInstance().getSwitching().add(player);
                player.closeInventory();
                BanGUI.openGUI(player, data);
            } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§9Mute " + data.getTarget())) {
                event.setCancelled(true);
                //PunishManager.getInstance().getSwitching().add(player);
                //player.closeInventory();
                //open mute inv
            } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_PURPLE + "Warn " + data.getTarget())) {
                event.setCancelled(true);
                //PunishManager.getInstance().getSwitching().add(player);
                //player.closeInventory();
                //open warn inv
            } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lATTENTION")){
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, (float) 0.6);
                event.setCancelled(true);
            } else {
                //assuming its a punishment
                if (item.getItemMeta().hasEnchants()) {
                    String punishmentId = item.getItemMeta().getDisplayName();
                    PunishManager.getInstance().getRemovingReason().put(player, punishmentId);
                    player.closeInventory();
                    player.sendMessage("§aPlease enter the reason you are removing this punishment. Type \"cancel\" to cancel.");
                } else {
                    player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, (float) 0.6);
                    event.setCancelled(true);
                }
            }
        } catch (Exception e) {
            System.out.println("");
        }

    }

    @EventHandler
    public void closePG(InventoryCloseEvent event) {
        try {
            Player player = (Player) event.getPlayer();
            Inventory inv = event.getInventory();

            if (inv == null)
                return;

            if (!inv.getName().equalsIgnoreCase("§cPunish " + PunishManager.getInstance().getPlayerPunishing().get(player)) || inv.getName() == null)
                return;

            if (PunishManager.getInstance().getSwitching().contains(player))
                return;
            PunishManager.getInstance().getPlayerPunishing().remove(player);
            player.sendMessage("§cCancelled!");
        } catch (Exception e) {
            System.out.println("");
        }

    }

    //Duration GUI
    @EventHandler
    public void handleDG(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            ClickType clickType = event.getClick();
            Inventory inventory = event.getClickedInventory();
            ItemStack item = event.getCurrentItem();
            PunishData data = PunishManager.getInstance().getPlayerPunishing().get(player);

            if (inventory == null)
                return;

            if (!inventory.getName().equalsIgnoreCase("§cSelect a duration") || inventory.getName() == null)
                return;

            if (item == null || !item.hasItemMeta()) {
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, (float) 0.6);
                event.setCancelled(true);
                return;
            }

            PunishDuration duration = PunishDuration.parseFromDisplayName(item.getItemMeta().getDisplayName());
            data.setDuration(duration);
            PunishManager.getInstance().updateData(player, data);

            PunishManager.getInstance().punish(AccountHub.getInstance().uuidFromName(data.getTarget()), data.getPlayer(), data.getType(), data.getReason(), data.getDuration(), false);

            PunishManager.getInstance().getSwitching().add(player);
            player.closeInventory();
            PunishManager.getInstance().getSwitching().remove(player);
            PunishManager.getInstance().getPlayerPunishing().remove(player);
        } catch (Exception e) {
            System.out.println("");
        }

    }

    @EventHandler
    public void closeDG(InventoryCloseEvent event) {
        try {
            Player player = (Player) event.getPlayer();
            Inventory inv = event.getInventory();

            if (inv == null)
                return;

            if (!inv.getName().equalsIgnoreCase("§cSelect a duration") || inv.getName() == null)
                return;

            if (PunishManager.getInstance().getSwitching().contains(player))
                return;

            PunishManager.getInstance().getPlayerPunishing().remove(player);
            player.sendMessage("§cCancelled!");
        } catch (Exception e) {
            System.out.println("");
        }

    }

    //Ban GUI
    @EventHandler
    public void handleBG(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            ClickType clickType = event.getClick();
            Inventory inventory = event.getClickedInventory();
            ItemStack item = event.getCurrentItem();
            PunishData data = PunishManager.getInstance().getPlayerPunishing().get(player);

            if (inventory == null)
                return;

            if (!inventory.getName().equalsIgnoreCase("§cSelect a reason") || inventory.getName() == null)
                return;

            if (item == null || !item.hasItemMeta()) {
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, (float) 0.6);
                event.setCancelled(true);
                return;
            }

            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("Other")) {
                PunishManager.getInstance().getSwitching().add(player);
                event.setCancelled(true);
                player.closeInventory();
                PunishManager.getInstance().getSettingReason().put(player, data);
                player.sendMessage("§aPlease enter the reason for this punishment. Type \"cancel\" to cancel.");
            } else {
                data.setReason(item.getItemMeta().getDisplayName());
                PunishManager.getInstance().updateData(player, data);

                PunishManager.getInstance().getSwitching().add(player);
                event.setCancelled(true);
                player.closeInventory();
                DurationGUI.openGUI(player, data);
            }
        } catch (Exception e) {
            System.out.println("");
        }

    }

    @EventHandler
    public void closeBG(InventoryCloseEvent event) {
        try {
            Player player = (Player) event.getPlayer();
            Inventory inv = event.getInventory();

            if (inv == null)
                return;

            if (!inv.getName().equalsIgnoreCase("§cSelect a reason") || inv.getName() == null)
                return;

            if (PunishManager.getInstance().getSwitching().contains(player))
                return;
            PunishManager.getInstance().getPlayerPunishing().remove(player);
            player.sendMessage("§cCancelled!");
        } catch (Exception e) {
            System.out.println("");
        }

    }
}
