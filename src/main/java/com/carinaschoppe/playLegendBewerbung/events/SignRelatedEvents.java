package com.carinaschoppe.playLegendBewerbung.events;

import com.carinaschoppe.playLegendBewerbung.PlayLegendBewerbung;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.scheduler.BukkitTask;

public class SignRelatedEvents implements Listener {

  public static final Set<Sign> SIGNS = new HashSet<>();
  private static BukkitTask task;

  public static void updateSign() {
    if (task != null) {
      return;
    }
    task = Bukkit.getScheduler().runTaskTimer(PlayLegendBewerbung.getInstance(), () -> {
      for (var sign : SIGNS) {
        for (Player player : Bukkit.getOnlinePlayers()) {
          var side = sign.getTargetSide(player);
          side.line(0, player.displayName());
          side.line(1, Component.text(DatabaseRank.getPlayerRank(player).getRankName()));
          side.line(2, Component.text(DatabaseRank.getPlayerRank(player).getPrefix()));
          sign.update(true);
          player.sendBlockUpdate(sign.getLocation(), sign);
        }
      }
    }, 1, 1);

  }

  @EventHandler(ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
    //check if the block was a sign in the signs list and remove it
    var block = event.getBlock();
    if (block.getState(false) instanceof Sign sign) {
      if (SIGNS.contains(sign)) {
        SIGNS.remove(sign);
        SignRelatedEvents.updateSign();
      }
    }
  }

  // Methode zum Speichern von Daten im PersistentDataContainer eines Schilds

  @EventHandler(ignoreCancelled = true)
  public void onSignChange(SignChangeEvent event) {
    var sign = (Sign) event.getBlock().getState(false);
    if (sign.getSide(Side.FRONT).line(0).equals(Component.text("[name]"))) {
      SIGNS.add(sign);
      updateSign();

    }

  }

  @EventHandler(ignoreCancelled = true)
  public void onBlockPlace(BlockPlaceEvent event) {
    //check if the block is a sign and if the first line is [name] if so add it to the signs list
    if (event.getBlock().getState(true) instanceof org.bukkit.block.Sign sign) {
      if (sign.getSide(Side.FRONT).line(0).equals(Component.text("[name]"))) {
        SIGNS.add(sign);
        updateSign();
      }
    }
  }

}
