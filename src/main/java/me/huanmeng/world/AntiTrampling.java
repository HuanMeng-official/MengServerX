package me.huanmeng.world;

import me.huanmeng.MengServerX;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class AntiTrampling implements Listener {
    @EventHandler
    public void onFarmlandTrample(PlayerInteractEvent event) {
        Plugin config = MengServerX.getPlugin(MengServerX.class);
        if (config.getConfig().getBoolean("AntiTrampling")) {
            if (event.getAction() == Action.PHYSICAL && event.getClickedBlock() != null && event.getClickedBlock().getType().toString().equals("FARMLAND")) {
                event.setCancelled(true);
            }
        }
    }
}
