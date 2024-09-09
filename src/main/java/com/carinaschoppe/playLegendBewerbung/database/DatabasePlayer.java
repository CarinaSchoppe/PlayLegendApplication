package com.carinaschoppe.playLegendBewerbung.database;

import io.ebean.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Entity
@Table(name = "players")
@Getter
public class DatabasePlayer extends Model {
  @Id
  @Column(name = "uuid", unique = true, nullable = false)
  private UUID uuid;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "rank_name")
  private DatabaseRank databaseRank;
  @Column(name = "rank_expiry")
  private LocalDateTime rankExpiry;
  @Column(name = "name")
  private String name;
  @Column(name = "permanent", nullable = false)
  private boolean permanent;

  public static void init() {
  }

  public DatabasePlayer setUuid(UUID uuid) {
    this.uuid = uuid;
    return this;
  }

  public DatabasePlayer setDatabaseRank(DatabaseRank databaseRank) {
    this.databaseRank = databaseRank;
    return this;
  }

  public DatabasePlayer setRankExpiry(LocalDateTime rankExpiry) {
    this.rankExpiry = rankExpiry;
    return this;
  }

  public DatabasePlayer setName(String name) {
    this.name = name;
    return this;
  }

  public DatabasePlayer setPermanent(boolean permanent) {
    this.permanent = permanent;
    return this;
  }

}
