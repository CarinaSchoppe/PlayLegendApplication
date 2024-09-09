package com.carinaschoppe.playLegendBewerbung.ranklogic;

import com.carinaschoppe.playLegendBewerbung.PlayLegendBewerbung;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.messages.Messages;
import com.carinaschoppe.playLegendBewerbung.utility.Utility;
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

  /**
   * Updates the permissions of a given player by setting the Permissible object
   * of the CraftHumanEntity to a new instance of RankHandler.
   *
   * @param player The player to update the permissions for.
   */
  public static void updatePlayerPermissions(Player player) {
    try {
      player.hasPermission("");
      Field field = CraftHumanEntity.class.getDeclaredField("perm");
      field.setAccessible(true);
      field.set(player, new RankHandler(player));
      field.setAccessible(false);
      player.updateCommands();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * A task that runs every 20 ticks and checks if a player's rank has expired. If it has, the
   * player is set to the default rank and the permanent flag is set to true. If the player is online,
   * a message is sent to the player.
   */
  public static void playerRankRemover() {

    Bukkit.getScheduler().runTaskTimer(PlayLegendBewerbung.getInstance(), () -> {
      //go through all players in the database and check if they have expired then set them to
      // default group
      for (var dbPlayer : DatabaseServices.DATABASE_PLAYERS) {
        if (dbPlayer.getRankExpiry() != null && !dbPlayer.isPermanent() &&
            dbPlayer.getRankExpiry().isBefore(LocalDateTime.now())) {
          dbPlayer.setRankExpiry(null).setDatabaseRank(
              DatabaseServices.DATABASE_RANK.stream().filter(rank -> rank.getLevel() == 0)
                  .findFirst().get());
          dbPlayer.setPermanent(true);
          Bukkit.getScheduler().runTaskAsynchronously(PlayLegendBewerbung.getInstance(),
              () -> dbPlayer.save());

          var player = Bukkit.getPlayer(dbPlayer.getUuid());
          if (player != null && player.isOnline()) {
            player.sendMessage(Utility.convertComponent(
                Messages.INSTANCE.getNewRankReceived()
                    .replace("%rank%", dbPlayer.getDatabaseRank().getRankName())));
          }
        }
      }
      /**
       * Checks if the player represented by this RankHandler has the given
       * permission. This method takes into account the player's rank and
       * whether the player is an operator or not.
       *
       * @param inName The name of the permission to check.
       * @return True if the player has the given permission, false otherwise.
       */
    }, 20, 20);

  }


  @Override
  public boolean hasPermission(@NotNull String inName) {
    var db = DatabaseServices.DATABASE_PLAYERS.stream()
        .filter(dbPlayer -> dbPlayer.getUuid().equals(player.getUniqueId())).findFirst().get();

    var permissions = db.getDatabaseRank().getAllPermissionsOfRankAndChildren();

    if (permissions.contains("-" + inName)) {
      return false;
    }
    if (permissions.contains("*") || player.isOp()) {
      return true;
    }

    return permissions.contains(inName);

  }
}
