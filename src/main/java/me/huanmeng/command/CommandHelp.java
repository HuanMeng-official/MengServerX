package me.huanmeng.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.huanmeng.MengServerX.getMessage;

public class CommandHelp implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.GREEN + getMessage("command-help_wl") + "\n" + ChatColor.YELLOW + getMessage("command-help_add_wl") + "\n" + getMessage("command-help_rm_wl"));
            player.sendMessage(ChatColor.GREEN + getMessage("command-help_util") + "\n" + ChatColor.YELLOW + getMessage("command-help_seed_util") + "\n" + getMessage("command-help_sn_util") + "\n" + getMessage("command-help_gm_util"));
            player.sendMessage(ChatColor.GREEN + getMessage("command-help_other") + "\n" + ChatColor.YELLOW + getMessage("command-help_help_other"));
        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            console.sendMessage(ChatColor.GREEN + getMessage("command-help_wl") + "\n" + ChatColor.YELLOW + getMessage("command-help_add_wl") + "\n" + getMessage("command-help_rm_wl"));
            console.sendMessage(ChatColor.GREEN + getMessage("command-help_util") + "\n" + ChatColor.YELLOW + getMessage("command-help_seed_util") + "\n" + getMessage("command-help_sn_util") + "\n" + getMessage("command-help_gm_util"));
            console.sendMessage(ChatColor.GREEN + getMessage("command-help_other") + "\n" + ChatColor.YELLOW + getMessage("command-help_help_other"));
        }
        return false;
    }
}
