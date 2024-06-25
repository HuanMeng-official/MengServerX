package me.huanmeng.world;

import me.huanmeng.MengServerX;
import me.huanmeng.util.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import static me.huanmeng.util.Abbreviations.M;

public class DropCleaner {
    static Plugin plugin = MengServerX.getPlugin(MengServerX.class);

    public static void startCleanTask() {
        if (plugin.getConfig().getBoolean("DropCleaner")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    cleanDrops();
                }
            }.runTaskTimer(plugin, 0L, 12000L);
        }
    }

    private static void cleanDrops() {
        int itemCount = 0;
        for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Item) {
                    entity.remove();
                    itemCount++;
                }
            }
        }
        Bukkit.getLogger().info(TextColor.YELLOW + M + TextColor.RESET + "清理了" + itemCount + "个凋落物！");
    }
}
