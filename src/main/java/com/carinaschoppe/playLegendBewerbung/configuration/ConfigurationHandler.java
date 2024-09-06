package com.carinaschoppe.playLegendBewerbung.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.File;

@Getter
public class ConfigurationHandler {

    private static Configuration instance;
    private final File configFile = new File(Bukkit.getServer().getPluginsFolder(), "/PlayLegend/configuration.json");

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public void save() {

    }

    public void load() {

    }

}
