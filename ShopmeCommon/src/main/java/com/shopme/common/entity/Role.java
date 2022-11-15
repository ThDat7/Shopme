package com.shopme.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roles")

@Getter @Setter @NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 150)
    private String description;

    public Role(int id) {
        this.id = id;
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }


}
