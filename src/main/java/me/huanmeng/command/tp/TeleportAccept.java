package me.huanmeng.command.tp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static me.huanmeng.MengServerX.getMessage;
import static me.huanmeng.util.Abbreviations.M;

public class TeleportAccept implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("player-command"));
            return true;
        }

        Player targetPlayer = (Player) sender;
        UUID requesterUUID = Teleport.tpaRequests.get(targetPlayer.getUniqueId());

        if (requesterUUID == null) {
            targetPlayer.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("no-apply"));
            return true;
        }

        Player requester = Bukkit.getPlayer(requesterUUID);

        if (requester == null || !requester.isOnline()) {
            targetPlayer.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("offline"));
            Teleport.tpaRequests.remove(targetPlayer.getUniqueId());
            return true;
        }

        requester.teleport(targetPlayer.getLocation());
        targetPlayer.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("accept_1") + requester.getName() + getMessage("accept_2"));
        requester.sendMessage(ChatColor.YELLOW + M + ChatColor.RESET + getMessage("have-accept"));
        Teleport.tpaRequests.remove(targetPlayer.getUniqueId());

        return true;
    }
}
