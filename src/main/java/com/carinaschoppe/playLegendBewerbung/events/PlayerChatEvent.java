package com.carinaschoppe.playLegendBewerbung.events;

import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import com.carinaschoppe.playLegendBewerbung.utility.Utility;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatEvent implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onAsyncChat(AsyncChatEvent event) {

    event.setCancelled(true);

    var player = event.getPlayer();
    var rank = DatabaseRank.getPlayerRank(player);
    if (rank == null) {
      return;
    }
    var name = rank.getRankName();
    Bukkit.broadcast(
        Utility.convertComponent("[" + name + "]" + " " + player.getName() + ": ")
            .append(event.message()));


  }

}
