package com.mahmoudh.kiwe.entity;

import javax.persistence.*;

@Entity
@Table(name = "IMAGE")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="IMAGE_ID")
    Integer imageId;

    @Column(name = "IMAGE", columnDefinition="BLOB")
    private byte[] image;



    @ManyToOne
    @JoinColumn(name="PRODUCT_ID", nullable=false)
    private Product product;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
