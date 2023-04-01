package com.mikefonseta.foodcost.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mikefonseta.foodcost.Activity.ProductActivity;
import com.mikefonseta.foodcost.Database.Database;
import com.mikefonseta.foodcost.Database.Model.CompProduct;
import com.mikefonseta.foodcost.Database.Model.Ingredient;
import com.mikefonseta.foodcost.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ListIngredientProductAdapter extends ArrayAdapter<CompProduct> {

    private Context context;
    private List<CompProduct> ingredientsList;

    public ListIngredientProductAdapter(Context context, int resource, List<CompProduct> itemList) {
        super(context, resource, itemList);
        this.context = context;
        this.ingredientsList = itemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_ingredient_product, null);

        TextView txtNameQuantity = view.findViewById(R.id.textNameQuantityIngrediant);
        TextView txtQuantity = view.findViewById(R.id.textQuantityIngrediant);
        Button btnRemove = view.findViewById(R.id.btnRemoveIngrediant);
        Button btnUpdate = view.findViewById(R.id.btnUpdateIngrediant);

        CompProduct item = ingredientsList.get(position);

        Database db = new Database(context);
        Ingredient ingredient = db.getIngredient(item.getId_ingredient());

        DecimalFormat df = new DecimalFormat("#.##");
        txtNameQuantity.setText(ingredient.getName() + " (" +item.getQuantity()+"g)");
        txtQuantity.setText(String.format("%s€", df.format(item.getQuantity() * (ingredient.getPrice()/1000))));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showUpdateDialog(ingredientsList.get(position));
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

    private void showUpdateDialog(CompProduct ingredientUpdate) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle("AGGIORNA");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_ingredient_to_product);

        final Spinner name = dialog.findViewById(R.id.ingredientsListChoice);
        EditText quantity = dialog.findViewById(R.id.quantity);

        Database db = new Database(context);
        Ingredient ingredient = db.getIngredient(ingredientUpdate.getId_ingredient());

        List<String> ingredientName = new ArrayList<>();
        ingredientName.add(ingredient.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ingredientName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        name.setAdapter(adapter);
        name.setEnabled(false);

        DecimalFormat df = new DecimalFormat("#.##");
        quantity.setText(String.format("%s", df.format(ingredientUpdate.getQuantity())));

        float oldQuantity = ingredientUpdate.getQuantity();

        Button addConfirmBtn = dialog.findViewById(R.id.btnAdd);
        addConfirmBtn.setText("AGGIORNA");
        addConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientUpdate.setQuantity(Float.parseFloat(quantity.getText().toString()));

                Database db = new Database(context);
                dialog.dismiss();

                if(ingredientUpdate.getId_product() > -1)
                {
                    if(db.UpdateCompProduct(ingredientUpdate))
                    {
                        float priceBefore = ((ProductActivity) context).getPrice();
                        float priceUpdate = priceBefore - ((oldQuantity * ingredient.getPrice())/1000) + ((ingredientUpdate.getQuantity() * ingredient.getPrice())/1000);
                        ((ProductActivity) context).setPrice(priceUpdate);
                        UpdatePrice(priceUpdate);

                        Toast.makeText(context, "Ingrediente aggiornato", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(context, "Errore", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    float priceBefore = ((ProductActivity) context).getPrice();
                    float priceUpdate = priceBefore - ((oldQuantity * ingredient.getPrice())/1000) + ((ingredientUpdate.getQuantity() * ingredient.getPrice())/1000);
                    ((ProductActivity) context).setPrice(priceUpdate);
                    UpdatePrice(priceUpdate);
                }

                notifyDataSetChanged();
            }
        });

        dialog.show();
    }

    private void removeItem(int position) {
        Database db = new Database(context);
        Ingredient ingredient = db.getIngredient(ingredientsList.get(position).getId_ingredient());
        float priceBefore = ((ProductActivity) context).getPrice();
        float priceUpdate = priceBefore - ((ingredientsList.get(position).getQuantity() * ingredient.getPrice())/1000);
        ((ProductActivity) context).setPrice(priceUpdate);
        if(ingredientsList.get(position) != null && ingredientsList.get(position).getId_product() > -1) {
            if(db.RemoveCompProduct(ingredientsList.get(position)))
            {
                UpdatePrice(priceUpdate);
                ingredientsList.remove(position);
            }
        }
        else if(ingredientsList.get(position).getId_product() == -1)
        {
            UpdatePrice(priceUpdate);
            ingredientsList.remove(position);
        }
        notifyDataSetChanged();
    }

    private void UpdatePrice(float price)
    {
        ((ProductActivity) context).setPrice(price);
    }

}
