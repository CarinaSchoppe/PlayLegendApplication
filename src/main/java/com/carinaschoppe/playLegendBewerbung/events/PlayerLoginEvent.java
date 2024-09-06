package com.carinaschoppe.playLegendBewerbung.events;

import com.carinaschoppe.playLegendBewerbung.database.DatabasePlayer;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.ranklogic.RankHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLoginEvent implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onPlayerLogin(org.bukkit.event.player.PlayerLoginEvent event) {
    new Thread(() -> {
      var databasePlayer = DatabaseServices.DATABASE_PLAYERS.stream()
          .filter(p -> p.getUuid().equals(event.getPlayer().getUniqueId()))
          .findFirst()
          .orElse(new DatabasePlayer().setPermanent(true).setUuid(event.getPlayer().getUniqueId())
              .setRankExpiry(null).setName(event.getPlayer().getName()).setDatabaseRank(
                  DatabaseServices.DATABASE_RANK.stream().filter(rank -> rank.getLevel() == 0)
                      .findFirst().get()));

      databasePlayer.setName(event.getPlayer().getName());
      databasePlayer.save();
      DatabaseServices.DATABASE_PLAYERS.add(databasePlayer);
    }).start();
    RankHandler.updatePlayerPermissions(event.getPlayer());


  }

}
