package me.huanmeng.world;

import me.huanmeng.MengServerX;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BreakBoard implements Listener {
    private static Map<String, Integer> playerBlockCount = new HashMap<>();
    private static ScoreboardManager manager;
    private static Scoreboard board;
    private static Objective objective;
    private static File dataFile;
    private static FileConfiguration dataConfig;
    static Plugin config = MengServerX.getPlugin(MengServerX.class);

    public static void enableBoard() {
        if (config.getConfig().getBoolean("Board")) {
            manager = Bukkit.getScoreboardManager();
            board = manager.getNewScoreboard();
            objective = board.registerNewObjective("blockCount", "dummy", Objects.requireNonNull(config.getConfig().getString("BreakBoardName")));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            dataFile = new File(JavaPlugin.getPlugin(MengServerX.class).getDataFolder(), "data_break.yml");

            if (!dataFile.exists()) {
                dataFile.getParentFile().mkdirs();
                config.saveResource("data_break.yml", false);
            }

            dataConfig = YamlConfiguration.loadConfiguration(dataFile);
            loadBlockCounts();
        }
    }

    public static void disableBoard() {
        if (config.getConfig().getBoolean("BreakBoard")) {
            saveBlockCounts();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String playerName = event.getPlayer().getName();
        int count = playerBlockCount.getOrDefault(playerName, 0) + 1;
        playerBlockCount.put(playerName, count);
        Score score = objective.getScore(playerName);
        score.setScore(count);
        event.getPlayer().setScoreboard(board);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        int count = playerBlockCount.getOrDefault(playerName, 0);
        Score score = objective.getScore(playerName);
        score.setScore(count);
        event.getPlayer().setScoreboard(board);
    }

    private static void loadBlockCounts() {
        for (String key : dataConfig.getKeys(false)) {
            int count = dataConfig.getInt(key);
            playerBlockCount.put(key, count);
            Score score = objective.getScore(key);
            score.setScore(count);
        }
    }

    private static void saveBlockCounts() {
        for (Map.Entry<String, Integer> entry : playerBlockCount.entrySet()) {
            dataConfig.set(entry.getKey(), entry.getValue());
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
