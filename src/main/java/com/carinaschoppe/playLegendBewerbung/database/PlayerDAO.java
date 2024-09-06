package com.carinaschoppe.playLegendBewerbung.database;

import io.ebean.Database;
import io.ebean.Query;
import io.ebean.annotation.Transactional;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class PlayerDAO {
    public PlayerDAO(Database database) {
        this.database = database;
    }
    private final Database database;

    public static void init() {
    }

    /**
     * Speichert oder aktualisiert einen Spieler in der Datenbank.
     *
     * @param databasePlayer Das zu speichernde Player-Objekt.
     */
    @Transactional
    public void savePlayer(DatabasePlayer databasePlayer) {
        database.save(databasePlayer);  // Automatisch speichern oder aktualisieren
    }

    /**
     * Holt einen Spieler anhand seiner UUID aus der Datenbank.
     *
     * @param uuid Die UUID des Spielers.
     * @return Der Spieler mit der angegebenen UUID oder null, falls nicht gefunden.
     */
    @Transactional

    public Optional<DatabasePlayer> getPlayerByUUID(UUID uuid) {
        return Optional.ofNullable(database.find(DatabasePlayer.class, uuid));
    }

    /**
     * Holt alle Spieler aus der Datenbank.
     *
     * @return Eine Liste aller Spieler.
     */
    @Transactional
    public List<DatabasePlayer> getAllPlayers() {
        // Erstellen einer Ebean-Abfrage, um alle Spieler abzurufen
        Query<DatabasePlayer> query = database.find(DatabasePlayer.class);

        // Abfrage ausführen und Liste der Spieler zurückgeben
        return query.findList();
    }

    public List<DatabasePlayer> findPlayersByName(String name) {
        Query<DatabasePlayer> query = database.find(DatabasePlayer.class).where().eq("name", name).query();
        return query.findList();
    }

    public void updatePlayer(DatabasePlayer player) {
        database.update(player);
    }

    // Spieler löschen
    public void deletePlayer(DatabasePlayer player) {
        database.delete(player);
    }
}
