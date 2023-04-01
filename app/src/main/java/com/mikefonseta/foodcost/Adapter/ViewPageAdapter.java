package com.mikefonseta.foodcost.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mikefonseta.foodcost.Fragments.IngredientsFragment;
import com.mikefonseta.foodcost.Fragments.ProductsFragment;

public class ViewPageAdapter extends FragmentStateAdapter {
    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new ProductsFragment();
        }
        return new IngredientsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
