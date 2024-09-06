package com.carinaschoppe.playLegendBewerbung.database;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rank")
@Getter
@Setter
public class Rank {

    @Id
    @Column(name = "rank_name", nullable = false, unique = true)
    private String rankName;

    @Column(name = "prefix")
    private String prefix;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "rank_permissions", joinColumns = @JoinColumn(name = "rank_name"))
    @Column(name = "permission")
    private List<String> permissions;

    @Column(name = "level", nullable = false)
    private int level;

}
