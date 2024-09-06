package com.carinaschoppe.playLegendBewerbung.messages;

import com.carinaschoppe.playLegendBewerbung.PlayLegendBewerbung;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MessageHandler {


  public static final File MESSAGES_FILE =
      new File(PlayLegendBewerbung.getInstance().getPluginFolder(), "messages.json");


  public static void load() {
    var gson = new Gson();
    //convert the Messages class to json
    if (!MESSAGES_FILE.exists()) {
      try {
        MESSAGES_FILE.createNewFile();
        Messages.INSTANCE = new Messages();
        save();
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        Messages.INSTANCE = gson.fromJson(new FileReader(MESSAGES_FILE), Messages.class);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }

    }
    //load the messages from to file
  }

  private static void save() {
    var gson = new GsonBuilder().setPrettyPrinting().create();
    try {
      gson.toJson(Messages.INSTANCE, new FileWriter(MESSAGES_FILE));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

}
