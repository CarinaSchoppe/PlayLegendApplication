package com.carinaschoppe.playLegendBewerbung.messages;

import lombok.Getter;
import lombok.Setter;


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
      "Du hast nicht genug Rechte um diesen Befehl auszufeuhren.";
  private String addedRank = "Der Rang %rank% wurde erstellt.";
  private String removedRank = "Der Rang %rank% wurde geloescht.";
  private String addedPlayerToRank =
      "Dem Spieler %player% wurde der Rang %rank% verliehen.";
  private String removedPlayerFromRank =
      "Dem Spieler %player% wurde der Rang %rank% entzogen.";
  private String permanentTime =
      "Dein Rank %rank% ist permanent";
  private String permanentTimeOther =
      "Der Rank %rank% von %player% ist permanent";
  private String remainingTime =
      "Es verbleiben noch %days% Tage, %hours% Stunden, %min% Minuten und %sec% für " +
          "deinen Rank %rank% Sekunden.";
  private String remainingTimeOther =
      "Es verbleiben noch %days% Tage, %hours% Stunden, %min% Minuten und %sec% Sekunden im Rank " +
          "%rank%" +
          " für den" +
          " Spieler: %player%";
  private String prefixChanged =
      "Der Prefix vom Rang: %rank% ist nun: %prefix%";
  private String levelChanged =
      "Das Level vom Rang: %rank% ist nun: %level%";
  private String addPermissionToRank =
      "Die Permission %permission% wurde dem Rang %rank% hinzugefeugt.";
  private String newRankReceived =
      "Herzlichen Glückwunsch zum neuen Rang %rank%!";
  private String noPlayer =
      "Du musst ein Spieler für diesen Befehl sein.";


}
