package com.carinaschoppe.playLegendBewerbung.events;

import de.blablubbabc.insigns.SignSendEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SignRelatedEvents implements Listener {


  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onSignChange(SignSendEvent event) {
    event.getPlayer().sendMessage("hello");
    for (int i = 0; i < 4; i++) {
      String line = event.getLine(i);
      if (line.contains("[name]")) {
        event.setLine(i, line.replace("[name]", event.getPlayer().getName()));
      }
    }
  }




}

