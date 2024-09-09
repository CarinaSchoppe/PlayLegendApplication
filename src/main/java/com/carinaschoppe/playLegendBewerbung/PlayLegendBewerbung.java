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


  /**
   * Called when the plugin is enabled.
   *
   * <p>This is called when the plugin is enabled, either by the server administrator or by the
   * plugin itself. This is a good place to put code to initialize any resources that the plugin
   * needs to do its job.
   *
   * <p>In this plugin, the following actions are performed:
   * <ul>
   *   <li>The plugin folder is created if it does not exist.</li>
   *   <li>The database is created.</li>
   *   <li>The players and ranks are loaded from the database.</li>
   *   <li>The default ranks are loaded.</li>
   *   <li>The {@link #initialize(PluginManager)} method is called to initialize the plugin.
   * </ul>
   */
  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;

    Bukkit.getScheduler().runTaskAsynchronously(this, this::fileLoading);
    initialize(Bukkit.getPluginManager());
  }


  private void fileLoading() {
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

  }


  /**
   * Initializes the plugin and registers all events, commands, and listeners if InSigns is enabled.
   *
   * @param pluginManager the plugin manager
   */
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


  /**
   * Called when the plugin is disabled.
   *
   * <p>This is called when the plugin is disabled, either by the server administrator or by the
   * plugin itself. This is a good place to put code to clean up any resources that the plugin
   * created or to cancel any tasks that were started when the plugin was enabled.
   *
   * <p>The plugin will not be able to perform any tasks after this method is called, so it
   * should be used to clean up and resources that the plugin created or to cancel any tasks that
   * were started when the plugin was enabled.
   *
   * @see org.bukkit.plugin.Plugin#onDisable()
   */
  @Override
  public void onDisable() {

    getLogger().info("Plugin deaktiviert.");   // Plugin shutdown logic
  }

}
