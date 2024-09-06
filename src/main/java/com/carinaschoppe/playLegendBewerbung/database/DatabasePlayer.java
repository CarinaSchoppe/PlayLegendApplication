package com.carinaschoppe.playLegendBewerbung.database;

import io.ebean.Model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "players")
@Getter
@Setter
public class DatabasePlayer extends Model {
    @Id
    @Column(name = "uuid", unique = true, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rank_name")
    private DatabaseRank databaseRank;

    public static void init() {
    }
    @Column(name = "rank_expiry")
    private LocalDateTime rankExpiry;

    @Column(name = "name")
    private String name;

    @Column(name = "permanent", nullable = false)
    private boolean permanent;

}
