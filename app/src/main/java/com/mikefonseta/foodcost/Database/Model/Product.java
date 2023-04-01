package com.mikefonseta.foodcost.Database.Model;

public class Product {

    private int id_product;
    private String name;

    public Product(int id_product, String name) {
        this.id_product = id_product;
        this.name = name;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
