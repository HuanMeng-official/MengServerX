package me.huanmeng.player;

import me.huanmeng.MengServerX;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
public class ChatFormat implements Listener {
    Plugin config = MengServerX.getPlugin(MengServerX.class);
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (config.getConfig().getBoolean("ChatFormat")) {
            e.setFormat(
                    ChatColor.GRAY + "[" +
                            ChatColor.DARK_PURPLE + "Level: " +
                            ChatColor.GOLD +
                            player.getWorld().getName() +
                            ChatColor.GRAY + "]" +
                            ChatColor.AQUA + player.getName() +
                            ChatColor.WHITE + ": " +
                            ChatColor.RESET + e.getMessage()
            );
        }
    }
}