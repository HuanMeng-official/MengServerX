package me.huanmeng.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import static me.huanmeng.MengServerX.getMessage;

public class SendNotice implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()){
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + getMessage("notice-info") + ChatColor.GRAY + ">> " + ChatColor.RESET + args[0]);
        } else if (sender instanceof ConsoleCommandSender) {
            Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + getMessage("notice-info") + ChatColor.GRAY + ">> " + ChatColor.RESET + args[0]);
        }
        return false;
    }
}
