package me.huanmeng.world;

import me.huanmeng.MengServerX;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class KeepInventory implements Listener {

    Plugin config = MengServerX.getPlugin(MengServerX.class);
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        if (config.getConfig().getBoolean("KeepInventory")) {
            e.setKeepInventory(true);
            e.getDrops().clear();
            e.setKeepLevel(true);
            e.setDroppedExp(0);
        }
    }
}
