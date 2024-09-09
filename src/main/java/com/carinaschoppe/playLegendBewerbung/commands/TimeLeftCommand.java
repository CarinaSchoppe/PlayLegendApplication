package com.carinaschoppe.playLegendBewerbung.commands;

import com.carinaschoppe.playLegendBewerbung.database.DatabasePlayer;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.messages.Messages;
import com.carinaschoppe.playLegendBewerbung.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TimeLeftCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {
    if (!command.getLabel().equalsIgnoreCase("time")) {
      return false;
    }

    if (!(sender instanceof Player player)) {
      sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getNoPlayer()));
      return false;
    }

    if (!player.hasPermission("playlegendbewerbung.time")) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getNoPermissions()));
      return false;
    }

    if (args.length == 0) {
      //get own time left
      //time left
      var dbPlayerSearch =
          DatabaseServices.DATABASE_PLAYERS.stream()
              .filter(it -> it.getUuid().equals(player.getUniqueId())).findFirst();
      if (dbPlayerSearch.isEmpty()) {
        return false;
      }
      var dbPlayer = dbPlayerSearch.get();
      //Rank not permanent
      ownPlayerTimeLeft(dbPlayer, player);
    } else if (args.length == 1) {
      var other =
          DatabaseServices.DATABASE_PLAYERS.stream()
              .filter(it -> it.getName().equalsIgnoreCase(args[0])).findFirst();
      if (other.isEmpty()) {
        player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getPlayerNotFound()));
        return false;
      }
      var dbPlayerSearch = other.get();
      otherPlayerMesssage(dbPlayerSearch, player);
    } else {
      sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
    }
    return false;
  }

  private void otherPlayerMesssage(DatabasePlayer dbPlayerSearch, Player player) {
    if (!dbPlayerSearch.isPermanent()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRemainingTimeOther().replace(
              "%time%", Utility.calculateTimeLeft(dbPlayerSearch))
          .replace("%rank%", DatabaseRank.getPlayerRank(player).getRankName()).replace("%player%"
              , dbPlayerSearch.getName())));
    } else {
      //rank is permenent
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getPermanentTimeOther()
          .replace("%rank%", DatabaseRank.getPlayerRank(player).getRankName())
          .replace("%name%", dbPlayerSearch.getName())));
    }
  }

  private void ownPlayerTimeLeft(DatabasePlayer dbPlayer, Player player) {
    if (!dbPlayer.isPermanent()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRemainingTime().replace(
              "%time%", Utility.calculateTimeLeft(dbPlayer))
          .replace("%rank%", DatabaseRank.getPlayerRank(player).getRankName())));

    } else {
      //rank is permenent
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getPermanentTime()
          .replace("%rank%", DatabaseRank.getPlayerRank(player).getRankName())));
    }
  }
}
