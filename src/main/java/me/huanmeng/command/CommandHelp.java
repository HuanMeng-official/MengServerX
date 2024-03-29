package me.huanmeng.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandHelp implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.GREEN + "----白名单----\n" + ChatColor.YELLOW + "/msx_wl_add <player> - 添加白名单\n" + "/msx_wl_remove <player> - 移除白名单");
            player.sendMessage(ChatColor.GREEN + "----实用功能----\n" + ChatColor.YELLOW + "/msx_seed - 查询所在世界的种子\n" + "/msx_sn - 发送全服通告\n" + "/msx_gm <number> - 切换游戏模式");
            player.sendMessage(ChatColor.GREEN + "----其他功能----\n" + ChatColor.YELLOW + "/msx_help - 命令帮助");
        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            console.sendMessage(ChatColor.GREEN + "----白名单----\n" + ChatColor.YELLOW + "/msx_wl_add <player> - 添加白名单\n" + "/msx_wl_remove <player> - 移除白名单");
            console.sendMessage(ChatColor.GREEN + "----实用功能----\n" + ChatColor.YELLOW + "/msx_seed - 查询所在世界的种子\n" + "/msx_sn - 发送全服通告\n" + "/msx_gm <number> - 切换游戏模式");
            console.sendMessage(ChatColor.GREEN + "----其他功能----\n" + ChatColor.YELLOW + "/msx_help - 命令帮助");
        }
        return false;
    }
}
