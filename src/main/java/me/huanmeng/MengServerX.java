package me.huanmeng;

import me.huanmeng.command.WhiteListAdd;
import me.huanmeng.command.WhiteListRemove;
import me.huanmeng.event.JoinGame;
import me.huanmeng.player.ChatFormat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;
import static me.huanmeng.util.Abbreviations.*;
import static me.huanmeng.util.TextColor.*;

public final class MengServerX extends JavaPlugin {

    public void onLoad() {
        log.info(N + GREEN + "Plugin is running on spigot " + YELLOW + V);
    }

    @Override
    public void onEnable() {
        log.info(N + "Plugin is enable");
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChatFormat(), this);
        getServer().getPluginManager().registerEvents(new JoinGame(), this);
        Objects.requireNonNull(getCommand("msx_add")).setExecutor(new WhiteListAdd());
        Objects.requireNonNull(getCommand("msx_remove")).setExecutor(new WhiteListRemove());
    }

    @Override
    public void onDisable() {
        log.info(N + "Plugin is disable");
    }

    private static final Logger log = Bukkit.getLogger();
}
