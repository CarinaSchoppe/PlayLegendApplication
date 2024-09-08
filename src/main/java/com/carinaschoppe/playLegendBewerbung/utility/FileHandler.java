package com.carinaschoppe.playLegendBewerbung.utility;

import com.carinaschoppe.playLegendBewerbung.configuration.ConfigurationHandler;
import com.carinaschoppe.playLegendBewerbung.messages.MessageHandler;
import java.io.File;

public class FileHandler {


  public static void loadFiles(File pluginsFolder) {
    ConfigurationHandler.load(pluginsFolder);
    MessageHandler.load(pluginsFolder);
  }


  public static void makePluginFolder(File pluginsFolder) {
    if (!pluginsFolder.exists()) {
      pluginsFolder.mkdirs();
    }
  }

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
