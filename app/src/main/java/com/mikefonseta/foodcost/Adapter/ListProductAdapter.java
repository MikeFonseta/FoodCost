package com.mikefonseta.foodcost.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mikefonseta.foodcost.Activity.ProductActivity;
import com.mikefonseta.foodcost.Database.Database;
import com.mikefonseta.foodcost.Database.Model.Ingredient;
import com.mikefonseta.foodcost.Database.Model.Product;
import com.mikefonseta.foodcost.R;

import java.text.DecimalFormat;
import java.util.List;

public class ListProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> productList;

    public ListProductAdapter(Context context, int resource, List<Product> itemList) {
        super(context, resource, itemList);
        this.context = context;
        this.productList = itemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_ingredient, null);

        TextView txtNameQuantity = view.findViewById(R.id.textNameQuantityIngrediant);
        TextView txtQuantity = view.findViewById(R.id.textQuantityIngrediant);
        Button btnRemove = view.findViewById(R.id.btnRemoveIngrediant);
        Button btnUpdate = view.findViewById(R.id.btnUpdateIngrediant);

        Product item = productList.get(position);
        Database db = new Database(context);
        float price = db.getProductPrice(item.getId_product());

        DecimalFormat df = new DecimalFormat("#.##");
        txtNameQuantity.setText(item.getName());
        txtQuantity.setText(String.format("%s€", df.format(price)));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("id_product", item.getId_product());
                context.startActivity(intent);
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
            }
        });
        return view;
    }

    private void removeItem(int position) {
        Database db = new Database(context);
        if(productList.get(position) != null && db.RemoveProduct(productList.get(position).getId_product())) {
            productList.remove(position);
        }
        notifyDataSetChanged();
    }

}
