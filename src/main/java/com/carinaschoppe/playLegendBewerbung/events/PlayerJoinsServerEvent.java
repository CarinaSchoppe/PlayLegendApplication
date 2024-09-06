package com.carinaschoppe.playLegendBewerbung.events;

import com.carinaschoppe.playLegendBewerbung.database.DatabasePlayer;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinsServerEvent implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event) {
    var player = event.getPlayer();


    var databaseRank = DatabaseRank.getPlayerRank(player);
    var name = databaseRank.getRankName();

    Bukkit.broadcast(
        Messages.convertComponent(name + " " + player.getName() + " hat das Spiel betreten!"));
    //generate dao player
    new Thread(() -> {
      var databasePlayer = DatabaseServices.DATABASE_PLAYERS.stream()
          .filter(p -> p.getUuid().equals(player.getUniqueId()))
          .findFirst()
          .orElse(new DatabasePlayer().setPermanent(true).setUuid(player.getUniqueId())
              .setRankExpiry(null).setName(player.getName()).setDatabaseRank(
                  DatabaseServices.DATABASE_RANK.stream().filter(rank -> rank.getLevel() == 0)
                      .findFirst().get()));

      databasePlayer.setName(player.getName());
      databasePlayer.save();
      DatabaseServices.DATABASE_PLAYERS.add(databasePlayer);
    }).start();
  }

}
