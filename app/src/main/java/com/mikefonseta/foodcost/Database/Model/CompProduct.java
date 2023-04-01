package com.mikefonseta.foodcost.Database.Model;

public class CompProduct {

    private int id_comp;
    private int id_product;
    private float quantity;
    private int id_ingredient;

    public CompProduct(int id_comp, int id_product, float quantity, int id_ingredient) {
        this.id_comp = id_comp;
        this.id_product = id_product;
        this.quantity = quantity;
        this.id_ingredient = id_ingredient;
    }

    public int getId_comp() {
        return id_comp;
    }

    public void setId_comp(int id_comp) {
        this.id_comp = id_comp;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public int getId_ingredient() {
        return id_ingredient;
    }

    public void setId_ingredient(int id_ingredient) {
        this.id_ingredient = id_ingredient;
    }

    @Override
    public String toString() {
        return "CompProduct{" +
                "id_product=" + id_product +
                ", quantity=" + quantity +
                ", id_ingredient=" + id_ingredient +
                '}';
    }
}
