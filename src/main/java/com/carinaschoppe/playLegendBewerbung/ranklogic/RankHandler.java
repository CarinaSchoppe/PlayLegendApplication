package com.carinaschoppe.playLegendBewerbung.ranklogic;

import com.carinaschoppe.playLegendBewerbung.PlayLegendBewerbung;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.messages.Messages;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.jetbrains.annotations.NotNull;

public class RankHandler extends PermissibleBase {

  private final Player player;

  public RankHandler(Player player) {
    super(player);
    this.player = player;
  }

  public static void updatePlayerPermissions(Player player) {
    try {
      Field field = CraftHumanEntity.class.getDeclaredField("perm");
      field.setAccessible(true);
      field.set(player, new RankHandler(player));
      field.setAccessible(false);
      player.updateCommands();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static void playerRankRemover() {

    Bukkit.getScheduler().runTaskTimerAsynchronously(PlayLegendBewerbung.getInstance(), () -> {
      //go through all players in the database and check if they have expired then set them to
      // default group
      for (var dbPlayer : DatabaseServices.DATABASE_PLAYERS) {
        if (dbPlayer.getRankExpiry() != null &&
            dbPlayer.getRankExpiry().isBefore(LocalDateTime.now())) {
          dbPlayer.setRankExpiry(null).setDatabaseRank(
              DatabaseServices.DATABASE_RANK.stream().filter(rank -> rank.getLevel() == 0)
                  .findFirst().get()).save();

          var player = Bukkit.getPlayer(dbPlayer.getUuid());
          if (player != null && player.isOnline()) {
            player.sendMessage(Messages.convertComponent(
                Messages.INSTANCE.getNewRankReceived()
                    .replace("%rank%", dbPlayer.getDatabaseRank().getRankName())));
          }
        }
      }
    }, 20, 20);

  }


  @Override
  public boolean hasPermission(@NotNull String inName) {
    var db = DatabaseServices.DATABASE_PLAYERS.stream()
        .filter(dbPlayer -> dbPlayer.getUuid().equals(player.getUniqueId())).findFirst().get();

    var permissions = db.getDatabaseRank().getPermissions();

    if (permissions.contains("-" + inName)) {
      return false;
    }

    if (permissions.contains("*") || player.isOp()) {
      return true;
    }

    return permissions.contains(inName);

  }
}
