package com.asuc.asucmobile.domain.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.asuc.asucmobile.R;
import com.asuc.asucmobile.domain.adapters.RouteSelectionAdapter;
import com.asuc.asucmobile.domain.models.Journey;
import com.asuc.asucmobile.domain.models.Trip;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

/**
 * Created by alexthomas on 10/14/17.
 */

public class RouteSelectActivity extends BaseActivity {


    private ListView busList;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_list_times);

        setupToolbar("Routes", true);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("clicked_on_route", bundle);


        ArrayList<Journey> routes = (ArrayList<Journey>) getIntent().getSerializableExtra("routes");

        final RouteSelectionAdapter routeAdapter = new RouteSelectionAdapter(getBaseContext(), routes);
        busList = (ListView) findViewById(R.id.routeList);
        busList.setAdapter(routeAdapter);

        busList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Journey journey = routeAdapter.getItem(position);
                ArrayList<Trip> trips = journey.getTrips();
                Intent busTimes = new Intent(RouteSelectActivity.this, RouteSelected.class);
                busTimes.putExtra("list_of_times", journey);
                startActivity(busTimes);
            }
        });
    }


}


