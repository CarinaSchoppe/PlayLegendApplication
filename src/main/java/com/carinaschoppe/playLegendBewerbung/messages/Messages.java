package com.carinaschoppe.playLegendBewerbung.messages;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Messages {
  public static Messages INSTANCE;
  private String rankAlreadyExists = "Der Rang %rank% existiert bereits.";
  private String permissionAlreadyExists = "Der Rang %rank% hat bereits die Permission " +
      "%permission%.";
  private String permissionRemoved = "Dem Rang %rank% wurde die Permission %permission% entfernt.";
  private String permissionAdded = "Dem Rang %rank% wurde die Permission %permission% hinzugefügt.";
  private String cannotDeleteDefaultRank = "Der Rang default kann nicht gelöscht werden.";
  private String noPermissions = "Du hast nicht genug Rechte um diesen Befehl auszufuehren.";
  private String playerNotInDatabase = "Dieser Spieler existiert noch nicht in " +
      "der Datenbank.";
  private String playerNotOnline = "Der angegebene Spieler ist nicht online";
  private String playerNotFound =
      "Es konnte kein Spieler mit diesem Namen gefunden werden.";
  private String argumentsNotCorrect = "Es fehlen Argumente oder die Argumente sind ungültig bei " +
      "diesem" +
      " Befehl.";
  private String lackOfPermission =
      "Du hast nicht genug Rechte um diesen Befehl auszufeuhren.";
  private String rankNotExist =
      "Der Rank %rank% existiert nicht!";
  private String addedRank = "Der Rang %rank% wurde erstellt.";
  private String removedRank = "Der Rang %rank% wurde geloescht.";
  private String addedPlayerToRank =
      "Dem Spieler %player% wurde der Rang %rank% verliehen.";

  private String permanentTime =
      "Dein Rank %rank% ist permanent";
  private String permanentTimeOther =
      "Der Rank %rank% von %player% ist permanent";
  private String remainingTime =
      "Es verbleiben noch %time% für " +
          "deinen Rank %rank% Sekunden.";

  private String timestampNotCorrect =
      "Das Zeitstempel-Format muss XdXhXmXs sein.";
  private String remainingTimeOther =
      "Es verbleiben noch %time% im Rank " +
          "%rank%" +
          " für den" +
          " Spieler: %player%";
  private String prefixChanged =
      "Der Prefix vom Rang: %rank% ist nun: %prefix%";
  private String rankLevelChanged =
      "Das Level vom Rang: %rank% ist nun: %level%";
  private String rankNameChanged =
      "Der Name ist nun: %name%";

  private String addPermissionToRank =
      "Die Permission %permission% wurde dem Rang %rank% hinzugefeugt.";
  private String newRankReceived =
      "Herzlichen Glückwunsch zum neuen Rang %rank%!";
  private String noPlayer =
      "Du musst ein Spieler für diesen Befehl sein.";

}
