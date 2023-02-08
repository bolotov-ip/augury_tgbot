package com.bolotov.augurybot.model;

import javax.persistence.*;

@Entity
@Table(name="card")
public class Card implements ViewButton{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "name")
    String nameCard;

    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return nameCard;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }
}
