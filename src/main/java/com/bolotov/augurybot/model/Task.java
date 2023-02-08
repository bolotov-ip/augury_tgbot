package com.bolotov.augurybot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="task")
public class Task implements ViewButton{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "name_product")
    String nameProduct;

    @Column(name = "comment")
    String comment;

    @Column(name = "author")
    String author;

    @Column(name = "order_type")
    String orderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    State state =State.WORKING;

    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        String buttonName = "Задача №" + getId() + " " + getNameProduct();
        return buttonName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
