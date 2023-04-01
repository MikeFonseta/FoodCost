package com.mikefonseta.foodcost.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikefonseta.foodcost.Activity.ProductActivity;
import com.mikefonseta.foodcost.Adapter.ListIngredientAdapter;
import com.mikefonseta.foodcost.Adapter.ListProductAdapter;
import com.mikefonseta.foodcost.Database.Database;
import com.mikefonseta.foodcost.Database.Model.Product;
import com.mikefonseta.foodcost.R;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    List<Product> productList = new ArrayList<>();
    private ListProductAdapter arrayAdapter;
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        FloatingActionButton addProduct = view.findViewById(R.id.add_product);
        listView = view.findViewById(R.id.list_item_product);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ProductActivity.class);
                intent.putExtra("id_product", -1);
                getActivity().startActivity(intent);
            }
        });

        Database db = new Database(getActivity());
        productList = db.getAllProducts();

        arrayAdapter = new ListProductAdapter(getActivity(), R.layout.item_ingredient, productList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Database db = new Database(getActivity());
        productList = db.getAllProducts();

        arrayAdapter = new ListProductAdapter(getActivity(), R.layout.item_ingredient, productList);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }
}