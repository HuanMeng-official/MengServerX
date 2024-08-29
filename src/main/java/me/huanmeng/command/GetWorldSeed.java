package me.huanmeng.command;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.huanmeng.util.Abbreviations.M;
import static me.huanmeng.MengServerX.getMessage;

public class GetWorldSeed implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            World world = player.getWorld();
            long seed = world.getSeed();
            player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("seed-info") + ChatColor.GREEN + seed);
        }
        return false;
    }
}
