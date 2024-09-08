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


    //load the messages from to file
  }

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
