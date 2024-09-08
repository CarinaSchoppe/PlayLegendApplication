package com.carinaschoppe.playLegendBewerbung;

import com.carinaschoppe.playLegendBewerbung.commands.PermissionsManagementCommand;
import com.carinaschoppe.playLegendBewerbung.commands.PlayerRankManagementCommand;
import com.carinaschoppe.playLegendBewerbung.commands.RankManagementCommand;
import com.carinaschoppe.playLegendBewerbung.commands.TimeLeftCommand;
import com.carinaschoppe.playLegendBewerbung.configuration.Configuration;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.database.DefaultRankGeneration;
import com.carinaschoppe.playLegendBewerbung.events.PlayerChatEvent;
import com.carinaschoppe.playLegendBewerbung.events.PlayerJoinsServerEvent;
import com.carinaschoppe.playLegendBewerbung.events.PlayerLoginEvent;
import com.carinaschoppe.playLegendBewerbung.events.SignRelatedEvents;
import com.carinaschoppe.playLegendBewerbung.ranklogic.RankHandler;
import com.carinaschoppe.playLegendBewerbung.utility.FileHandler;
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


  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;
    var pluginsFolder = new File(Bukkit.getServer().getPluginsFolder(), "/PlayLegend");
    var databaseFile = new File(pluginsFolder, "database.db");
    FileHandler.makePluginFolder(pluginsFolder);
    FileHandler.loadFiles(pluginsFolder);
    if (Configuration.INSTANCE.getType().equalsIgnoreCase("sqlite")) {
      FileHandler.makeDatabaseFile(databaseFile);
    }

    DatabaseServices.createDatabase(databaseFile);

    DatabaseServices.loadPlayers();
    DatabaseServices.loadRanks();
    DefaultRankGeneration.loadDefaultRanks();

    initialize(Bukkit.getPluginManager());
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
