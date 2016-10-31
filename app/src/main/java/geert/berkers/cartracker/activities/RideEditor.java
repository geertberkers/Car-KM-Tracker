package geert.berkers.cartracker.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import geert.berkers.cartracker.R;
import geert.berkers.cartracker.manager.RideManager;
import geert.berkers.cartracker.model.Ride;

/**
 * Created by Geert on 11-9-2015
 */
public class RideEditor extends AppCompatActivity {

    private Button btnAddRide;

    private EditText startMileAge;
    private EditText endMileAge;
    private EditText description;

    private String startMileAgeString;
    private String endMileAgeString;
    private String descriptionString;

    private RideManager rideManager;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_edit_layout);

        initControls();

        rideManager = RideManager.getRideManager();

        Bundle b = getIntent().getExtras();

        try {
            Ride rideToEdit = b.getParcelable("rideParcelable");
            if (rideToEdit != null) {
                startMileAge.setText(String.valueOf(rideToEdit.getStartMileAge()));
                endMileAge.setText(String.valueOf(rideToEdit.getEndMileAge()));
                description.setText(rideToEdit.getDescription());

                String changeRide = "Rit aanpassen";
                btnAddRide.setText(changeRide);
                setTitle(changeRide);
            }
        } catch (Exception ex) {
            Log.e("NoRideToEdit", "Found no ride to edit");
        }
    }

    public void initControls() {
        setActionBar();

        btnAddRide = (Button) findViewById(R.id.btnAddRide);
        description = (EditText) findViewById(R.id.description);
        endMileAge = (EditText) findViewById(R.id.endMileAge);
        startMileAge = (EditText) findViewById(R.id.startMileAge);

        setOnFocusChangedListeners();
    }

    private void setActionBar() {
        setTitle("Rit toevoegen");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));
        }
    }

    public void setOnFocusChangedListeners() {
        startMileAge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!startMileAge.getText().toString().isEmpty()) {
                    startMileAgeString = startMileAge.getText().toString();
                }
                restoreValues();
                startMileAge.setText("");
                return false;
            }
        });
        endMileAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    endMileAgeString = endMileAge.getText().toString();
                    restoreValues();
                    endMileAge.setText("");
                }
            }
        });
        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    descriptionString = description.getText().toString();
                    restoreValues();
                    description.setText("");
                }
            }
        });

    }

    public void restoreValues() {
        if (startMileAge.getText().toString().isEmpty()) {
            startMileAge.setText(startMileAgeString);
        }
        if (endMileAge.getText().toString().isEmpty()) {
            endMileAge.setText(endMileAgeString);
        }
        if (description.getText().toString().isEmpty()) {
            description.setText(descriptionString);
        }
    }

    public void addRide(View view) {
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String driver = sharedPref.getString("driverName", null);
        double ritStartMileAge = Double.valueOf(startMileAge.getText().toString());
        double ritEndMileAge = Double.valueOf(endMileAge.getText().toString());
        String ritDescription = description.getText().toString();

        Ride ride = new Ride(
                date,
                driver,
                ritDescription,
                ritStartMileAge,
                ritEndMileAge,
                false
        );

        rideManager.addRide(ride);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
