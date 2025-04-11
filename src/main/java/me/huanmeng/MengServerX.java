package me.huanmeng;

import me.huanmeng.command.*;
import me.huanmeng.command.tp.*;
import me.huanmeng.event.JoinGame;
import me.huanmeng.player.ChatFormat;
import me.huanmeng.player.JoinTips;
import me.huanmeng.player.QuitTips;
import me.huanmeng.world.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;
import static me.huanmeng.util.Abbreviations.*;
import static me.huanmeng.util.TextColor.*;

public final class MengServerX extends JavaPlugin {
    private static FileConfiguration messages;

    public void onLoad() {
        log.info(N + GREEN + "Plugin is running on spigot " + YELLOW + V + RESET);
    }

    @Override
    public void onEnable() {
        loadMessages();
        log.info(N + "Plugin is enable");
        log.info(GREEN + getMessage("lang-info") + RESET);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        DropCleaner.startCleanTask();
        getServer().getPluginManager().registerEvents(new ChatFormat(), this);
        getServer().getPluginManager().registerEvents(new JoinGame(), this);
        getServer().getPluginManager().registerEvents(new JoinTips(), this);
        getServer().getPluginManager().registerEvents(new QuitTips(), this);
        getServer().getPluginManager().registerEvents(new KeepInventory(), this);
        getServer().getPluginManager().registerEvents(new AntiCreeperBoom(), this);
        getServer().getPluginManager().registerEvents(new AntiTrampling(), this);
        getServer().getPluginManager().registerEvents(new BreakBoard(), this);
        BreakBoard.enableBoard();
        Objects.requireNonNull(getCommand("msx_help")).setExecutor(new CommandHelp());
        Objects.requireNonNull(getCommand("msx_wl_add")).setExecutor(new WhiteListAdd());
        Objects.requireNonNull(getCommand("msx_wl_remove")).setExecutor(new WhiteListRemove());
        Objects.requireNonNull(getCommand("msx_seed")).setExecutor(new GetWorldSeed());
        Objects.requireNonNull(getCommand("msx_gm")).setExecutor(new SetGameMode());
        Objects.requireNonNull(getCommand("msx_sn")).setExecutor(new SendNotice());
        Objects.requireNonNull(getCommand("msx_tp")).setExecutor(new Teleport());
        Objects.requireNonNull(getCommand("msx_accept")).setExecutor(new TeleportAccept());
        Objects.requireNonNull(getCommand("msx_deny")).setExecutor(new TeleportDeny());
        Objects.requireNonNull(getCommand("msx_ai")).setExecutor(new OpenAIChat(getConfig()));
    }

    @Override
    public void onDisable() {
        log.info(N + "Plugin is disable");
        BreakBoard.disableBoard();
    }

    private static final Logger log = Bukkit.getLogger();

    private void loadMessages() {
        String language = getConfig().getString("Language", "en");
        File messagesFile = new File(getDataFolder(), "lang_" + language + ".yml");

        if (!messagesFile.exists()) {
            saveResource("lang_" + language + ".yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public static String getMessage(String key) {
        return messages.getString(key, "Language file not found: " + key);
    }
}
