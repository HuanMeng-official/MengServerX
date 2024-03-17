package me.huanmeng.command;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import static me.huanmeng.util.Abbreviations.M;

public class SetGameMode implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (player.isOp()) {
            String mode = args[0];
            switch (mode) {
                case "1":
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "已将您的游戏模式更换为创造");
                    break;
                case "2":
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "已将您的游戏模式更换为冒险");
                    break;
                case "3":
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "已将您的游戏模式更换为旁观");
                    break;
                case "0":
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "已将您的游戏模式更换为生存");
                    break;
            }
        }else {
            player.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "该命令只能由管理员执行");
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("0");
            list.add("1");
            list.add("2");
            list.add("3");
            return list;
        }
        return null;
    }
}