package com.mikefonseta.foodcost.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikefonseta.foodcost.Adapter.ListIngredientAdapter;
import com.mikefonseta.foodcost.Database.Database;
import com.mikefonseta.foodcost.Database.Model.Ingredient;
import com.mikefonseta.foodcost.R;

import java.util.List;

public class IngredientsFragment extends Fragment {

    private List<Ingredient> ingredientList;
    private ListIngredientAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        FloatingActionButton button = view.findViewById(R.id.add_ingredient);
        ListView listView = view.findViewById(R.id.list_item_ingredient);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        Database db = new Database(getActivity());
        ingredientList = db.getAllIngredients();

        arrayAdapter = new ListIngredientAdapter(getActivity(), R.layout.item_ingredient, ingredientList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        return view;
    }



    private void showAddDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("AGGIUNGI");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_ingredient);

        final EditText nameET = dialog.findViewById(R.id.name);
        final EditText quantityET = dialog.findViewById(R.id.quantity);
        final EditText priceET = dialog.findViewById(R.id.price);

        Button addConfirmBtn = dialog.findViewById(R.id.btnConfirmAdd);
        addConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                Float quantity = Float.valueOf(quantityET.getText().toString());
                Float price = Float.valueOf(priceET.getText().toString());

                Ingredient ingredient = new Ingredient(-1,name,quantity,price);
                Database db = new Database(getActivity());
                dialog.dismiss();
                if(db.addIngredient(ingredient))
                {
                    ingredientList.add(ingredient);
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Ingrediente aggiunto", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "Errore", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}