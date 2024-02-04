package me.huanmeng.command;

import me.huanmeng.MengServerX;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static me.huanmeng.util.Abbreviations.M;

public class WhiteListRemove implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.isOp()){
            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RED + "该命令只能由管理员执行");
            return false;
        }
        if(args.length != 1){
            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RED + "Used: /msx_remove <player>");
            return false;
        }
        Plugin config = MengServerX.getPlugin(MengServerX.class);
        List<String> whitelist = config.getConfig().getStringList("List");
        if(whitelist.contains(args[0])){
            Player remove = Bukkit.getPlayer(args[0]);
            whitelist.remove(args[0]);
            config.getConfig().set("List", whitelist);
            config.saveConfig();
            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "已取消玩家白名单");
            if(remove == null){
                return false;
            }
            if(remove.isOnline()){
                sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + "已取消玩家白名单");
                remove.kickPlayer(ChatColor.translateAlternateColorCodes('&',
                                Objects.requireNonNull(config.getConfig().getString("Repeal"))
                        )
                );
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RED + "该玩家不在白名单中，请检查玩家名是否输入正确");
        }
        return false;
    }
}
