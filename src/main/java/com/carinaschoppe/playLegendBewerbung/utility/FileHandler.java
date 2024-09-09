package com.carinaschoppe.playLegendBewerbung.utility;

import com.carinaschoppe.playLegendBewerbung.configuration.ConfigurationHandler;
import com.carinaschoppe.playLegendBewerbung.messages.MessageHandler;
import java.io.File;

public class FileHandler {


  /**
   * Loads the configuration and messages from the given plugin folder.
   *
   * @param pluginsFolder the folder containing the configuration and messages
   */
  public static void loadFiles(File pluginsFolder) {
    ConfigurationHandler.load(pluginsFolder);
    MessageHandler.load(pluginsFolder);
  }


  /**
   * Creates the plugin's folder if it does not exist.
   *
   * @param pluginsFolder The folder to create.
   */
  public static void makePluginFolder(File pluginsFolder) {
    if (!pluginsFolder.exists()) {
      pluginsFolder.mkdirs();
    }
  }

  /**
   * Creates a new database file if it doesn't exist.
   *
   * @param databaseFile the file to be created
   */
  public static void makeDatabaseFile(File databaseFile) {
    if (!databaseFile.exists()) {
      try {
        databaseFile.createNewFile();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
