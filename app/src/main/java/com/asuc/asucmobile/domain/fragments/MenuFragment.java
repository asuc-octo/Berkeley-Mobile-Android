package com.asuc.asucmobile.domain.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.view.ViewCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.asuc.asucmobile.R;

import com.asuc.asucmobile.domain.adapters.FoodAdapter;
import com.asuc.asucmobile.domain.adapters.FoodPlaceAdapter;
import com.asuc.asucmobile.domain.main.LegendPopUpActivity;
import com.asuc.asucmobile.domain.main.OpenCafeActivity;
import com.asuc.asucmobile.domain.main.OpenDiningHallActivity;
import com.asuc.asucmobile.domain.models.Cafe;
import com.asuc.asucmobile.domain.models.DiningHall;
import com.asuc.asucmobile.domain.models.FoodItem;
import com.asuc.asucmobile.domain.models.FoodPlace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by rustie on 10/11/17.
 */



public class MenuFragment extends Fragment {

    public enum FoodType {
        DiningHall,
        Cafe
    }

    public static final String TAG = "MenuFragment";

    private static final SimpleDateFormat HOURS_FORMAT =
            new SimpleDateFormat("h:mm a", Locale.ENGLISH);
    private static final String LATE_NIGHT_OPEN = "10:00 PM";
    private static final String LATE_NIGHT_CLOSE = "2:00 AM";

    private static ArrayList<FoodAdapter> adapters = new ArrayList<>();

    // where we at
    private static FoodPlace foodPlace;

    // which type of menu are we dealing with
    private static FoodType foodType;

    // maintain references to ui elements
    private ListView foodMenu;
    private TextView emptyListView;
    private View header;
    private TextView headerHours;
    private View v;

    public MenuFragment() {

    }

    public void setFoodType(FoodType type) {
        foodType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize view
        v = inflater.inflate(R.layout.fragment_menu, container, false);

        // Get references to views.
        foodMenu = (ListView) v.findViewById(R.id.food_menu);
        ViewCompat.setNestedScrollingEnabled(foodMenu,true);

        emptyListView = (TextView) v.findViewById(R.id.empty_list);
        header = getActivity().getLayoutInflater().inflate(R.layout.header_image, foodMenu, false);
        headerHours = (TextView) header.findViewById(R.id.header_text);

        ImageView legendIcon = (ImageView)header.findViewById(R.id.legend_info);
        legendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LegendPopUpActivity newDialog = new LegendPopUpActivity();
                newDialog.show(getFragmentManager(), "dialog");
            }
        });
        //for clicking on a drawable in the TextView
//        headerHours.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_LEFT = 0;
//                final int DRAWABLE_TOP = 1;
//                final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;
//                Log.d("touched:", "true");
//
//                if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                    if(event.getRawX() >= (headerHours.getRight() - headerHours.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        LegendPopUpActivity newDialog = new LegendPopUpActivity();
//                        newDialog.show(getFragmentManager(), "dialog");
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

        if (emptyListView == null) {
            Log.d(TAG, "EMPTY EMPTY");
        }

        // Add the header to the list.
        foodMenu.addHeaderView(header);


        FoodPlace foodPlace = null;
        if (foodType == FoodType.DiningHall) {
            foodPlace = ((OpenDiningHallActivity) getActivity()).getDiningHall();
        } else if (foodType == FoodType.Cafe) {
            foodPlace = ((OpenCafeActivity) getActivity()).getCafe();
        }

        if (foodPlace == null) {
            getActivity().finish();
            return v;
        }

        if (foodType == FoodType.DiningHall) {
            populateDiningHall((DiningHall) foodPlace);
        } else if (foodType == FoodType.Cafe) {
            populateCafe((Cafe) foodPlace);
        }

        return v;
    }

    /**
     * Populates the View with the current Cafe
     * @param cafe
     */
    private void populateCafe(Cafe cafe) {
        // Populate menu views.
        String whichMenu = getArguments().getString("whichMenu");
        String opening;
        String closing;
        try {
            ArrayList<FoodItem> foodItems;
            boolean isOpen;
            MenuFragment.foodPlace = cafe;
            if (whichMenu != null) {
                switch (whichMenu) {
                    case "Breakfast":
                        foodItems = cafe.getBreakfastMenu();
                        opening = HOURS_FORMAT.format(cafe.getBreakfastOpening());
                        closing = HOURS_FORMAT.format(cafe.getBreakfastClosing());
                        isOpen = cafe.isBreakfastOpen();
                        break;
                    case "Lunch/Dinner":
                        foodItems = cafe.getLunchDinnerMenu();
                        opening = HOURS_FORMAT.format(cafe.getLunchDinnerOpening());
                        closing = HOURS_FORMAT.format(cafe.getLunchDinnerClosing());
                        isOpen = cafe.isLunchDinnerOpen();
                        break;
                    default:
                        foodItems = cafe.getLunchDinnerMenu();
                        opening = HOURS_FORMAT.format(cafe.getLunchDinnerOpening());
                        closing = HOURS_FORMAT.format(cafe.getLunchDinnerClosing());
                        isOpen = cafe.isLunchDinnerOpen();
                }

                if (foodItems == null || foodItems.size() == 0) {
                    emptyListView.setText(String.format("No %s Today!", whichMenu));
                    foodMenu.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);
                } else {
                    String headerString = "Hours:  " + opening + " to " + closing + "  ";
                    SpannableString spannableHeader;
                    if (isOpen) {
                        spannableHeader = new SpannableString(headerString + "OPEN");
                        spannableHeader.setSpan(
                                new ForegroundColorSpan(Color.rgb(153, 204, 0)),
                                headerString.length(),
                                headerString.length() + 4,
                                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
                        );
                    } else {
                        spannableHeader = new SpannableString(headerString + "CLOSED");
                        spannableHeader.setSpan(
                                new ForegroundColorSpan(Color.rgb(255, 68, 68)),
                                headerString.length(),
                                headerString.length() + 6,
                                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
                        );
                    }
                    headerHours.setText(spannableHeader);
                    FoodAdapter adapter = new FoodAdapter(getActivity(), foodItems, FoodPlaceAdapter.FoodType.Cafe);
                    MenuFragment.adapters.add(adapter);
                    foodMenu.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Catch a null exception, meaning that there is no menu for this time slot.
            emptyListView.setText(String.format("No %s Today!", whichMenu));
            foodMenu.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Populates the View with the current Dining Hall
     * @param diningHall
     */
    private void populateDiningHall(DiningHall diningHall) {
        // Populate menu views.
        String whichMenu = getArguments().getString("whichMenu");
        String opening;
        String closing;
        try {
            ArrayList<FoodItem> foodItems;
            boolean isOpen;
            MenuFragment.foodPlace = diningHall;
            if (whichMenu != null) {
                switch (whichMenu) {
                    case "Breakfast":
                        foodItems = diningHall.getBreakfastMenu();

                        opening = HOURS_FORMAT.format(diningHall.getBreakfastOpen());
                        closing = HOURS_FORMAT.format(diningHall.getBreakfastClose());
                        isOpen = diningHall.isBreakfastOpen();
                        break;
                    case "Lunch":
                        foodItems = diningHall.getLunchMenu();
                        opening = HOURS_FORMAT.format(diningHall.getLunchOpen());
                        closing = HOURS_FORMAT.format(diningHall.getLunchClose());
                        isOpen = diningHall.isLunchOpen();
                        break;
                    case "LimitedL":
                        if (diningHall.limitedLunchToday()) {
                            foodItems = diningHall.getLimitedLunchMenu();
                        } else {
                            foodItems = null;
                        }
                        if (diningHall.getLimitedLunchOpen() != null) {
                            opening = HOURS_FORMAT.format(diningHall.getLimitedLunchOpen());
                        } else {
                            opening = LATE_NIGHT_OPEN;
                        }
                        if (diningHall.getLimitedLunchClose() != null) {
                            closing = HOURS_FORMAT.format(diningHall.getLimitedLunchClose());
                        } else {
                            closing = LATE_NIGHT_CLOSE;
                        }
                        isOpen = diningHall.isLimitedLunchOpen();
                        break;
                    case "LimitedD":
                        if (diningHall.limitedDinnerToday()) {
                            foodItems = diningHall.getLimitedDinnerMenu();
                        } else {
                            foodItems = null;
                        }
                        if (diningHall.getLimitedDinnerOpen() != null) {
                            opening = HOURS_FORMAT.format(diningHall.getLimitedDinnerOpen());
                        } else {
                            opening = LATE_NIGHT_OPEN;
                        }
                        if (diningHall.getLimitedDinnerClose() != null) {
                            closing = HOURS_FORMAT.format(diningHall.getLimitedDinnerClose());
                        } else {
                            closing = LATE_NIGHT_CLOSE;
                        }
                        isOpen = diningHall.isLimitedDinnerOpen();
                        break;
                    default:
                        foodItems = diningHall.getDinnerMenu();
                        opening = HOURS_FORMAT.format(diningHall.getDinnerOpen());
                        closing = HOURS_FORMAT.format(diningHall.getDinnerClose());
                        isOpen = diningHall.isDinnerOpen();
                }
                if (foodItems == null || foodItems.size() == 0) {
                    if(whichMenu.equals("LimitedL") || whichMenu.equals("LimitedD"))
                        emptyListView.setText(String.format("No %s Today!", "Limited"));
                    else
                        emptyListView.setText(String.format("No %s Today!", whichMenu));
                    foodMenu.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);
                } else {
                    String headerString = "Hours:  " + opening + " to " + closing + "  ";
                    SpannableString spannableHeader;
                    if (isOpen) {
                        spannableHeader = new SpannableString(headerString + "OPEN");
                        spannableHeader.setSpan(
                                new ForegroundColorSpan(Color.rgb(153, 204, 0)),
                                headerString.length(),
                                headerString.length() + 4,
                                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
                        );
                    } else {
                        spannableHeader = new SpannableString(headerString + "CLOSED");
                        spannableHeader.setSpan(
                                new ForegroundColorSpan(Color.rgb(255, 68, 68)),
                                headerString.length(),
                                headerString.length() + 6,
                                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
                        );
                    }
                    headerHours.setText(spannableHeader);

//                    Collections.sort(foodItems, CustomComparators.FacilityComparators.getFoodSortByAZ());
//                    Collections.sort(foodItems CustomComparators.FacilityComparators.getFoodSortByFavorite());
//

                    FoodAdapter adapter = new FoodAdapter(getActivity(), foodItems, FoodPlaceAdapter.FoodType.DiningHall);
                    MenuFragment.adapters.add(adapter);
                    foodMenu.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Catch a null exception, meaning that there is no menu for this time slot.
            emptyListView.setText(String.format("No %s Today!", whichMenu));
            foodMenu.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
        }
    }



    public static void refreshLists() {
        if (MenuFragment.adapters == null) {
            return;
        }
        for (FoodAdapter adapter : MenuFragment.adapters) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Returns the DiningHall if we're looking at a DiningHall Menu, otherwise null
     * @return
     */
    public static DiningHall getDiningHall() {
        if (foodType == FoodType.DiningHall) {
            return (DiningHall) foodPlace;
        } else {
            return null;
        }
    }

    /**
     * Returns the Cafe if we're looking at a Cafe Menu, otherwise null
     * @return
     */
    public static Cafe getCafe() {
        if (foodType == FoodType.Cafe) {
            return (Cafe) foodPlace;
        } else {
            return null;
        }
    }

}
