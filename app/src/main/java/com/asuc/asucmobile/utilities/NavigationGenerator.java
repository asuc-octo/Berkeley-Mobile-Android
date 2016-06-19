package com.asuc.asucmobile.utilities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.asuc.asucmobile.R;
import com.asuc.asucmobile.adapters.MainMenuAdapter;
import com.asuc.asucmobile.fragments.BlankFragment;
import com.asuc.asucmobile.fragments.DiningHallFragment;
import com.asuc.asucmobile.fragments.GymFragment;
import com.asuc.asucmobile.fragments.LibraryFragment;
import com.asuc.asucmobile.fragments.SAOFragment;
import com.asuc.asucmobile.fragments.StartStopSelectFragment;
import com.asuc.asucmobile.main.MainActivity;
import com.asuc.asucmobile.models.Category;

public class NavigationGenerator {

    public static final Category[] SECTIONS = new Category[] {
            new Category(R.drawable.beartransit, "BearTransit") {
                @Override
                public void loadFragment(FragmentManager fragmentManager) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new StartStopSelectFragment())
                            .commit();
                }
            },
            new Category(R.drawable.dining_hall, "Dining Halls") {
                @Override
                public void loadFragment(FragmentManager fragmentManager) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new DiningHallFragment())
                            .commit();
                }
            },
            new Category(R.drawable.library, "Libraries") {
                @Override
                public void loadFragment(FragmentManager fragmentManager) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new LibraryFragment())
                            .commit();
                }
            },
            new Category(R.drawable.gym, "Gyms") {
                @Override
                public void loadFragment(FragmentManager fragmentManager) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new GymFragment())
                            .commit();
                }
            },
            new Category(R.drawable.library, "Student Advocates Office") {
                @Override
                public void loadFragment(FragmentManager fragmentManager) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new SAOFragment())
                            .commit();
                }
            }
    };

    private static DrawerLayout drawerLayout;
    private static AppCompatActivity activity;
    private static MainMenuAdapter adapter;

    public static void generateMenu(final AppCompatActivity activity) {
        if (NavigationGenerator.activity != null && NavigationGenerator.activity == activity) {
            return;
        }

        // Set the adapter for the list view
        NavigationGenerator.activity = activity;
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) activity.findViewById(R.id.drawer_list);

        adapter = new MainMenuAdapter(activity, SECTIONS);
        if (drawerList != null) {
            drawerList.setAdapter(adapter);
            drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (activity.findViewById(R.id.content_frame) == null) {
                                Intent i = new Intent(activity, MainActivity.class);
                                i.putExtra("page", position);
                                activity.startActivity(i);
                                activity.finish();
                            } else {
                                adapter.getItem(position).loadFragment(activity.getSupportFragmentManager());
                            }
                        }
                    }, 0);
                }
            });
        }
    }

    public static void loadSection(int index) {
        if (index == -1) {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new BlankFragment())
                    .commit();
        } else {
            adapter.getItem(index).loadFragment(activity.getSupportFragmentManager());
        }
    }

    /**
     * For Fragments with Toolbars
     */
    public static void generateToolbarMenuButton(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.navi);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });
    }

    /**
     * For Fragments without Toolbars
     */
    public static void generateToolbarMenuButton(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });
    }

    public static void openMenu() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    public static void closeMenu() {
        if (drawerLayout != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }, 0);
        }
    }

}
