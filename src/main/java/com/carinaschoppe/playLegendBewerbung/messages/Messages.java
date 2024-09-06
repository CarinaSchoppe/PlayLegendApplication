package com.carinaschoppe.playLegendBewerbung.messages;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;


@Getter
@Setter
public class Messages {

  public static Messages INSTANCE;

  private String playerNotInDatabase = "Dieser Spieler existiert noch nicht in " +
      "der Datenbank.";
  private String playerNotOnline = "Der angegebene Spieler ist nicht online";
  private String playerNotFound =
      "Es konnte kein Spieler mit diesem Namen gefunden werden.";
  private String notEnoughArguments = "Es fehlen Argumente bei diesem Befehl.";
  private String lackOfPermission =
      "Du hast nicht genug Rechte um diesen Befehl auszuf hren.";
  private String addedRank = "Der Rang %rank% wurde erstellt.";
  private String removedRank = "Der Rang %rank% wurde gel scht.";
  private String addedPlayerToRank =
      "Dem Spieler %player% wurde der Rang %rank% verliehen.";
  private String removedPlayerFromRank =
      "Dem Spieler %player% wurde der Rang %rank% entzogen.";
  private String remainingTime =
      "Es verbleiben noch %days% Tage, %hours% Stunden, %min% Minuten und %sec% Sekunden.";
  private String addPermissionToRank =
      "Die Permission %permission% wurde dem Rang %rank% hinzugefügt.";
  private String newRankReceived =
      "Herzlichen Glückwunsch zum neuen Rang %rank%!";
  private String noPlayer =
      "Du musst ein Spieler für diesen Befehl sein.";

  public static Component convertComponent(String message) {
    return MiniMessage.miniMessage().deserialize(message);
  }


}
