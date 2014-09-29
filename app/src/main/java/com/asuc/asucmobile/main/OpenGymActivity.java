package com.asuc.asucmobile.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.asuc.asucmobile.R;
import com.asuc.asucmobile.models.Gym;

import java.text.SimpleDateFormat;

public class OpenGymActivity extends Activity {

    private static final SimpleDateFormat HOURS_FORMAT =
            new SimpleDateFormat("h:mm a");

    public static Gym staticGym;

    private Gym gym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gym);

        gym = staticGym;

        if (getActionBar() != null) {
            getActionBar().setTitle(gym.getName());
        }

        ImageView availabilityIcon = (ImageView) findViewById(R.id.availability_icon);
        TextView availabilityText = (TextView) findViewById(R.id.availability);
        TextView hours = (TextView) findViewById(R.id.hours);
        TextView address = (TextView) findViewById(R.id.address);

        if (gym.getOpening() == null || gym.getClosing() == null) {
            availabilityIcon.setImageDrawable(getResources().getDrawable(R.drawable.question_mark_blue));
            availabilityText.setText("Unknown Hours");
        } else if (gym.isOpen()) {
            availabilityIcon.setImageDrawable(getResources().getDrawable(R.drawable.check_mark_blue));
            availabilityText.setText("Open");
        } else {
            availabilityIcon.setImageDrawable(getResources().getDrawable(R.drawable.no_entry_blue));
            availabilityText.setText("Closed");
        }

        String hoursString;
        if (gym.getOpening() != null && gym.getClosing() != null) {
            hoursString =
                    HOURS_FORMAT.format(gym.getOpening()) +
                            " to " +
                            HOURS_FORMAT.format(gym.getClosing()
                            );
        } else {
            hoursString = "Unknown";
        }

        hours.setText(hoursString);
        address.setText(gym.getAddress());
    }

}
