package com.carinaschoppe.playLegendBewerbung.commands;

import com.carinaschoppe.playLegendBewerbung.PlayLegendBewerbung;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.messages.Messages;
import com.carinaschoppe.playLegendBewerbung.utility.Utility;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankManagementCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {
    if (!command.getLabel().equalsIgnoreCase("rank")) {
      return false;
    }
    if (!(sender instanceof Player player)) {
      sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getNoPlayer()));
      return false;
    }

    if (!player.hasPermission("playlegendbewerbung.rank")) {

      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getNoPermissions()));
      return false;
    }

    //commands: rank delete, rank add, rank setlevel, rank setprefix, rank setname

    if (args.length < 2) {
      sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
      return false;
    }

    rankType(sender, args, player);

    return false;
  }

  private void rankType(@NotNull CommandSender sender, @NotNull String @NotNull [] args,
                        Player player) {
    if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
      rankDelete(player, args[1]);
    } else if (args[0].equalsIgnoreCase("add")) {
      rankAdd(player, args[1]);
    } else if (args[0].equalsIgnoreCase("setlevel")) {
      if (args.length != 3) {
        sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
        return;
      }
      rankSetLevel(player, args[1], args[2]);
    } else if (args[0].equalsIgnoreCase("setname")) {
      if (args.length != 3) {
        sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
        return;
      }
      rankSetName(player, args[1], args[2]);
    } else if (args[0].equalsIgnoreCase("setprefix")) {
      if (args.length != 3) {
        sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
        return;
      }
      rankSetPrefix(player, args[1], args[2]);
    }
  }

  private void rankSetPrefix(Player player, @NotNull String name, @NotNull String prefix) {
    //check if rank exists
    //check if rank exists
    var dbSearch = DatabaseServices.DATABASE_RANK.stream()
        .filter(it -> it.getRankName().equalsIgnoreCase(name)).findFirst();
    if (dbSearch.isEmpty()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRankNotExist().replace(
          "%rank%", name)));
      return;
    }
    var dbRank = dbSearch.get();
    dbRank.setPrefix(prefix);
    Bukkit.getScheduler().runTaskAsynchronously(PlayLegendBewerbung.getInstance(),
        () -> dbRank.save());
    player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getPrefixChanged().replace(
        "%prefix%", dbRank.getPrefix()).replace("%rank%", dbRank.getRankName())));
  }

  private void rankSetName(Player player, @NotNull String name, @NotNull String rankname) {
    //check if rank exists
    var dbSearch = DatabaseServices.DATABASE_RANK.stream()
        .filter(it -> it.getRankName().equalsIgnoreCase(name)).findFirst();
    if (dbSearch.isEmpty()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRankNotExist().replace(
          "%rank%", name)));
      return;
    }
    var dbRank = dbSearch.get();
    dbRank.setRankName(rankname);
    Bukkit.getScheduler().runTaskAsynchronously(PlayLegendBewerbung.getInstance(),
        () -> dbRank.save());
    player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRankNameChanged().replace(
        "%name%", dbRank.getRankName())));

  }

  private void rankSetLevel(Player player, @NotNull String name, @NotNull String level) {
    //check if rank exists
    var dbSearch = DatabaseServices.DATABASE_RANK.stream()
        .filter(it -> it.getRankName().equalsIgnoreCase(name)).findFirst();
    if (dbSearch.isEmpty()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRankNotExist().replace(
          "%rank%", name)));
      return;
    }
    var dbRank = dbSearch.get();

    int lvl = 1;
    try {
      lvl = Integer.parseInt(level);
    } catch (NumberFormatException e) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
      return;
    }

    if (lvl < 0) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
      return;
    }

    if (lvl > 99) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
      return;
    }

    if (dbRank.getRankName().equalsIgnoreCase("default")) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
      return;
    }

    dbRank.setLevel(lvl);
    Bukkit.getScheduler().runTaskAsynchronously(PlayLegendBewerbung.getInstance(),
        () -> dbRank.save());
    player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRankLevelChanged().replace(
        "%rank%", dbRank.getRankName()).replace("%level%", String.valueOf(dbRank.getLevel()))));
  }

  private void rankAdd(Player player, @NotNull String name) {
    //check if rank exists
    var dbSearch = DatabaseServices.DATABASE_RANK.stream()
        .filter(it -> it.getRankName().equalsIgnoreCase(name)).findFirst();
    if (dbSearch.isPresent()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRankAlreadyExists().replace(
          "%rank%", name)));
      return;
    }
    var newRank = new DatabaseRank().setRankName(name).setLevel(0).setPermissions(List.of())
        .setPrefix("PLACEHOLDER");
    Bukkit.getScheduler().runTaskAsynchronously(PlayLegendBewerbung.getInstance(),
        () -> newRank.save());
    DatabaseServices.DATABASE_RANK.add(newRank);

    player.sendMessage(Utility.convertComponent(
        Messages.INSTANCE.getAddedRank().replace("%rank%", newRank.getRankName())));
  }

  public void rankDelete(@NotNull Player player, @NotNull String rankname) {
    //check if rank exists
    var dbSearch = DatabaseServices.DATABASE_RANK.stream()
        .filter(it -> it.getRankName().equalsIgnoreCase(rankname)).findFirst();
    if (dbSearch.isEmpty()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRankNotExist()));
      return;
    }

    var dbRank = dbSearch.get();
    if (dbRank.getRankName().equalsIgnoreCase("default")) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getCannotDeleteDefaultRank()));
      return;
    }

    player.sendMessage(Utility.convertComponent(
        Messages.INSTANCE.getRemovedRank().replace("%rank%", dbRank.getRankName())));
    Bukkit.getScheduler()
        .runTaskAsynchronously(PlayLegendBewerbung.getInstance(), () -> dbRank.delete());
    DatabaseServices.DATABASE_RANK.remove(dbRank);

  }

}
