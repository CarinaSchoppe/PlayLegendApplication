package com.carinaschoppe.playLegendBewerbung.events;

import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import com.carinaschoppe.playLegendBewerbung.messages.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinsServerEvent implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onPlayerJoin(PlayerJoinEvent event) {
    var player = event.getPlayer();
    var databaseRank = DatabaseRank.getPlayerRank(player);
    var name = databaseRank.getRankName();
    event.joinMessage(
        Messages.convertComponent("[" + name + "]" + " " + player.getName() + " hat das Spiel " +
            "betreten!"));
    //generate dao player

  }

}
