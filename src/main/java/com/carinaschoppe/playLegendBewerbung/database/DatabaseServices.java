package com.carinaschoppe.playLegendBewerbung.database;

import com.carinaschoppe.playLegendBewerbung.PlayLegendBewerbung;
import com.carinaschoppe.playLegendBewerbung.configuration.Configuration;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.datasource.DataSourceConfig;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;

public class DatabaseServices {

  public static final Set<DatabasePlayer> DATABASE_PLAYERS = new HashSet<>();
  public static final Set<DatabaseRank> DATABASE_RANK = new HashSet<>();
  @Getter
  private static Database database;

  public static void loadRanks() {
    List<DatabaseRank> databaseRanks =
        DatabaseServices.getDatabase().find(DatabaseRank.class).findList();
    DATABASE_RANK.addAll(databaseRanks);
  }

  public static void loadPlayers() {
    List<DatabasePlayer> databasePlayers =
        DatabaseServices.getDatabase().find(DatabasePlayer.class).findList();
    DATABASE_PLAYERS.addAll(databasePlayers);
  }

  public static Database createDatabase() {

    // Speichern des ursprünglichen ClassLoaders
    ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
    ClassLoader pluginClassLoader = PlayLegendBewerbung.class.getClassLoader();

    try {
      // Setze den Plugin-ClassLoader als den aktuellen ContextClassLoader
      Thread.currentThread().setContextClassLoader(pluginClassLoader);

      // Erstelle die Ebean-Datenbank-Konfiguration
      io.ebean.config.DatabaseConfig serverConfig = new io.ebean.config.DatabaseConfig();
      serverConfig.setName("db");
      serverConfig.loadFromProperties();  // Lädt Konfiguration aus application.yaml oder ebean.properties
      serverConfig.setDdlGenerate(true);  // Generiert die Tabellen-SQL-Anweisungen
      serverConfig.setDdlRun(true);       // Führe die DDL aus, um die Tabellen zu erstellen
      serverConfig.setDefaultServer(true);
      serverConfig.setRegister(true);

      // Setze die Datenbankverbindung
      DataSourceConfig dataSourceConfig = new DataSourceConfig();
      dataSourceConfig.setUsername(
          Configuration.INSTANCE.getUsername());  // Setze falls erforderlich
      dataSourceConfig.setPassword(
          Configuration.INSTANCE.getPassword());  // Setze falls erforderlich
      dataSourceConfig.setUrl("jdbc:mysql://" + Configuration.INSTANCE.getHost() + ":" +
          Configuration.INSTANCE.getPort() + "/" + Configuration.INSTANCE.getDatabase());
      // SQLite-Dateipfad
      dataSourceConfig.setDriver("com.mysql.cj.jdbc.Driver");
      serverConfig.setDataSourceConfig(dataSourceConfig);
      // Initialisiere alle Klassen, die mit Ebean verbunden sind
      DatabasePlayer.init();  // Initialisiere die DatabasePlayer-Entität
      DatabaseRank.init();  // Initialisiere die DAO für Player


      // Erstelle die Datenbank mit dem Plugin-ClassLoader
      DatabaseFactory.createWithContextClassLoader(serverConfig, pluginClassLoader);
      database = DatabaseFactory.create(serverConfig);


      // Logge die erfolgreiche Erstellung der Datenbank
      PlayLegendBewerbung.getInstance().getLogger().info("Successfully created database");
    } catch (Exception e) {
      PlayLegendBewerbung.getInstance().getLogger()
          .severe("Error creating database: " + e.getMessage());
      e.printStackTrace();
    } finally {
      // Setze den originalen ClassLoader zurück, nachdem die Datenbank initialisiert wurde
      Thread.currentThread().setContextClassLoader(originalClassLoader);
    }

    return database;
  }

}
