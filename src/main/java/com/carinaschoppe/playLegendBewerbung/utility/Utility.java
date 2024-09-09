package com.carinaschoppe.playLegendBewerbung.utility;

import com.carinaschoppe.playLegendBewerbung.configuration.Configuration;
import com.carinaschoppe.playLegendBewerbung.database.DatabasePlayer;
import java.time.Duration;
import java.time.LocalDateTime;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public class Utility {

  /**
   * Converts a string message into an {@link Component} that can be sent to players.
   * <p>
   * This method prepends the prefix defined in the configuration to the given message
   * and then deserializes the resulting string into an {@link Component}.
   *
   * @param message The message to convert
   * @return The converted component
   */
  public static @NotNull Component convertComponent(String message) {
    return MiniMessage.miniMessage().deserialize(Configuration.INSTANCE.getPrefix() + message);
  }

  /**
   * Calculates the time left until the rank of the given player expires.
   *
   * @param dbPlayer the player to calculate the time left for
   * @return a string in the format "Xd Xh Xm Xs" representing the time left
   */
  public static String calculateTimeLeft(DatabasePlayer dbPlayer) {
    LocalDateTime expiryTime = dbPlayer.getRankExpiry();
    LocalDateTime now = LocalDateTime.now();

    // Berechne die Dauer zwischen jetzt und dem Ablaufzeitpunkt
    Duration duration = Duration.between(now, expiryTime);

    // Extrahiere die verbleibenden Tage, Stunden, Minuten und Sekunden
    long days = duration.toDays();
    duration = duration.minusDays(days);

    long hours = duration.toHours();
    duration = duration.minusHours(hours);

    long minutes = duration.toMinutes();
    duration = duration.minusMinutes(minutes);

    long seconds = duration.getSeconds();

    // Erstelle einen String mit der verbleibenden Zeit
    String timeLeft = days + "d " + hours + "h " + minutes + "m " + seconds + "s";

    return timeLeft;
  }
}
