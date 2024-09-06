package com.carinaschoppe.playLegendBewerbung;

import com.carinaschoppe.playLegendBewerbung.database.DatabasePlayer;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseRank;
import com.carinaschoppe.playLegendBewerbung.database.PlayerDAO;
import com.carinaschoppe.playLegendBewerbung.database.RankDAO;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.datasource.DataSourceConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;


public class PlayLegendBewerbung extends JavaPlugin {


    @Getter
    private static PlayLegendBewerbung instance;
    @Getter
    private final File pluginFolder = new File(Bukkit.getServer().getPluginsFolder(), "/PlayLegend");
    @Getter
    private final File databaseFile = new File(pluginFolder, "database.db");
    private static Database database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        makePluginFolder();
        createDatabaseFile();
        database = createDatabase();
        initialize(Bukkit.getPluginManager());
    }


    private Database createDatabase() {

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
            dataSourceConfig.setUsername("root");  // Setze falls erforderlich
            dataSourceConfig.setPassword("");  // Setze falls erforderlich
            dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/database");  // SQLite-Dateipfad
            dataSourceConfig.setDriver("com.mysql.cj.jdbc.Driver");
            serverConfig.setDataSourceConfig(dataSourceConfig);
            // Initialisiere alle Klassen, die mit Ebean verbunden sind
            DatabasePlayer.init();  // Initialisiere die DatabasePlayer-Entität
            PlayerDAO.init();  // Initialisiere die DAO für Player
            DatabaseRank.init();  // Initialisiere die DAO für Player
            RankDAO.init();  // Initialisiere die DAO für Player


            // Erstelle die Datenbank mit dem Plugin-ClassLoader
            DatabaseFactory.createWithContextClassLoader(serverConfig, pluginClassLoader);
            database = DatabaseFactory.create(serverConfig);


            // Logge die erfolgreiche Erstellung der Datenbank
            this.getLogger().info("Successfully created database");
        } catch (Exception e) {
            this.getLogger().severe("Error creating database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Setze den originalen ClassLoader zurück, nachdem die Datenbank initialisiert wurde
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }

        return database;
    }

    private void createDatabaseFile() {
        if (!databaseFile.exists()) {
            try {
                databaseFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void makePluginFolder() {
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }
    }

    private void initialize(@NotNull PluginManager pluginManager) {
        getLogger().info("Plugin aktiviert und mit SQLite-Datenbank verbunden: " + databaseFile.getAbsolutePath());
    }


    @Override
    public void onDisable() {

        getLogger().info("Plugin deaktiviert.");   // Plugin shutdown logic
    }

}
