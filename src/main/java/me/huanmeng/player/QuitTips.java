package me.huanmeng.player;

import me.huanmeng.MengServerX;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import static me.huanmeng.util.Abbreviations.M;

public class QuitTips implements Listener {
    Plugin config = MengServerX.getPlugin(MengServerX.class);
    @EventHandler
    public void PlayerOnQuit(PlayerQuitEvent e) {
        if (config.getConfig().getBoolean("QuitTip")) {
            Player player = e.getPlayer();
            String name = player.getName();
            e.setQuitMessage(ChatColor.YELLOW + M + ChatColor.RESET + name + " 退出了游戏!");
        }
    }
}
