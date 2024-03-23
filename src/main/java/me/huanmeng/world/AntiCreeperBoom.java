package me.huanmeng.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import me.huanmeng.MengServerX;

public class AntiCreeperBoom implements Listener {
    Plugin plugin = MengServerX.getPlugin(MengServerX.class);
    @EventHandler
    public void onCreeperBoom(EntityExplodeEvent event){
        if (event.getEntityType().name().equals("CREEPER")) {
            event.setCancelled(plugin.getConfig().getBoolean("AntiCreeperBoom"));
        }
    }
}
