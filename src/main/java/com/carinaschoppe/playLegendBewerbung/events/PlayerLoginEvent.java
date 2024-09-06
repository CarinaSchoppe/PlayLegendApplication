package com.carinaschoppe.playLegendBewerbung.events;

import com.carinaschoppe.playLegendBewerbung.ranklogic.RankHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLoginEvent implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onPlayerLogin(org.bukkit.event.player.PlayerLoginEvent event) {

    RankHandler.updatePlayerPermissions(event.getPlayer());


  }

}
