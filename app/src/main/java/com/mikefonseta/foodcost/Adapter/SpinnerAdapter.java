package com.mikefonseta.foodcost.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mikefonseta.foodcost.Database.Model.Ingredient;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Ingredient> {

    private Context context;
    private List<Ingredient> values;

    public SpinnerAdapter(Context context, int textViewResourceId, List<Ingredient> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Ingredient getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);

        label.setText(String.format("%s %s € / %s kg", values.get(position).getName(), values.get(position).getPrice(), values.get(position).getQuantity()/1000));

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());

        return label;
    }
}