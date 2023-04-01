package com.mikefonseta.foodcost.Adapter;

import android.app.Dialog;
import android.content.Context;
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

import com.mikefonseta.foodcost.Database.Database;
import com.mikefonseta.foodcost.Database.Model.Ingredient;
import com.mikefonseta.foodcost.R;

import java.text.DecimalFormat;
import java.util.List;

public class ListIngredientAdapter extends ArrayAdapter<Ingredient> {

    private Context context;
    private List<Ingredient> ingredientsList;

    public ListIngredientAdapter(Context context, int resource, List<Ingredient> itemList) {
        super(context, resource, itemList);
        this.context = context;
        this.ingredientsList = itemList;
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

        Ingredient item = ingredientsList.get(position);
        DecimalFormat df = new DecimalFormat("#.##");
        txtNameQuantity.setText(item.getName() + " (" +item.getQuantity().toString()+"g)");
        txtQuantity.setText(String.format("%s€", df.format(item.getPrice())));

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

    private void showUpdateDialog(Ingredient ingredientUpdate) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle("AGGIORNA");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_ingredient);

        final EditText nameET = dialog.findViewById(R.id.name);
        final EditText quantityET = dialog.findViewById(R.id.quantity);
        final EditText priceET = dialog.findViewById(R.id.price);

        nameET.setText(ingredientUpdate.getName());
        quantityET.setText(ingredientUpdate.getQuantity().toString());
        priceET.setText(ingredientUpdate.getPrice().toString());

        Button addConfirmBtn = dialog.findViewById(R.id.btnConfirmAdd);
        addConfirmBtn.setText("AGGIORNA");
        addConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                Float quantity = Float.valueOf(quantityET.getText().toString());
                Float price = Float.valueOf(priceET.getText().toString());

                Ingredient ingredient = new Ingredient(ingredientUpdate.getId_ingredient(),name,quantity,price);
                Database db = new Database(context);
                dialog.dismiss();
                if(db.UpdateIngredient(ingredient))
                {
                    ingredientsList.set(ingredientsList.indexOf(ingredientUpdate),ingredient);
                    Toast.makeText(context, "Ingrediente aggiornato", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Errore", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void removeItem(int position) {
        Database db = new Database(context);
        if(ingredientsList.get(position) != null && db.RemoveIngredient(ingredientsList.get(position).getId_ingredient())) {
            ingredientsList.remove(position);
        }
        notifyDataSetChanged();
    }

}
