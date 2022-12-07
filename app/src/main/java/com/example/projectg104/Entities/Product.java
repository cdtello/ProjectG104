package com.example.projectg104.Entities;

import java.util.UUID;

public class Product {
    private String id;
    private byte[] image;
    private String name;
    private String description;
    private int price;

    public Product(String id, String name, String description, int price, byte[] image) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(String name, String description, int price, byte[] image) {
        this.id = UUID.randomUUID().toString();
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Product(String name, String description, int price) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getId(){return id;}

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
