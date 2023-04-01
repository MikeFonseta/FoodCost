package com.mikefonseta.foodcost.Database.Model;

public class Ingredient {

    private int id_ingredient;
    private String name;
    private Float quantity;
    private  Float price;

    public Ingredient(int id, String name, Float quantity, Float price) {
        this.id_ingredient = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId_ingredient() {
        return id_ingredient;
    }

    public void setId_ingredient(int id_ingredient) {
        this.id_ingredient = id_ingredient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
