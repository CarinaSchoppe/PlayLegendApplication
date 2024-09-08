package com.carinaschoppe.playLegendBewerbung.database;

import com.carinaschoppe.playLegendBewerbung.PlayLegendBewerbung;
import com.carinaschoppe.playLegendBewerbung.configuration.Configuration;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.datasource.DataSourceConfig;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class DatabaseServices {

  public static final Set<DatabasePlayer> DATABASE_PLAYERS = new HashSet<>();
  public static final Set<DatabaseRank> DATABASE_RANK = new HashSet<>();
  @Getter
  @Setter
  private static Database database;

  public static void loadRanks() {
    List<DatabaseRank> databaseRanks =
        database.find(DatabaseRank.class).findList();
    DATABASE_RANK.addAll(databaseRanks);
  }

  public static void loadPlayers() {
    List<DatabasePlayer> databasePlayers =
        database.find(DatabasePlayer.class).findList();
    DATABASE_PLAYERS.addAll(databasePlayers);
  }

  public static Database createDatabase(File databaseFile) {

    // Speichern des ursprünglichen ClassLoaders
    ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
    ClassLoader pluginClassLoader = PlayLegendBewerbung.class.getClassLoader();

    try {
      // Setze den Plugin-ClassLoader als den aktuellen ContextClassLoader
      Thread.currentThread().setContextClassLoader(pluginClassLoader);
      // Erstelle die Ebean-Datenbank-Konfiguration
      io.ebean.config.DatabaseConfig serverConfig = new io.ebean.config.DatabaseConfig();
      serverConfig.setName("db");
      serverConfig.setDdlGenerate(true);  // Generiert die Tabellen-SQL-Anweisungen
      serverConfig.setDdlRun(true);
      // Setze die Datenbankverbindung
      DataSourceConfig dataSourceConfig = new DataSourceConfig();
      dataSourceConfig.setUsername(
          Configuration.INSTANCE.getUsername());  // Setze falls erforderlich
      dataSourceConfig.setPassword(
          Configuration.INSTANCE.getPassword());  // Setze falls erforderlich
      if (Configuration.INSTANCE.getType().equalsIgnoreCase("sqlite")) {
        dataSourceConfig.setUrl(
            "jdbc:sqlite:" + databaseFile.getAbsolutePath());
        // SQLite-Dateipfad
        dataSourceConfig.setDriver("org.sqlite.JDBC");
      } else if (Configuration.INSTANCE.getType().equalsIgnoreCase("mysql")) {
        dataSourceConfig.setUrl("jdbc:mysql://" + Configuration.INSTANCE.getHost() + ":" +
            Configuration.INSTANCE.getPort() + "/" + Configuration.INSTANCE.getDatabase());
        // SQLite-Dateipfad
        dataSourceConfig.setDriver("com.mysql.cj.jdbc.Driver");
      }


      serverConfig.setDataSourceConfig(dataSourceConfig);
      // Initialisiere alle Klassen, die mit Ebean verbunden sind
      DatabasePlayer.init();  // Initialisiere die DatabasePlayer-Entität
      DatabaseRank.init();  // Initialisiere die DAO für Player


      // Erstelle die Datenbank mit dem Plugin-ClassLoader
      DatabaseFactory.createWithContextClassLoader(serverConfig, pluginClassLoader);
      database = DatabaseFactory.create(serverConfig);

      // Logge die erfolgreiche Erstellung der Datenbank
      if (PlayLegendBewerbung.getInstance() != null) {
        PlayLegendBewerbung.getInstance().getLogger().info("Successfully created database");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // Setze den originalen ClassLoader zurück, nachdem die Datenbank initialisiert wurde
      Thread.currentThread().setContextClassLoader(originalClassLoader);
    }

    return database;
  }

}
