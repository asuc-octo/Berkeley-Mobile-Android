package com.asuc.asucmobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asuc.asucmobile.R;
import com.asuc.asucmobile.controllers.GymClassController;
import com.asuc.asucmobile.models.GymClass;
import com.asuc.asucmobile.utilities.Callback;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import noman.weekcalendar.WeekCalendar;
import noman.weekcalendar.listener.OnDateClickListener;

public class GymClassFragment extends Fragment {
    public static final String TAG = "GymClassFragment";

    private ArrayList<Integer> filter;
    private ArrayList<GymClass> mClasses;
    private HashMap<Button, Boolean> clickTracker;
    private Button allAround, cardio, mind, core, dance, strength, aqua;
    private TableLayout table;
    private WeekCalendar calendar;
    private View layout;
    private int dayOfMonth;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mClasses = new ArrayList<>();
        filter = new ArrayList<>();
        clickTracker = new HashMap<>();
        dayOfMonth = new DateTime().getDayOfMonth();

        layout = inflater.inflate(R.layout.fragment_class, container, false);
        calendar = (WeekCalendar) layout.findViewById(R.id.weekCalendar);
        calendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {
                dayOfMonth = dateTime.getDayOfMonth();
                initClassTable(inflater);
                Log.d("LOL", dayOfMonth+"");
            }
        });
        createDummyData();

        initButtons(inflater);
        initClassTable(inflater);

        // Work with Rustie and figure out pulling json
        // refresh();
        return layout;
    }
    private void initButtons(final LayoutInflater inflater) {
        allAround = (Button) layout.findViewById(R.id.all_around);
        allAround.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (clickTracker.get(allAround)) {
                        filter.remove((Integer) GymClass.ALL_AROUND);
                        allAround.setBackgroundResource(R.drawable.opaque_rounded_shape_1);
                        allAround.setTextColor(getResources().getColor(R.color.card_background));
                        clickTracker.put(allAround, false);
                    } else {
                        filter.add(GymClass.ALL_AROUND);
                        allAround.setBackgroundResource(R.drawable.transparent_rounded_shape_1);
                        allAround.setTextColor(getResources().getColor(R.color.excercise_color_1));
                        clickTracker.put(allAround, true);
                    }
                }
                initClassTable(inflater);
                return false;
            }
        });

        cardio = (Button) layout.findViewById(R.id.cardio);
        cardio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (clickTracker.get(cardio)) {
                        filter.remove((Integer) GymClass.CARDIO);
                        cardio.setBackgroundResource(R.drawable.opaque_rounded_shape_2);
                        cardio.setTextColor(getResources().getColor(R.color.card_background));
                        clickTracker.put(cardio, false);
                    } else {
                        filter.add(GymClass.CARDIO);
                        cardio.setBackgroundResource(R.drawable.transparent_rounded_shape_2);
                        cardio.setTextColor(getResources().getColor(R.color.excercise_color_2));
                        clickTracker.put(cardio, true);
                    }
                }
                initClassTable(inflater);
                return false;
            }
        });

        mind = (Button) layout.findViewById(R.id.mind);
        mind.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (clickTracker.get(mind)) {
                        filter.remove((Integer) GymClass.MIND);
                        mind.setBackgroundResource(R.drawable.opaque_rounded_shape_3);
                        mind.setTextColor(getResources().getColor(R.color.card_background));
                        clickTracker.put(mind, false);
                    } else {
                        filter.add(GymClass.MIND);
                        mind.setBackgroundResource(R.drawable.transparent_rounded_shape_3);
                        mind.setTextColor(getResources().getColor(R.color.excercise_color_3));
                        clickTracker.put(mind, true);
                    }
                }
                initClassTable(inflater);
                return false;
            }
        });

        core = (Button) layout.findViewById(R.id.core);
        core.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (clickTracker.get(core)) {
                        filter.remove((Integer) GymClass.CORE);
                        core.setBackgroundResource(R.drawable.opaque_rounded_shape_4);
                        core.setTextColor(getResources().getColor(R.color.card_background));
                        clickTracker.put(core, false);
                    } else {
                        filter.add(GymClass.CORE);
                        core.setBackgroundResource(R.drawable.transparent_rounded_shape_4);
                        core.setTextColor(getResources().getColor(R.color.excercise_color_4));
                        clickTracker.put(core, true);
                    }
                }
                initClassTable(inflater);
                return false;
            }
        });

        dance = (Button) layout.findViewById(R.id.dance);
        dance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (clickTracker.get(dance)) {
                        filter.remove((Integer) GymClass.DANCE);
                        dance.setBackgroundResource(R.drawable.opaque_rounded_shape_5);
                        dance.setTextColor(getResources().getColor(R.color.card_background));
                        clickTracker.put(dance, false);
                    } else {
                        filter.add(GymClass.DANCE);
                        dance.setBackgroundResource(R.drawable.transparent_rounded_shape_5);
                        dance.setTextColor(getResources().getColor(R.color.excercise_color_5));
                        clickTracker.put(dance, true);
                    }
                }
                initClassTable(inflater);
                return false;
            }
        });

        strength = (Button) layout.findViewById(R.id.aqua);
        strength.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (clickTracker.get(aqua)) {
                        filter.remove((Integer) GymClass.ALL_AROUND);
                        aqua.setBackgroundResource(R.drawable.opaque_rounded_shape_6);
                        aqua.setTextColor(getResources().getColor(R.color.card_background));
                        clickTracker.put(aqua, false);
                    } else {
                        filter.add(GymClass.STRENGTH);
                        aqua.setBackgroundResource(R.drawable.transparent_rounded_shape_6);
                        aqua.setTextColor(getResources().getColor(R.color.excercise_color_6));
                        clickTracker.put(aqua, true);
                    }
                }
                initClassTable(inflater);
                return false;
            }
        });

        aqua = (Button) layout.findViewById(R.id.aqua);
        aqua.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (clickTracker.get(aqua)) {
                        aqua.setBackgroundResource(R.drawable.opaque_rounded_shape_7);
                        aqua.setTextColor(getResources().getColor(R.color.card_background));
                        clickTracker.put(aqua, false);
                    } else {
                        filter.remove((Integer) GymClass.AQUA);
                        filter.add(GymClass.AQUA);
                        aqua.setBackgroundResource(R.drawable.transparent_rounded_shape_7);
                        aqua.setTextColor(getResources().getColor(R.color.excercise_color_7));
                        clickTracker.put(aqua, true);
                    }
                }
                initClassTable(inflater);
                return false;
            }
        });


        clickTracker.put(allAround, false);
        clickTracker.put(cardio, false);
        clickTracker.put(mind, false);
        clickTracker.put(core, false);
        clickTracker.put(dance, false);
        clickTracker.put(aqua, false);
    }

    private void initClassTable(LayoutInflater inflater) {
        table = (TableLayout) layout.findViewById(R.id.class_table);
        View titleRow = table.findViewById(R.id.title_row);
        table.removeAllViews();
        table.addView(titleRow);

        for (GymClass gymClass: mClasses) {
            if (filter.contains(gymClass.getClassType()) ||
                    dayOfMonth != gymClass.getDate().getDate())
                continue;

            View tr = inflater.inflate(R.layout.class_row, null, false);
            Button filterColor = (Button) tr.findViewById(R.id.filterColor);
            TextView excerciseTime = (TextView) tr.findViewById(R.id.excerciseTime);
            TextView excerciseName = (TextView) tr.findViewById(R.id.excerciseName);
            TextView excerciseTrainer = (TextView) tr.findViewById(R.id.excerciseTrainer);
            TextView excerciseLocation = (TextView) tr.findViewById(R.id.excerciseLocation);

            switch (gymClass.getClassType()) {
                case GymClass.ALL_AROUND:
                    filterColor.setBackgroundColor(getResources().getColor(R.color.excercise_color_1)); break;
                case GymClass.CARDIO:
                    filterColor.setBackgroundColor(getResources().getColor(R.color.excercise_color_2)); break;
                case GymClass.MIND:
                    filterColor.setBackgroundColor(getResources().getColor(R.color.excercise_color_3)); break;
                case GymClass.CORE:
                    filterColor.setBackgroundColor(getResources().getColor(R.color.excercise_color_4)); break;
                case GymClass.DANCE:
                    filterColor.setBackgroundColor(getResources().getColor(R.color.excercise_color_5)); break;
                case GymClass.STRENGTH:
                    filterColor.setBackgroundColor(getResources().getColor(R.color.excercise_color_6)); break;
                case GymClass.AQUA:
                    filterColor.setBackgroundColor(getResources().getColor(R.color.excercise_color_7)); break;
            }


            DateFormat df = new SimpleDateFormat("HH:mm");
            excerciseTime.setText(df.format(gymClass.getStartTime()) + " - " + df.format(gymClass.getEndTime()));
            excerciseName.setText(gymClass.getName());
            excerciseTrainer.setText(gymClass.getTrainer());
            excerciseLocation.setText(gymClass.getLocation());

            table.addView(tr);
        }
    }


    private void refresh() {
        GymClassController.getInstance().refreshInBackground(getActivity(), new Callback() {

            @Override
            @SuppressWarnings("unchecked")
            public void onDataRetrieved(Object data) {
                mClasses = (ArrayList<GymClass>) data;
                Log.d(TAG, data.toString());
            }

            @Override
            public void onRetrievalFailed() {
                Toast.makeText(getContext(), "Unable to retrieve cafe data please try again",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void createDummyData() {
        SimpleDateFormat TEMP_DATE_FORMATER =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

        for (int i = 0 ; i < 15; i++) {
            String name = "Class " + i;
            String description = "";


            String trainer = "Emer Gencyexitonly";
            int classType = (i % 7);
            String location = "Court #5, RSF";

            if (i % 2 == 0) {
                trainer = "Ladis Washeroom";
                classType = (i % 7);
                location = "Court #10, RSF";
            }

            Date date = Calendar.getInstance().getTime();
            Log.d("LOLOL", TEMP_DATE_FORMATER.format(date));

            try {
                long tmpDate;
                Date startTime = null;
                Date endTime = null;
                String openingString = "2000-01-01T" + (i + 5) + ":15:00.000Z";
                String closingString = "2000-01-01T" + (i + 6) + ":15:00.000Z";

                if (!openingString.equals("null")) {
                    tmpDate = TEMP_DATE_FORMATER.parse(openingString).getTime();
                    startTime = new Date(tmpDate);
                }
                if (!closingString.equals("null")) {
                    tmpDate = TEMP_DATE_FORMATER.parse(closingString).getTime();
                    endTime = new Date(tmpDate);
                }


                mClasses.add(new GymClass(i, date, startTime, endTime, name, description, trainer, classType, location));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}

