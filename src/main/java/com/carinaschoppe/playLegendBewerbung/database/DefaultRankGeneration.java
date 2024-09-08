package com.carinaschoppe.playLegendBewerbung.database;

import java.util.List;

public class DefaultRankGeneration {

  public static void loadDefaultRanks() {
    if (!DatabaseServices.DATABASE_RANK.isEmpty()) {
      return;
    }

    //generate default ranks
    var defaultRank =
        new DatabaseRank().setRankName("default").setLevel(0).setPermissions(
            List.of(
                "minecraft.help", "bukkit.broadcast", "bukkit.broadcast.user", "minecraft.command" +
                    ".gamemode")
        ).setPrefix("Nothing");
    defaultRank.save();
    DatabaseServices.DATABASE_RANK.add(defaultRank);


  }

}
