package com.carinaschoppe.playLegendBewerbung.commands;

import com.carinaschoppe.playLegendBewerbung.database.DatabasePlayer;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.messages.Messages;
import com.carinaschoppe.playLegendBewerbung.utility.Utility;
import java.time.LocalDateTime;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerRankManagementCommand implements CommandExecutor {

  private static void sendMessages(Player player, DatabaseRank dbRank, DatabasePlayer dbPlayer) {
    var onlinePlayer = Bukkit.getPlayer(player.getUniqueId());

    if (onlinePlayer != null) {
      onlinePlayer.sendMessage(Utility.convertComponent(
          Messages.INSTANCE.getNewRankReceived().replace("%rank%", dbRank.getRankName())));
    }

    player.sendMessage(Utility.convertComponent(
        Messages.INSTANCE.getAddedPlayerToRank().replace("%rank%", dbRank.getRankName())
            .replace("%player%", dbPlayer.getName())));
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {
    if (!command.getLabel().equalsIgnoreCase("playergroup")) {
      return false;
    }
    if (!(sender instanceof Player player)) {
      sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getNoPlayer()));
      return false;
    }

    if (!player.hasPermission("playlegendbewerbung.playergroups")) {

      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getNoPermissions()));
      return false;
    }

    //commands: playergroup player rankname timestamp
    if (args.length < 2) {
      sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
      return false;
    }

    //rank added permanently
    if (args.length == 2) {
      playerGroupSetPermanently(player, args[0], args[1]);
    } else {
      //using timestamps
      playerGroupSetUsingTimestamp(player, args[0], args[1], args[2]);
    }

    return false;
  }

  private void playerGroupSetUsingTimestamp(Player player, @NotNull String playerName,
                                            @NotNull String rankName, @NotNull String timestamp) {
    //format of timestamp XdXhXmXs where X is the number

    if (!timestamp.matches("[0-9]+d[0-9]+h[0-9]+m[0-9]+s")) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getTimestampNotCorrect()));
      return;
    }

    //format the timestamp into localdatetime
    var dateTimeNow = LocalDateTime.now();
    var dateTime =
        dateTimeNow.plusDays(Long.parseLong(timestamp.substring(0, timestamp.indexOf("d"))))
            .plusHours(Long.parseLong(
                timestamp.substring(timestamp.indexOf("d") + 1, timestamp.indexOf("h"))))
            .plusMinutes(Long.parseLong(
                timestamp.substring(timestamp.indexOf("h") + 1, timestamp.indexOf("m"))))
            .plusSeconds(Long.parseLong(
                timestamp.substring(timestamp.indexOf("m") + 1, timestamp.indexOf("s"))));

    var dbPlayerSearch = DatabaseServices.DATABASE_PLAYERS.stream()
        .filter(dbP -> dbP.getName().equalsIgnoreCase(playerName)).findFirst();
    if (dbPlayerSearch.isEmpty()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getPlayerNotFound()));
      return;
    }

    //check for rank
    var dbRankSearch = DatabaseServices.DATABASE_RANK.stream()
        .filter(dbR -> dbR.getRankName().equalsIgnoreCase(rankName)).findFirst();
    if (dbRankSearch.isEmpty()) {
      player.sendMessage(Utility.convertComponent(
          Messages.INSTANCE.getRankNotExist().replace("%rank%", rankName)));
      return;
    }

    var dbPlayer = dbPlayerSearch.get();
    var dbRank = dbRankSearch.get();

    if (dbRank.getRankName().equalsIgnoreCase("default")) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
      return;
    }

    dbPlayer.setDatabaseRank(dbRank);
    dbPlayer.setRankExpiry(dateTime);
    dbPlayer.setPermanent(false);
    dbPlayer.save();

    sendMessages(player, dbRank, dbPlayer);


  }

  private void playerGroupSetPermanently(Player player, @NotNull String playerName,
                                         @NotNull String rankName) {
    //check if player exists

    var dbPlayerSearch = DatabaseServices.DATABASE_PLAYERS.stream()
        .filter(dbP -> dbP.getName().equalsIgnoreCase(playerName)).findFirst();
    if (dbPlayerSearch.isEmpty()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getPlayerNotFound()));
      return;
    }

    //check for rank
    var dbRankSearch = DatabaseServices.DATABASE_RANK.stream()
        .filter(dbR -> dbR.getRankName().equalsIgnoreCase(rankName)).findFirst();
    if (dbRankSearch.isEmpty()) {
      player.sendMessage(Utility.convertComponent(
          Messages.INSTANCE.getRankNotExist().replace("%rank%", rankName)));
      return;
    }

    var dbPlayer = dbPlayerSearch.get();
    var dbRank = dbRankSearch.get();


    dbPlayer.setDatabaseRank(dbRank);
    dbPlayer.setPermanent(true);
    dbPlayer.save();

    sendMessages(player, dbRank, dbPlayer);


  }
}
