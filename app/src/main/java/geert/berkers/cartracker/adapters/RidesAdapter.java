package geert.berkers.cartracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import geert.berkers.cartracker.R;
import geert.berkers.cartracker.model.Ride;

/**
 * Created by Geert on 11-9-2015
 */

public class RidesAdapter extends BaseAdapter {

   // private boolean paid;

    private Context context;

    private List<Ride> rides = new ArrayList<>();

    public RidesAdapter(Context context, List<Ride> rides) {
        this.context = context;
        this.rides = rides;
    }

    @Override
    public int getCount() {
        return rides.size();
    }

    @Override
    public Ride getItem(int position) {
        return rides.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.ride_layout, parent, false);
        } else {
            row = convertView;
        }

        TextView date = (TextView) row.findViewById(R.id.date);
        TextView driver = (TextView) row.findViewById(R.id.driver);
        TextView startMileAge = (TextView) row.findViewById(R.id.startMileAge);
        TextView endMileAge = (TextView) row.findViewById(R.id.drivenMileAge);
        TextView description = (TextView) row.findViewById(R.id.description);

        Ride ride = rides.get(position);
        date.setText(ride.getDate());
        driver.setText(ride.getDriver());
        startMileAge.setText(String.valueOf(ride.getStartMileAge()));
        endMileAge.setText(String.valueOf(ride.getEndMileAge()));
        description.setText(String.valueOf(ride.getDescription()));

        return row;
    }

    public void updateRides(List<Ride> rides) {
        this.rides = rides;
    }
}

