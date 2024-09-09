package com.carinaschoppe.playLegendBewerbung.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MessageHandler {


  public static File MESSAGES_FILE;


  /**
   * Loads the messages from the messages.json file in the given plugins folder.
   * If the file does not exist, it will be created and the default messages will be written to it.
   *
   * @param pluginsFolder the folder where the messages.json file should be located
   */
  public static void load(File pluginsFolder) {
    var gson = new Gson();
    MESSAGES_FILE = new File(pluginsFolder, "messages.json");
    //convert the Messages class to json
    if (!MESSAGES_FILE.exists()) {
      try {
        pluginsFolder.mkdirs();
        MESSAGES_FILE.createNewFile();
        save();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    try {
      Messages.INSTANCE = gson.fromJson(new FileReader(MESSAGES_FILE), Messages.class);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Saves the messages to the messages.json file in the given plugins folder.
   * If the Messages instance is null, a new one will be created with the default messages.
   * This method will throw a RuntimeException if the file cannot be written for any reason.
   */
  private static void save() {
    if (Messages.INSTANCE == null) {
      Messages.INSTANCE = new Messages();
    }
    var gson = new GsonBuilder().setPrettyPrinting().create();
    try {
      var code = gson.toJson(Messages.INSTANCE);
      var writer = new FileWriter(MESSAGES_FILE, false);
      writer.write(code);
      writer.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

}
