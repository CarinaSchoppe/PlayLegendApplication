package com.carinaschoppe.playLegendBewerbung;

import com.carinaschoppe.playLegendBewerbung.commands.PermissionsManagementCommand;
import com.carinaschoppe.playLegendBewerbung.commands.PlayerRankManagementCommand;
import com.carinaschoppe.playLegendBewerbung.commands.RankManagementCommand;
import com.carinaschoppe.playLegendBewerbung.commands.TimeLeftCommand;
import com.carinaschoppe.playLegendBewerbung.configuration.ConfigurationHandler;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.database.RankGeneration;
import com.carinaschoppe.playLegendBewerbung.events.PlayerJoinsServerEvent;
import com.carinaschoppe.playLegendBewerbung.events.PlayerLoginEvent;
import com.carinaschoppe.playLegendBewerbung.messages.MessageHandler;
import com.carinaschoppe.playLegendBewerbung.ranklogic.RankHandler;
import java.io.File;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public class PlayLegendBewerbung extends JavaPlugin {


  @Getter
  private static PlayLegendBewerbung instance;
  @Getter
  private final File pluginFolder = new File(Bukkit.getServer().getPluginsFolder(), "/PlayLegend");


  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;
    makePluginFolder();


    loadFiles();

    DatabaseServices.createDatabase();
    DatabaseServices.loadPlayers();
    DatabaseServices.loadRanks();
    RankGeneration.loadDefaultRanks();

    initialize(Bukkit.getPluginManager());
  }

  private void loadFiles() {
    ConfigurationHandler.save();
    MessageHandler.save();

  }


  private void makePluginFolder() {
    if (!pluginFolder.exists()) {
      pluginFolder.mkdirs();
    }
  }

  private void initialize(@NotNull PluginManager pluginManager) {


    RankHandler.playerRankRemover();
    pluginManager.registerEvents(new PlayerJoinsServerEvent(), this);
    pluginManager.registerEvents(new PlayerLoginEvent(), this);

    this.getCommand("rank").setExecutor(new RankManagementCommand());
    this.getCommand("time").setExecutor(new TimeLeftCommand());
    this.getCommand("playergroup").setExecutor(new PlayerRankManagementCommand());
    this.getCommand("permission").setExecutor(new PermissionsManagementCommand());
    getLogger().info("Plugin aktiviert und mit MySQL-Datenbank verbunden!");
  }


  @Override
  public void onDisable() {

    getLogger().info("Plugin deaktiviert.");   // Plugin shutdown logic
  }

}
