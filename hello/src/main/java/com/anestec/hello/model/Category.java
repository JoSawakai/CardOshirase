package com.anestec.hello.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "CATEGORY", uniqueConstraints = @UniqueConstraint(columnNames = "KUBUN"))
public class Category {

    @Id
    private Long id;  // 主キー

    @Column(nullable = false, name = "KUBUN")
    private String category;

    @Column(nullable = false, name = "DISPLAYNAME")
    private String displayName;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKubun() {
        return category;
    }

    public void setKubun(String kubun) {
        this.category = kubun;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
