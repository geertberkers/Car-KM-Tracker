package geert.berkers.cartracker.manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import geert.berkers.cartracker.listeners.ChildEventListener;
import geert.berkers.cartracker.model.Ride;

/**
 * Created by Geert.
 */
public class DatabaseManager {

    //TODO: Add FireBase Authentication later
    private DatabaseReference database;

    private static DatabaseManager databaseManager;

    private DatabaseManager() {
        setUpDatabase();
    }

    public static DatabaseManager getDatabaseManager() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }

        return databaseManager;
    }

    private void setUpDatabase() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void setChildEventListener(ChildEventListener childEventListener){
        database.child("rides").addChildEventListener(childEventListener);
    }

    void saveRide(Ride ride) {
        String key = database.child("rides").push().getKey();
        ride.setKey(key);
        Map<String, Object> rideValues = ride.toMap();

        database.child("rides").child(key).setValue(rideValues);
    }

    void removeRide(String key) {
        database.child("rides").child(key).removeValue();
    }

    void changeRide(Ride ride) {
        Map<String, Object> rideValues = ride.toMap();
        database.child("rides").child(ride.getKey()).setValue(rideValues);
    }
}
