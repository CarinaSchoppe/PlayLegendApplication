package com.carinaschoppe.playLegendBewerbung.ranklogic;

import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.jetbrains.annotations.NotNull;

public class RankHandler extends PermissibleBase {

  private final Player player;

  public RankHandler(Player player) {
    super(player);
    this.player = player;
  }

  @Override
  public boolean hasPermission(@NotNull String inName) {
    var db =
        DatabaseServices.DATABASE_PLAYERS.stream()
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
