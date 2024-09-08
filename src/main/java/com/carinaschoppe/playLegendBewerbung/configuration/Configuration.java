package com.carinaschoppe.playLegendBewerbung.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Configuration {

  public static Configuration INSTANCE;

  private String prefix = "[PlayLegend] ";
  private String host = "localhost";
  private int port = 3306;
  private String username = "root";
  private String password = "";
  private String database = "database";
  private String type = "sqlite";



}
