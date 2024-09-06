package com.carinaschoppe.playLegendBewerbung.database;


import io.ebean.Database;
import io.ebean.annotation.Transactional;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class RankDAO {


    private final Database database;

    public RankDAO(Database database) {
        this.database = database;
    }

    public static void init() {
    }


    /**
     * Speichert oder aktualisiert einen Rank in der Datenbank.
     *
     * @param databaseRank Das zu speichernde oder zu aktualisierende Rank-Objekt.
     */
    @Transactional
    public void saveOrUpdateRank(DatabaseRank databaseRank) {
        database.save(databaseRank);  // Rank speichern oder aktualisieren
    }

    /**
     * Holt einen Rank anhand seines Namens aus der Datenbank.
     *
     * @param rankName Der Name des Ranks.
     * @return Der Rank mit dem angegebenen Namen oder null, falls nicht gefunden.
     */
    @Transactional
    public DatabaseRank getRankByName(String rankName) {
        return database.find(DatabaseRank.class).where().eq("name", rankName).findOne();
    }

    /**
     * Holt alle Ranks aus der Datenbank.
     *
     * @return Eine Liste aller Ranks.
     */
    @Transactional
    public List<DatabaseRank> getAllRanks() {
        return database.find(DatabaseRank.class).findList();
    }

    /**
     * Löscht einen Rank aus der Datenbank.
     *
     * @param databaseRank Das zu löschende Rank-Objekt.
     */
    @Transactional
    public void deleteRank(DatabaseRank databaseRank) {
        database.delete(databaseRank);  // Rank löschen
    }


    /**
     * Gibt alle Permissions des Ranks, sowie alle Permissions der untergeordneten Ranks zurück.
     *
     * @param databaseRank Der Rank, dessen Permissions abgefragt werden sollen.
     * @return eine Liste aller Permissions des Ranks und der untergeordneten Ranks.
     */
    public List<String> getAllPermissionsOfRankAndChildren(DatabaseRank databaseRank) {
        List<String> allPermissions = new ArrayList<>(databaseRank.getPermissions());

        for (DatabaseRank child : getAllRanks()) {
            if (child.getLevel() < databaseRank.getLevel()) {
                allPermissions.addAll(child.getPermissions());
            }
        }
        allPermissions.addAll(databaseRank.getPermissions());
        return allPermissions;
    }
}
