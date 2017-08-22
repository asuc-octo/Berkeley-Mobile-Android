package com.asuc.asucmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asuc.asucmobile.R;
import com.asuc.asucmobile.fragments.MenuFragment;
import com.asuc.asucmobile.main.ListOfFavorites;
import com.asuc.asucmobile.models.FoodItem;
import com.asuc.asucmobile.utilities.SerializableUtilities;

import java.util.ArrayList;

public class FoodAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FoodItem> foodItems;

    public FoodAdapter(Context context, ArrayList<FoodItem> foodItems) {
        this.context = context;
        this.foodItems = foodItems;
    }


    @Override
    public int getCount() {
        return foodItems.size();
    }

    @Override
    public Object getItem(int i) {
        return foodItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final FoodItem foodItem = foodItems.get(i);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.food_row, parent, false);
        }

        final TextView foodName = (TextView) convertView.findViewById(R.id.food_name);
        TextView foodType = (TextView) convertView.findViewById(R.id.food_type);
        TextView foodCalories = (TextView) convertView.findViewById(R.id.calories);

        foodName.setText(foodItem.getName());

        if (foodItem.getFoodType() != null && !foodItem.getFoodType().equals("None") && !foodItem.getFoodType().equals("null")) {
            foodType.setVisibility(View.VISIBLE);
            foodType.setText(foodItem.getFoodType().toUpperCase());
        } else {
            foodType.setVisibility(View.GONE);
        }

        if (!foodItem.getCost().equals("$NaN")) {
            foodCalories.setText(foodItem.getCost());
        } else if (foodItem.getCalories().equals("null")) {
            foodCalories.setVisibility(View.GONE);
        } else {
            foodCalories.setVisibility(View.VISIBLE);
            foodCalories.setText(String.format("%s cal", foodItem.getCalories()));
        }
        final ListOfFavorites listOfFavorites = (ListOfFavorites) SerializableUtilities.loadSerializedObject(context);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.favorite);

        if (listOfFavorites != null && listOfFavorites.contains(foodItem.getName())) {
            imageView.setImageResource(R.drawable.post_favorite);
        } else {
            imageView.setImageResource(R.drawable.pre_favorite);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listOfFavorites != null && listOfFavorites.contains(foodItem.getName())) {
                    listOfFavorites.remove(foodItem.getName());
                    SerializableUtilities.saveObject(context, listOfFavorites);
                    imageView.setImageResource(R.drawable.pre_favorite);
                } else if (listOfFavorites != null) {
                    listOfFavorites.add(foodItem.getName());
                    SerializableUtilities.saveObject(context, listOfFavorites);
                    imageView.setImageResource(R.drawable.post_favorite);
                }

                MenuFragment.refreshLists();
            }
        });

        return convertView;
    }

}
