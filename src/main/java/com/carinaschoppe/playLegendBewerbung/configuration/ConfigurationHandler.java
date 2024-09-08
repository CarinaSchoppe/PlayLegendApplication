package com.carinaschoppe.playLegendBewerbung.configuration;

import com.carinaschoppe.playLegendBewerbung.PlayLegendBewerbung;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import lombok.Getter;

@Getter
public class ConfigurationHandler {

  private static final File CONFIG_FILE =
      new File(PlayLegendBewerbung.getInstance().getPluginFolder(), "configuration.json");


  public static void load() {
    var gson = new Gson();
    //convert the Messages class to json
    if (!CONFIG_FILE.exists()) {
      try {
        CONFIG_FILE.createNewFile();
        save();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    try {
      Configuration.INSTANCE = gson.fromJson(new FileReader(CONFIG_FILE), Configuration.class);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }


    //load the messages from to file
  }

  public static void save() {
    if (Configuration.INSTANCE == null) {
      Configuration.INSTANCE = new Configuration();
    }
    var gson = new GsonBuilder().setPrettyPrinting().create();
    try {
      var code = gson.toJson(Configuration.INSTANCE);
      var writer = new FileWriter(CONFIG_FILE, false);
      writer.write(code);
      writer.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

}
