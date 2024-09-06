package com.carinaschoppe.playLegendBewerbung.database;


import io.ebean.DB;
import io.ebean.Database;
import io.ebean.annotation.Transactional;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class RankDAO {


    private final Database database;

    public RankDAO() {
        this.database = DB.getDefault();
    }


    /**
     * Speichert oder aktualisiert einen Rank in der Datenbank.
     *
     * @param rank Das zu speichernde oder zu aktualisierende Rank-Objekt.
     */
    @Transactional
    public void saveOrUpdateRank(Rank rank) {
        database.save(rank);  // Rank speichern oder aktualisieren
    }

    /**
     * Holt einen Rank anhand seines Namens aus der Datenbank.
     *
     * @param rankName Der Name des Ranks.
     * @return Der Rank mit dem angegebenen Namen oder null, falls nicht gefunden.
     */
    @Transactional
    public Rank getRankByName(String rankName) {
        return database.find(Rank.class).where().eq("name", rankName).findOne();
    }

    /**
     * Holt alle Ranks aus der Datenbank.
     *
     * @return Eine Liste aller Ranks.
     */
    @Transactional
    public List<Rank> getAllRanks() {
        return database.find(Rank.class).findList();
    }

    /**
     * Löscht einen Rank aus der Datenbank.
     *
     * @param rank Das zu löschende Rank-Objekt.
     */
    @Transactional
    public void deleteRank(Rank rank) {
        database.delete(rank);  // Rank löschen
    }


    /**
     * Gibt alle Permissions des Ranks, sowie alle Permissions der untergeordneten Ranks zurück.
     *
     * @param rank Der Rank, dessen Permissions abgefragt werden sollen.
     * @return eine Liste aller Permissions des Ranks und der untergeordneten Ranks.
     */
    public List<String> getAllPermissionsOfRankAndChildren(Rank rank) {
        List<String> allPermissions = new ArrayList<>(rank.getPermissions());

        for (Rank child : getAllRanks()) {
            if (child.getLevel() < rank.getLevel()) {
                allPermissions.addAll(child.getPermissions());
            }
        }
        allPermissions.addAll(rank.getPermissions());
        return allPermissions;
    }
}
