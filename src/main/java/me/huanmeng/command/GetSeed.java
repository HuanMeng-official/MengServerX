package me.huanmeng.command;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.huanmeng.util.Abbreviations.M;

public class GetSeed implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            World world = player.getWorld();
            long seed = world.getSeed();
            player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "Seed: " + ChatColor.GREEN + seed);
        }
        return false;
    }
}
