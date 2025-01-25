package me.huanmeng.command.tp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static me.huanmeng.MengServerX.getMessage;
import static me.huanmeng.util.Abbreviations.M;

public class Teleport implements CommandExecutor {

    public static HashMap<UUID, UUID> tpaRequests = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("player-command"));
            return true;
        }

        Player senderPlayer = (Player) sender;

        if (args.length != 1) {
            senderPlayer.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("usage") + "/msx_tp <player>");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            senderPlayer.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("not-found"));
            return true;
        }

        if (targetPlayer.getUniqueId().equals(senderPlayer.getUniqueId())) {
            senderPlayer.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("not-apply"));
            return true;
        }

        tpaRequests.put(targetPlayer.getUniqueId(), senderPlayer.getUniqueId());
        senderPlayer.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("send-apply") + targetPlayer.getName());
        targetPlayer.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + senderPlayer.getName() + getMessage("have-apply"));

        return true;
    }
}
