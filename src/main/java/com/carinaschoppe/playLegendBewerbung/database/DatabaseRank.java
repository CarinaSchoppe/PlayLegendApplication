package com.carinaschoppe.playLegendBewerbung.database;


import io.ebean.Model;
import io.ebean.annotation.Transactional;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * Ein Datenbankeintrag, der einen Rang darstellt
 */
@Getter
@Entity
@Table(name = "rank")
public class DatabaseRank extends Model {
  @Id
  @GeneratedValue()
  @Column(name = "rankID", nullable = false, unique = true)
  private int rankID;
  @Column(name = "rank_name", nullable = false, unique = true)
  private String rankName;
  @Column(name = "prefix")
  private String prefix;
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "rank_permissions", joinColumns = @JoinColumn(name = "rankID"))
  @Column(name = "permission")
  private List<String> permissions;
  @Column(name = "level", nullable = false)
  private int level;

  public static void init() {
  }

  public static DatabaseRank getPlayerRank(Player player) {
    var dbPlayer = DatabaseServices.DATABASE_PLAYERS.stream()
        .filter(p -> p.getUuid().equals(player.getUniqueId())).findFirst().get();
    if (dbPlayer == null) {
      return null;
    }

    return dbPlayer.getDatabaseRank();

  }


  /**
   * Holt einen Rank anhand seines Namens aus der Datenbank.
   *
   * @param rankName Der Name des Ranks.
   * @return Der Rank mit dem angegebenen Namen oder null, falls nicht gefunden.
   */
  @Transactional
  public DatabaseRank getRankByName(String rankName) {
    return DatabaseServices.getDatabase()
        .find(DatabaseRank.class).where().eq("name", rankName).findOne();
  }

  /**
   * Holt alle Ranks aus der Datenbank.
   *
   * @return Eine Liste aller Ranks.
   */
  @Transactional
  public List<DatabaseRank> getAllRanks() {
    return DatabaseServices.getDatabase().find(DatabaseRank.class).findList();
  }

  /**
   * Gibt alle Permissions des Ranks, sowie alle Permissions der untergeordneten Ranks zurück.
   *
   * @return eine Liste aller Permissions des Ranks und der untergeordneten Ranks.
   */
  public List<String> getAllPermissionsOfRankAndChildren() {
    List<String> allPermissions = new ArrayList<>(getPermissions());

    for (DatabaseRank child : getAllRanks()) {
      if (child.getLevel() < getLevel()) {
        allPermissions.addAll(child.getPermissions());
      }
    }
    allPermissions.addAll(getPermissions());
    return allPermissions;
  }


  public DatabaseRank setPermissions(List<String> permissions) {
    this.permissions = permissions;
    return this;
  }

  public DatabaseRank setRankName(String rankName) {
    this.rankName = rankName;
    return this;
  }

  public DatabaseRank setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  public DatabaseRank setLevel(int level) {
    this.level = level;
    return this;
  }

}
