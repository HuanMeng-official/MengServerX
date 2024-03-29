package me.huanmeng;

import me.huanmeng.command.*;
import me.huanmeng.event.JoinGame;
import me.huanmeng.player.ChatFormat;
import me.huanmeng.world.AntiCreeperBoom;
import me.huanmeng.world.KeepInventory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;
import static me.huanmeng.util.Abbreviations.*;
import static me.huanmeng.util.TextColor.*;

public final class MengServerX extends JavaPlugin {

    public void onLoad() {
        log.info(N + GREEN + "Plugin is running on spigot " + YELLOW + V + RESET);
    }

    @Override
    public void onEnable() {
        log.info(N + "Plugin is enable");
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChatFormat(), this);
        getServer().getPluginManager().registerEvents(new JoinGame(), this);
        getServer().getPluginManager().registerEvents(new KeepInventory(), this);
        getServer().getPluginManager().registerEvents(new AntiCreeperBoom(), this);
        Objects.requireNonNull(getCommand("msx_help")).setExecutor(new CommandHelp());
        Objects.requireNonNull(getCommand("msx_wl_add")).setExecutor(new WhiteListAdd());
        Objects.requireNonNull(getCommand("msx_wl_remove")).setExecutor(new WhiteListRemove());
        Objects.requireNonNull(getCommand("msx_seed")).setExecutor(new GetWorldSeed());
        Objects.requireNonNull(getCommand("msx_gm")).setExecutor(new SetGameMode());
        Objects.requireNonNull(getCommand("msx_sn")).setExecutor(new SendNotice());
    }

    @Override
    public void onDisable() {
        log.info(N + "Plugin is disable");
    }

    private static final Logger log = Bukkit.getLogger();
}
