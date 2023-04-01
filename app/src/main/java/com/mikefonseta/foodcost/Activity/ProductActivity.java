package com.mikefonseta.foodcost.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mikefonseta.foodcost.Adapter.ListIngredientProductAdapter;
import com.mikefonseta.foodcost.Adapter.SpinnerAdapter;
import com.mikefonseta.foodcost.Database.Database;
import com.mikefonseta.foodcost.Database.Model.CompProduct;
import com.mikefonseta.foodcost.Database.Model.Ingredient;
import com.mikefonseta.foodcost.Database.Model.Product;
import com.mikefonseta.foodcost.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private List<CompProduct> ingredientList = new ArrayList<>();
    private int id_product = -1;
    private float price_product = 0;
    private ListIngredientProductAdapter arrayAdapter;
    private TextView priceTxt;
    public float getPrice() {
        return price_product;
    }

    public void setPrice(float price) {
        this.price_product = price;
        DecimalFormat df = new DecimalFormat("#.##");
        priceTxt.setText(String.format("%s€", df.format(price_product)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        id_product = intent.getIntExtra("id_product", -1);

        EditText name = findViewById(R.id.txtName);
        Button addIngredient = findViewById(R.id.add_ingredient_to_product);
        Button saveProduct = findViewById(R.id.btnSaveProduct);
        ListView listView = findViewById(R.id.list_item_ingredient_product);
        priceTxt = findViewById(R.id.txtTotal);

        Database db = new Database(this);
        if(id_product > -1) {
            Product product = db.getProduct(id_product);
            float price = db.getProductPrice(product.getId_product());
            ingredientList = db.getCompProduct(product.getId_product());
            name.setText(product.getName());
            price_product = price;
            DecimalFormat df = new DecimalFormat("#.##");
            priceTxt.setText(String.format("%s€", df.format(price_product)));
            saveProduct.setText("AGGIORNA");
        }

        arrayAdapter = new ListIngredientProductAdapter(this, R.layout.item_ingredient_product, ingredientList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_product > -1) {
                    if (name.getText().toString().length() > 0) {
                        System.out.println(ingredientList.size() + " idProd="+id_product);

                        db.UpdateProduct(ingredientList, name.getText().toString(),id_product);
                    }
                }
                else
                {
                    if (name.getText().toString().length() > 0 && ingredientList.size() > 0) {
                        db.addProduct(ingredientList, name.getText().toString());
                    }
                }
                onBackPressed();
            }
        });

    }

    private void showAddDialog() {
        final Dialog dialog = new Dialog(ProductActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_ingredient_to_product);

        final Spinner name = dialog.findViewById(R.id.ingredientsListChoice);
        final EditText quantity = dialog.findViewById(R.id.quantity);

        Database db = new Database(this);
        List<Ingredient> ingredientList = db.getAllIngredients();

        SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, ingredientList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        name.setAdapter(adapter);

        Button addConfirmBtn = dialog.findViewById(R.id.btnAdd);
        addConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityStr = quantity.getText().toString();
                ProductActivity.this.ingredientList.add(new CompProduct(-1,id_product,Float.parseFloat(quantityStr),adapter.getItem(name.getSelectedItemPosition()).getId_ingredient()));
                arrayAdapter.notifyDataSetChanged();
                price_product += (Float.parseFloat(quantityStr)*adapter.getItem(name.getSelectedItemPosition()).getPrice()/1000);
                DecimalFormat df = new DecimalFormat("#.##");
                priceTxt.setText(String.format("%s€", df.format(price_product)));

                dialog.dismiss();
            }
        });

        dialog.show();
    }


}