package com.carinaschoppe.playLegendBewerbung.events;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChunkLoadEvent implements Listener {


  @EventHandler(ignoreCancelled = true)
  public void onPlayerChunkLoad(io.papermc.paper.event.packet.PlayerChunkLoadEvent event) {

    var chunk = event.getChunk();
    var tileEntities = chunk.getTileEntities();
    var signs =
        Arrays.stream(tileEntities).filter(item -> item instanceof Sign).map(te -> (Sign) te)
            .toList();

    if (signs.isEmpty()) {
      return;
    }

    for (var sign : signs) {
      if (sign.getSide(Side.FRONT).line(0).equals(Component.text("[name]"))) {
        SignRelatedEvents.SIGNS.add(sign);
        SignRelatedEvents.updateSign();
      } else {
        SignRelatedEvents.SIGNS.remove(sign);
      }
    }
  }


}

