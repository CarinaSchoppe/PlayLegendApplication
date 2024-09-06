package com.carinaschoppe.playLegendBewerbung;

import com.carinaschoppe.playLegendBewerbung.database.PlayerDAO;
import com.carinaschoppe.playLegendBewerbung.database.Rank;
import com.carinaschoppe.playLegendBewerbung.database.RankDAO;
import io.ebean.DB;
import io.ebean.Database;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;


public class PlayLegendBewerbung extends JavaPlugin {


    @Getter
    private static PlayLegendBewerbung instance;
    @Getter
    private final File pluginFolder = new File(Bukkit.getServer().getPluginsFolder(), "/PlayLegend");
    @Getter
    private final File databaseFile = new File(pluginFolder, "database.db");
    private PlayerDAO playerDAO;
    private RankDAO rankDAO;
    private Database database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        makePluginFolder();
        createDatabaseFile();
        doDatabaseStuff();
        initialize(Bukkit.getPluginManager());
    }

    private void doDatabaseStuff() {
        database = DB.getDefault();
        playerDAO = new PlayerDAO();
        rankDAO = new RankDAO();

        // Beispiel für das Speichern eines neuen Ranks
        Rank exampleRank = new Rank();
        exampleRank.setRankName("Admin");
        exampleRank.setLevel(1);
        exampleRank.setPermissions(List.of("server.manage", "player.kick"));

        // Rank in der Datenbank speichern
        rankDAO.saveOrUpdateRank(exampleRank);

        getLogger().info("MyPlugin enabled, Ebean initialized, and Rank saved.");

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
