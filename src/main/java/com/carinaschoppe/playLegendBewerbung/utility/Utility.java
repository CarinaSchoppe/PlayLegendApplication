package com.carinaschoppe.playLegendBewerbung.utility;

import com.carinaschoppe.playLegendBewerbung.configuration.Configuration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class Utility {

  public static @NotNull Component convertComponent(String message) {
    return MiniMessage.miniMessage().deserialize(Configuration.INSTANCE.getPrefix() + message);
  }
}
