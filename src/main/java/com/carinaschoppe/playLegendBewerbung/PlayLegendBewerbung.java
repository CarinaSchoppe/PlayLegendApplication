package com.carinaschoppe.playLegendBewerbung;

import com.carinaschoppe.playLegendBewerbung.commands.PermissionsManagementCommand;
import com.carinaschoppe.playLegendBewerbung.commands.PlayerRankManagementCommand;
import com.carinaschoppe.playLegendBewerbung.commands.RankManagementCommand;
import com.carinaschoppe.playLegendBewerbung.commands.TimeLeftCommand;
import com.carinaschoppe.playLegendBewerbung.configuration.Configuration;
import com.carinaschoppe.playLegendBewerbung.configuration.ConfigurationHandler;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.database.RankGeneration;
import com.carinaschoppe.playLegendBewerbung.events.PlayerChatEvent;
import com.carinaschoppe.playLegendBewerbung.events.PlayerJoinsServerEvent;
import com.carinaschoppe.playLegendBewerbung.events.PlayerLoginEvent;
import com.carinaschoppe.playLegendBewerbung.events.SignRelatedEvents;
import com.carinaschoppe.playLegendBewerbung.messages.MessageHandler;
import com.carinaschoppe.playLegendBewerbung.ranklogic.RankHandler;
import java.io.File;
import java.util.Objects;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayLegendBewerbung extends JavaPlugin {


  @Getter
  private static PlayLegendBewerbung instance;
  @Getter
  private final File pluginFolder = new File(Bukkit.getServer().getPluginsFolder(), "/PlayLegend");


  private final File databaseFile = new File(pluginFolder, "database.db");

  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;
    makePluginFolder();
    loadFiles();
    if (Configuration.INSTANCE.getType().equalsIgnoreCase("sqlite")) {
      makeDatabaseFile();
    }

    DatabaseServices.createDatabase();
    DatabaseServices.loadPlayers();
    DatabaseServices.loadRanks();
    RankGeneration.loadDefaultRanks();

    initialize(Bukkit.getPluginManager());
  }

  private void makeDatabaseFile() {
    if (!databaseFile.exists()) {
      try {
        databaseFile.createNewFile();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void loadFiles() {
    ConfigurationHandler.load();
    MessageHandler.load();
  }


  private void makePluginFolder() {
    if (!pluginFolder.exists()) {
      pluginFolder.mkdirs();
    }
  }

  private void initialize(@NotNull PluginManager pluginManager) {


    Plugin insignsPlugin = getServer().getPluginManager().getPlugin("InSigns");
    if ((insignsPlugin != null) && insignsPlugin.isEnabled()) {
      // Replaces "[PLAYER]" with the player's name on signs and checks for the 'insigns.create.player' permission whenever a player tries to create a sign with "[PLAYER]" on it


      RankHandler.playerRankRemover();
      pluginManager.registerEvents(new PlayerJoinsServerEvent(), this);
      pluginManager.registerEvents(new PlayerLoginEvent(), this);
      pluginManager.registerEvents(new PlayerChatEvent(), this);
      pluginManager.registerEvents(new SignRelatedEvents(), this);

      Objects.requireNonNull(this.getCommand("rank")).setExecutor(new RankManagementCommand());
      Objects.requireNonNull(this.getCommand("time")).setExecutor(new TimeLeftCommand());
      Objects.requireNonNull(this.getCommand("playergroup"))
          .setExecutor(new PlayerRankManagementCommand());
      Objects.requireNonNull(this.getCommand("permission"))
          .setExecutor(new PermissionsManagementCommand());


      getLogger().info("Plugin aktiviert und mit MySQL-Datenbank verbunden!");
    }
  }


  @Override
  public void onDisable() {

    getLogger().info("Plugin deaktiviert.");   // Plugin shutdown logic
  }

}
