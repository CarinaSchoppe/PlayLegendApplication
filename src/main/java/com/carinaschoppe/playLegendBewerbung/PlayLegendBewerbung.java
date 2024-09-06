package com.carinaschoppe.playLegendBewerbung;

import com.carinaschoppe.playLegendBewerbung.configuration.ConfigurationHandler;
import com.carinaschoppe.playLegendBewerbung.database.DatabaseServices;
import com.carinaschoppe.playLegendBewerbung.database.RankGeneration;
import com.carinaschoppe.playLegendBewerbung.events.PlayerJoinsServerEvent;
import com.carinaschoppe.playLegendBewerbung.messages.MessageHandler;
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
    ConfigurationHandler.load();
    MessageHandler.load();

  }


  private void makePluginFolder() {
    if (!pluginFolder.exists()) {
      pluginFolder.mkdirs();
    }
  }

  private void initialize(@NotNull PluginManager pluginManager) {


    pluginManager.registerEvents(new PlayerJoinsServerEvent(), this);

    getLogger().info("Plugin aktiviert und mit MySQL-Datenbank verbunden!");
  }


  @Override
  public void onDisable() {

    getLogger().info("Plugin deaktiviert.");   // Plugin shutdown logic
  }

}
