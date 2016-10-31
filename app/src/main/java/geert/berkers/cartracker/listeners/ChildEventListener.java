package geert.berkers.cartracker.listeners;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import geert.berkers.cartracker.activities.MainActivity;
import geert.berkers.cartracker.manager.RideManager;
import geert.berkers.cartracker.model.Ride;

/**
 * Created by Geert.
 */

public class ChildEventListener implements com.google.firebase.database.ChildEventListener {

    private final MainActivity mainActivity;
    private Ride latestAddedRide;

    private RideManager rideManager;

    private static final String TAG = "FireBase";

    public ChildEventListener(RideManager rideManager, MainActivity activity){
        this.rideManager = rideManager;
        this.mainActivity = activity;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d(TAG, "onChildAdded: " + dataSnapshot.getKey() + " / previous: " + previousChildName);

        Ride ride = dataSnapshot.getValue(Ride.class);
        ride.setKey(dataSnapshot.getKey());

        rideManager.onRideAdded(ride);
        latestAddedRide = ride;

        notifyMainActivityRidesList();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        Log.i(TAG, "onChildChanged: " + dataSnapshot.getKey() + " / previous: " + previousChildName);

        Ride ride = dataSnapshot.getValue(Ride.class);
        ride.setKey(dataSnapshot.getKey());

        if (!latestAddedRide.equals(ride)) {
            rideManager.onRideChanged(ride);
        }

        notifyMainActivityRidesList();

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.i(TAG, "onChildRemoved: " + dataSnapshot.getKey());

        Ride ride = dataSnapshot.getValue(Ride.class);
        ride.setKey(dataSnapshot.getKey());

        rideManager.onRideRemoved(ride);

        notifyMainActivityRidesList();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        Log.i(TAG, "onChildMoved: " + dataSnapshot.getKey());
        Log.i(TAG, "onChildChanged: PreviousChild: " + previousChildName);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w("DatabaseError", "Failed to read value.", databaseError.toException());
    }

    private void notifyMainActivityRidesList(){
        mainActivity.showRides();
    }
}
