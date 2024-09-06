package com.carinaschoppe.playLegendBewerbung.events;

import com.carinaschoppe.playLegendBewerbung.database.DatabasePlayer;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinsServerEvent implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event) {
    var player = event.getPlayer();
    //generate dao player
    new Thread(() -> {
      var databasePlayer = DatabaseServices.DATABASE_PLAYERS.stream()
          .filter(p -> p.getUuid().equals(player.getUniqueId()))
          .findFirst()
          .orElse(new DatabasePlayer().setPermanent(true).setUuid(player.getUniqueId())
              .setRankExpiry(null).setName(player.getName()));

      databasePlayer.setName(player.getName());
      databasePlayer.save();
      DatabaseServices.DATABASE_PLAYERS.add(databasePlayer);
    }).start();
  }

}
