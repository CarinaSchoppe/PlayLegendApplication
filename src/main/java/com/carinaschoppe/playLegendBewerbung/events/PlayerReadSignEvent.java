package com.carinaschoppe.playLegendBewerbung.events;

import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerReadSignEvent {


  private final JavaPlugin plugin;

  public PlayerReadSignEvent(JavaPlugin plugin) {
    this.plugin = plugin;
    registerListener();
  }

  private void registerListener() {
    ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL,
        PacketType.Play.Server.TILE_ENTITY_DATA) {
      @Override
      public void onPacketSending(PacketEvent event) {

        var player = event.getPlayer();
        var rank = DatabaseRank.getPlayerRank(player);

        // The TILE_ENTITY_DATA packet contains NBT data
        NbtCompound nbtData = (NbtCompound) event.getPacket().getNbtModifier().read(0);

        // Check if this is a sign (tile entity ID is sign for this block)
        if (nbtData.containsKey("id") && nbtData.getString("id").equals("minecraft:sign")) {
          // Get the lines of the sign from the NBT data
          String[] lines = new String[4];
          for (int i = 0; i < 4; i++) {
            lines[i] = nbtData.getString("Text" + (i + 1));
          }

          // If the first line contains [name], modify it
          if (lines[0].contains("[name]")) {
            // Replace the first line with "XXX"
            nbtData.put("Text1", WrappedChatComponent.fromText(rank.getPrefix()).getJson());
            nbtData.put("Text1", WrappedChatComponent.fromText(rank.getRankName()).getJson());

            // Set the second line to the player's name
            nbtData.put("Text2", WrappedChatComponent.fromText(player.getName()).getJson());

            // Write the updated NBT data back to the packet
            event.getPacket().getNbtModifier().write(0, nbtData);
          }
        }
      }
    });


  }

  public void unregisterListener() {
    ProtocolLibrary.getProtocolManager().removePacketListeners(plugin);
  }


}
