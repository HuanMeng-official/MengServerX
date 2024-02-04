package me.huanmeng.command;

import me.huanmeng.MengServerX;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;


import java.util.List;

import static me.huanmeng.util.Abbreviations.M;

public class WhiteListAdd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(!sender.isOp()){
            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RED + "该命令只能由管理员执行");
            return false;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RED + "Used: /msx_add <player>");
            return false;
        }
        Plugin config = MengServerX.getPlugin(MengServerX.class);

        List<String> whitelist = config.getConfig().getStringList("List");
        if(whitelist.contains(args[0])){
            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RED + "该玩家已拥有白名单，切勿重复添加");
            return false;
        }
        whitelist.add(args[0]);
        config.getConfig().set("List", whitelist);
        config.saveConfig();
        sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "已将该玩家添加入白名单");
        return false;
    }
}
