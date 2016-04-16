package com.asuc.asucmobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.asuc.asucmobile.R;
import com.asuc.asucmobile.adapters.GymAdapter;
import com.asuc.asucmobile.controllers.GymController;
import com.asuc.asucmobile.main.OpenGymActivity;
import com.asuc.asucmobile.models.Gym;
import com.asuc.asucmobile.utilities.Callback;
import com.asuc.asucmobile.utilities.NavigationGenerator;
import com.flurry.android.FlurryAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GymFragment extends Fragment {

    private ListView mGymList;
    private ProgressBar mProgressBar;
    private LinearLayout mRefreshWrapper;

    private GymAdapter mAdapter;

    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FlurryAgent.onStartSession(getContext(), "4VPTT49FCCKH7Z2NVQ26");
        View layout = inflater.inflate(R.layout.fragment_gym, container, false);

        Toolbar toolbar = (Toolbar) layout.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        NavigationGenerator.generateToolbarMenuButton(toolbar);
        toolbar.setTitle("Gyms");

        ImageButton refreshButton = (ImageButton) layout.findViewById(R.id.refresh_button);

        mGymList = (ListView) layout.findViewById(R.id.gym_list);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progress_bar);
        mRefreshWrapper = (LinearLayout) layout.findViewById(R.id.refresh);

        mAdapter = new GymAdapter(getContext());
        mGymList.setAdapter(mAdapter);

        mGymList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GymController controller = ((GymController) GymController.getInstance(getContext()));
                controller.setCurrentGym(mAdapter.getItem(i));
                Intent intent = new Intent(getActivity(), OpenGymActivity.class);

                //Flurry log for tapping Gyms.
                Map<String, String> gymParams = new HashMap<>();
                gymParams.put("Hall", mAdapter.getItem(i).getName());
                FlurryAgent.logEvent("Taps Gym Hours", gymParams);

                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        refresh();

        return layout;
    }

    @Override
    public void onStop() {
        super.onStop();

        FlurryAgent.onEndSession(getContext());
    }

    /**
     * refresh() updates the visibility of necessary UI elements and refreshes the gym list
     * from the web.
     */
    private void refresh() {
        mGymList.setVisibility(View.GONE);
        mRefreshWrapper.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        GymController.getInstance(getContext()).refreshInBackground(new Callback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onDataRetrieved(Object data) {
                mGymList.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);

                mAdapter.setList((ArrayList<Gym>) data);
            }

            @Override
            public void onRetrievalFailed() {
                mProgressBar.setVisibility(View.GONE);
                mRefreshWrapper.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Unable to retrieve data, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

}