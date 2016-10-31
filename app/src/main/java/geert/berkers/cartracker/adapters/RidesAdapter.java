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
    private Context context;

    private List<Ride> rideList =  new ArrayList<>();

    public RidesAdapter(Context context, List<Ride> rideList, boolean paid){
        this.context = context;

        for(Ride ride : rideList) {
            if (ride.isPaid() == paid) {
                this.rideList.add(ride);
            }
        }
    }

    @Override
    public int getCount() {
        return rideList.size();
    }

    @Override
    public Ride getItem(int position) {
        return rideList.get(position);
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
        TextView driver = (TextView) row.findViewById(R.id.driver);
        TextView startMileAge = (TextView) row.findViewById(R.id.startMileAge);
        TextView endMileAge = (TextView) row.findViewById(R.id.drivenMileAge);
        TextView description = (TextView) row.findViewById(R.id.description);

        Ride ride = rideList.get(position);
        driver.setText(ride.getDriver());
        startMileAge.setText(String.valueOf(ride.getStartMileAge()));
        endMileAge.setText(String.valueOf(ride.getEndMileAge()));
        description.setText(String.valueOf(ride.getDescription()));

        /*
        // DIT ALLEEN TOESTAAN ALS JE ZELF EIGENAAR BENT

        editImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent workEditorIntent = new Intent(v.getContext(), RideEditor.class);
                workEditorIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                workEditorIntent.putExtra("workParcelable", rideList.get(position).getWork());

                v.getContext().startActivity(workEditorIntent);
            }
        });

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setTitle("Delete werk");
                alertDialog.setMessage("Weet je het zeker?");
                alertDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        new DeleteWorkTask().execute(rideList.get(position).getWorkString().substring(0, 10));
                        rideList.remove(rideList.get(position).getWork());
                        notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                alertDialog.setIcon(R.mipmap.ic_action_action_delete);
                alertDialog.show();
            }
        });
        */

        return row;
    }
}

