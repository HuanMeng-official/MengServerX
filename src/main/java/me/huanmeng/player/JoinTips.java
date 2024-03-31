package me.huanmeng.player;

import me.huanmeng.MengServerX;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import static me.huanmeng.util.Abbreviations.M;

public class JoinTips implements Listener {
    Plugin config = MengServerX.getPlugin(MengServerX.class);
    @EventHandler
    public void PlayerOnJoin(PlayerJoinEvent event) {
        if (config.getConfig().getBoolean("JoinTip")) {
            Player player = event.getPlayer();
            String name = player.getName();
            event.setJoinMessage(ChatColor.YELLOW + M + ChatColor.RESET + "欢迎" + name + "加入服务器！");
        }
    }
}
