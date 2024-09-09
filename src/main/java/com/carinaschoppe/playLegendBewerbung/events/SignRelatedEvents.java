package com.carinaschoppe.playLegendBewerbung.events;

import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import de.blablubbabc.insigns.SignSendEvent;
import java.util.Objects;
import org.bukkit.block.sign.Side;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SignRelatedEvents implements Listener {

  /**
   * This event is called when a player right clicks a sign.
   * It's used to display the current players name, rank and prefix on the sign.
   * Source of this code -> https://github.com/blablubbabc/IndividualSigns
   */
  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
  public void onSignChange(SignSendEvent event) {
    String line = event.getLine(Side.FRONT, 0);
    if (line.contains("[name]")) {
      event.setLine(Side.FRONT, 0, line.replace("[name]", event.getPlayer().getName()));
      event.setLine(Side.FRONT, 1, Objects.requireNonNull(
          DatabaseRank.getPlayerRank(event.getPlayer())).getRankName());
      event.setLine(Side.FRONT, 2, Objects.requireNonNull(
          DatabaseRank.getPlayerRank(event.getPlayer())).getPrefix());
    }
  }


}

