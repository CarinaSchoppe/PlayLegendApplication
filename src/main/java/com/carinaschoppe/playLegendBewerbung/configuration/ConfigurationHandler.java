package com.carinaschoppe.playLegendBewerbung.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class ConfigurationHandler {

  private static final File CONFIG_FILE =
      new File(Bukkit.getServer().getPluginsFolder(), "/PlayLegend/configuration.json");


  public static void load() {
    var gson = new Gson();
    //convert the Messages class to json
    if (!CONFIG_FILE.exists()) {
      try {
        CONFIG_FILE.createNewFile();
        Configuration.INSTANCE = new Configuration();
        save();
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        Configuration.INSTANCE = gson.fromJson(new FileReader(CONFIG_FILE), Configuration.class);
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }

    }
    //load the messages from to file
  }

  private static void save() {
    var gson = new GsonBuilder().setPrettyPrinting().create();
    try {
      gson.toJson(Configuration.INSTANCE, new FileWriter(CONFIG_FILE));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

}
