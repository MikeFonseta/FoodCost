package com.mikefonseta.foodcost.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mikefonseta.foodcost.Database.Model.CompProduct;
import com.mikefonseta.foodcost.Database.Model.Ingredient;
import com.mikefonseta.foodcost.Database.Model.Product;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context) {
        super(context, "foodcost.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Ingredient (\n" +
                "\tid_ingredient INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tname TEXT NOT NULL,\n" +
                "\tquantity DECIMAL(10,2) NOT NULL,\n" +
                "\tprice DECIMAL(10,2) NOT NULL\n);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Product (\n" +
                "\tid_product INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tname TEXT NOT NULL);");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS CompProduct (\n" +
                "\tid_comp INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tid_product INTEGER NOT NULL,\n" +
                "\tquantity DECIMAL(10,2) NOT NULL,\n" +
                "\tid_ingredient INTEGER NOT NULL,\n" +
                "\tCONSTRAINT fkCOProduct FOREIGN KEY(id_product) REFERENCES Product(id_product) ON DELETE CASCADE,\n" +
                "\tCONSTRAINT fkCOIngredient FOREIGN KEY(id_ingredient) REFERENCES Product(id_ingredient) ON DELETE CASCADE\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addIngredient(Ingredient ingredient)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name",ingredient.getName());
        contentValues.put("quantity",ingredient.getQuantity());
        contentValues.put("price",ingredient.getPrice());

        long insert = db.insert("Ingredient", null, contentValues);

        return insert != -1;
    }

    public boolean UpdateIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name",ingredient.getName());
        contentValues.put("quantity",ingredient.getQuantity());
        contentValues.put("price",ingredient.getPrice());

        long update = db.update("Ingredient",contentValues, "id_ingredient=?" , new String[]{String.valueOf(ingredient.getId_ingredient())});

        return update != -1;
    }
    public boolean RemoveIngredient(int id_ingredient)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long delete = db.delete("Ingredient", "id_ingredient=?" , new String[]{String.valueOf(id_ingredient)});

        return delete != -1;
    }

    public List<Ingredient> getAllIngredients()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Ingredient",null,null,null,null,null,null);

        List<Ingredient> ingredientList = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do {
                ingredientList.add(new Ingredient(cursor.getInt(0),cursor.getString(1),cursor.getFloat(2),cursor.getFloat(3)));
            }while(cursor.moveToNext());
        }

        return ingredientList;
    }

    public Ingredient getIngredient(int id_ingredient) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Ingredient",null,"id_ingredient=?",new String[]{String.valueOf(id_ingredient)},null,null,null);

        Ingredient ingredient = null;

        if(cursor.moveToFirst())
        {
            ingredient = new Ingredient(cursor.getInt(0),cursor.getString(1),cursor.getFloat(2),cursor.getFloat(3));
        }

        return ingredient;
    }

    public List<CompProduct> getCompProduct(int id_product) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("CompProduct",null,"id_product=?",new String[]{String.valueOf(id_product)},null,null,null);

        List<CompProduct> compProduct = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do {
                compProduct.add(new CompProduct(cursor.getInt(0),cursor.getInt(1),cursor.getFloat(2),cursor.getInt(3)));
            }while(cursor.moveToNext());
        }

        return compProduct;
    }

    public boolean addCompProduct(CompProduct compProduct)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id_product",compProduct.getId_product());
        contentValues.put("quantity",compProduct.getQuantity());
        contentValues.put("id_ingredient",compProduct.getId_ingredient());

        long insert = db.insert("CompProduct", null, contentValues);

        return insert != -1;
    }

    public boolean UpdateCompProduct(CompProduct ingredientUpdate) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        long update = 0;

        if(ingredientUpdate.getId_comp() > -1) {
            contentValues.put("id_product", ingredientUpdate.getId_product());
            contentValues.put("quantity", ingredientUpdate.getQuantity());
            contentValues.put("id_ingredient", ingredientUpdate.getId_ingredient());

            update = db.update("CompProduct", contentValues, "id_comp=?", new String[]{String.valueOf(ingredientUpdate.getId_comp())});
        }
        else
        {
            addCompProduct(ingredientUpdate);
        }
        return update != -1;
    }

    public boolean RemoveCompProduct(CompProduct compProduct) {
        SQLiteDatabase db = this.getWritableDatabase();
        long delete = db.delete("CompProduct", "id_comp=? " , new String[]{String.valueOf(compProduct.getId_comp())});

        return delete != -1;
    }

    public boolean addProduct(List<CompProduct> ingredientList, String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name",name);

        long insert = db.insert("Product", null, contentValues);

        String selectQuery = "SELECT * FROM Product WHERE id_product = (SELECT MAX(id_product) FROM Product)";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        for (CompProduct compProduct : ingredientList) {
            compProduct.setId_product(cursor.getInt(0));
            addCompProduct(compProduct);
        }

        cursor.close();

        return insert != -1;
    }

    public float getProductPrice(int id_product) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT CAST(SUM(CAST(cp.quantity*i.price AS FLOAT))/1000 AS FLOAT) AS total FROM Product AS p INNER JOIN CompProduct AS cp ON p.id_product = cp.id_product INNER JOIN Ingredient AS i ON cp.id_ingredient = i.id_ingredient WHERE p.id_product="+id_product;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        return cursor.getFloat(0);
    }

    public Product getProduct(int id_product) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Product",null,"id_product=?",new String[]{String.valueOf(id_product)},null,null,null);

        Product product = null;

        if(cursor.moveToFirst())
        {
            product = new Product(cursor.getInt(0),cursor.getString(1));
        }

        return product;
    }

    public List<Product> getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Product",null,null,null,null,null,null);

        List<Product> productList = new ArrayList<>();

        if(cursor.moveToFirst())
        {
            do {
                productList.add(new Product(cursor.getInt(0),cursor.getString(1)));
            }while(cursor.moveToNext());
        }

        return productList;
    }

    public boolean UpdateProduct(List<CompProduct> ingredientList, String name, int id_product) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (CompProduct compProduct : ingredientList) {
            System.out.print(compProduct.toString() + " ");
            compProduct.setId_product(id_product);
            UpdateCompProduct(compProduct);
        }

        contentValues.put("name",name);
        long update = db.update("Product",contentValues, "id_product=?" , new String[]{String.valueOf(id_product)});

        return update != -1;
    }

    public boolean RemoveProduct(int id_product)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        long delete = db.delete("Product", "id_product=?" , new String[]{String.valueOf(id_product)});

        return delete != -1;
    }


}
