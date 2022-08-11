package com.mahmoudh.kiwe.dto;

import com.mahmoudh.kiwe.entity.Image;
import com.mahmoudh.kiwe.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsResponse {
    Integer productId;
    String name;
    String category;
    Integer quantity;
    List<ImageResponse> images;

    public ProductDetailsResponse(Product p) {
        this.setName(p.getName());
        this.setCategory(p.getCategory());
        this.setQuantity(p.getQuantity());
        this.setProductId(p.getProductId());
        List<ImageResponse> images=new ArrayList<>();
        for (Image img:p.getImage()){
            ImageResponse imgR=new ImageResponse();
            imgR.setImageId(img.getImageId());
            imgR.setImage(img.getImage());
            images.add(imgR);
        }
        this.setImages(images);
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }
}
