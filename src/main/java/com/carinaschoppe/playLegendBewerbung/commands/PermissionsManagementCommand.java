package com.carinaschoppe.playLegendBewerbung.commands;

import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.messages.Messages;
import com.carinaschoppe.playLegendBewerbung.utility.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PermissionsManagementCommand implements CommandExecutor {


  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {

    if (!command.getLabel().equalsIgnoreCase("permission")) {
      return false;
    }
    if (!(sender instanceof Player player)) {
      sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getNoPlayer()));
      return false;
    }

    if (!player.hasPermission("playlegendbewerbung.permissions")) {

      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getNoPermissions()));
      return false;
    }

    //commands: permissions add rankname what, permissions remove rankname what

    if (args.length != 3) {
      sender.sendMessage(Utility.convertComponent(Messages.INSTANCE.getArgumentsNotCorrect()));
      return false;
    }

    if (args[0].equalsIgnoreCase("add")) {
      addPermission(player, args[1], args[2]);
    } else if (args[0].equalsIgnoreCase("remove")) {
      removePermission(player, args[1], args[2]);
    }
    return false;
  }

  private void removePermission(Player player, @NotNull String rankname,
                                @NotNull String permissionToRemove) {
    //check if rank exists
    var dbSearch = DatabaseServices.DATABASE_RANK.stream()
        .filter(it -> it.getRankName().equalsIgnoreCase(rankname)).findFirst();
    if (dbSearch.isEmpty()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRankNotExist()));
      return;
    }

    var dbRank = dbSearch.get();
    dbRank.getPermissions().remove(permissionToRemove);
    dbRank.save();
    player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getPermissionRemoved().replace(
        "%permission%", permissionToRemove).replace("%rank%", dbRank.getRankName())));
  }

  private void addPermission(Player player, @NotNull String rankname,
                             @NotNull String permissionToAdd) {
    //check if rank exists
    var dbSearch = DatabaseServices.DATABASE_RANK.stream()
        .filter(it -> it.getRankName().equalsIgnoreCase(rankname)).findFirst();
    if (dbSearch.isEmpty()) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getRankNotExist()));
      return;
    }

    var dbRank = dbSearch.get();

    if (dbRank.getPermissions().stream().anyMatch(it -> it.equalsIgnoreCase(permissionToAdd))) {
      player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getPermissionAlreadyExists()));
    }
    dbRank.getPermissions().add(permissionToAdd);
    dbRank.save();
    player.sendMessage(Utility.convertComponent(Messages.INSTANCE.getPermissionAdded().replace(
        "%permission%", permissionToAdd).replace("%rank%", dbRank.getRankName())));
  }
}
