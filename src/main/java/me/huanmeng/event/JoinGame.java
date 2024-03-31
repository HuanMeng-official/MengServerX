package me.huanmeng.event;

import me.huanmeng.MengServerX;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Objects;

public class JoinGame implements Listener {
    Plugin config = MengServerX.getPlugin(MengServerX.class);
    @EventHandler
    public void PlayerOnLogin(PlayerLoginEvent event){
        config.reloadConfig();
        if(config.getConfig().getBoolean("WhiteList")){
            String loginPlayer = event.getPlayer().getName();
            List<String> wedPlayer = config.getConfig().getStringList("List");
            if(!wedPlayer.contains(loginPlayer)){
                event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
                event.disallow(event.getResult(), ChatColor.translateAlternateColorCodes
                        ('&', Objects.requireNonNull(
                                config.getConfig().getString("NotAllow"))
                        )
                );
            } else {
                event.allow();
            }
        }
    }
}
