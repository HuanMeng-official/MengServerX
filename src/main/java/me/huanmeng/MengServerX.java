package me.huanmeng;

import me.huanmeng.player.ChatFormat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
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
    }

    @Override
    public void onDisable() {
        log.info(N + "Plugin is disable");
    }

    private static final Logger log = Bukkit.getLogger();
}
