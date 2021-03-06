package com.asuc.asucmobile.domain.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.asuc.asucmobile.GlobalApplication;
import com.asuc.asucmobile.R;
import com.asuc.asucmobile.domain.adapters.ResourceAdapter;
import com.asuc.asucmobile.domain.models.Resource;
import com.asuc.asucmobile.domain.main.ListOfFavorites;
import com.asuc.asucmobile.domain.main.OpenResourceActivity;
import com.asuc.asucmobile.domain.repository.Repository;
import com.asuc.asucmobile.domain.repository.RepositoryCallback;
import com.asuc.asucmobile.utilities.CustomComparators;
import com.asuc.asucmobile.utilities.NavigationGenerator;
import com.asuc.asucmobile.utilities.SerializableUtilities;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Collections;

import javax.inject.Inject;

public class ResourceFragment extends Fragment {

    @Inject
    Repository<Resource> repository;

    private ListView mResourceList;
    private ProgressBar mProgressBar;
    private LinearLayout mRefreshWrapper;

    private static ResourceAdapter mAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GlobalApplication.getRepositoryComponent().inject(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getContext());
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("opened_resource_screen", bundle);

        View layout = inflater.inflate(R.layout.fragment_resource, container, false);
        Toolbar toolbar = (Toolbar) layout.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        NavigationGenerator.generateToolbarMenuButton(getActivity(), toolbar);
        setHasOptionsMenu(true);
        toolbar.setTitle("Resources");
        ListOfFavorites listOfFavorites =
                (ListOfFavorites) SerializableUtilities.loadSerializedObject(getContext());
        if (listOfFavorites == null) {
            listOfFavorites = new ListOfFavorites();
            SerializableUtilities.saveObject(getContext(), listOfFavorites);
        }
        ImageButton refreshButton = (ImageButton) layout.findViewById(R.id.refresh_button);
        mResourceList = (ListView) layout.findViewById(R.id.resource_list);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progress_bar);
        mRefreshWrapper = (LinearLayout) layout.findViewById(R.id.refresh);
        mAdapter = new ResourceAdapter(getContext());
        mResourceList.setAdapter(mAdapter);
        mResourceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OpenResourceActivity.setResource(mAdapter.getItem(i));
                Intent intent = new Intent(getContext(), OpenResourceActivity.class);
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
    public void onResume() {
        super.onResume();
        NavigationGenerator.closeMenu(getActivity());
    }


//    // Start off list sorted by favorites.
//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        switch (menuItem.getItemId()){
//            case R.id.sortAZ:
//                Collections.sort(mAdapter.getResources(), CustomComparators.FacilityComparators.getSortResourcesByAZ());
//                mAdapter.notifyDataSetChanged();
//                break;
//            case R.id.sortFavorites:
//                Collections.sort(mAdapter.getResources(), CustomComparators.FacilityComparators.getSortByFavoriteResource(getContext()));
//                mAdapter.notifyDataSetChanged();
//                break;
//        }
//        return true;
//    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.resource, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        if (searchMenuItem != null) {
            final SearchView searchView = (SearchView) searchMenuItem.getActionView();
            if (searchView != null) {
                // Setting up aesthetics.
                EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                searchEditText.setTextColor(getResources().getColor(android.R.color.white));
                searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        // Close the keyboard.
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        final Filter filter = mAdapter.getFilter();
                        filter.filter(s);
                        return true;
                    }
                });
            }
        }
    }


    /**
     * refresh() updates the visibility of necessary UI elements and refreshes the resource list
     * from the web.
     */
    private void refresh() {
        mResourceList.setVisibility(View.GONE);
        mRefreshWrapper.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        repository.scanAll(mAdapter.getResources(), new RepositoryCallback<Resource>() {
            @Override
            public void onSuccess() {
                mResourceList.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);

                // sorted by default
                Collections.sort(mAdapter.getResources(), CustomComparators.FacilityComparators.getSortResourcesByAZ());
                Collections.sort(mAdapter.getResources(), CustomComparators.FacilityComparators.getSortByFavoriteResource(getContext()));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                mProgressBar.setVisibility(View.GONE);
                mRefreshWrapper.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Unable to retrieve data, please try again",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void refreshLists() {
        if (ResourceFragment.mAdapter == null)
            return;
        ResourceFragment.mAdapter.notifyDataSetChanged();

    }

}