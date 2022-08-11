package com.mahmoudh.kiwe.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="PRODUCT_ID")
    Integer productId;

    @Column(name="NAME")
    String name;

    @Column(name="QUANTITY")
    Integer quantity;

    @Column(name="CATEGORY")
    String category;

    @ManyToOne
    @JoinColumn(name="USER_ID", nullable=false)
    private User user;

    @OneToMany(mappedBy="product")
    private Set<Image> image;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Image> getImage() {
        return image;
    }

    public void setImage(Set<Image> image) {
        this.image = image;
    }
}
