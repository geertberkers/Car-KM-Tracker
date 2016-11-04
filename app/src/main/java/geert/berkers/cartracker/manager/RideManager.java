package geert.berkers.cartracker.manager;

import java.util.ArrayList;
import java.util.List;

import geert.berkers.cartracker.model.Ride;

/**
 * Created by Geert.
 */

public class RideManager {

    private static RideManager rideManager;

    private DatabaseManager databaseManager;

    private List<Ride> ridesList;

    private RideManager() {
        ridesList = new ArrayList<>();
    }

    public static RideManager getRideManager() {
        if (rideManager == null) {
            rideManager = new RideManager();
        }

        return rideManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void addRide(Ride ride) {
        databaseManager.saveRide(ride);
    }

    public void onRideAdded(Ride ride) {
        if (!ridesList.contains(ride)) {
            ridesList.add(ride);
        }
    }

    public void changeRide(Ride ride) {
        databaseManager.changeRide(ride);
    }

    public void onRideChanged(Ride changedRide) {
        List<Ride> rides = new ArrayList<>();
        for (Ride ride : ridesList) {
            if (ride.getKey().equals(changedRide.getKey())) {
                rides.add(changedRide);
            } else {
                rides.add(ride);
            }
        }

        ridesList = rides;
    }

    public void removeRide(Ride ride) {
        databaseManager.removeRide(ride.getKey());
    }

    public void onRideRemoved(Ride ride) {
        ridesList.remove(ride);
    }

    public List<Ride> getRidesList(boolean paid) {
        List<Ride> sortedRides = new ArrayList<>();

        for (Ride ride : ridesList) {
            if (ride.isPaid() == paid) {
                sortedRides.add(ride);
            }
        }

        return sortedRides;
    }

    public Ride getLastRide() {
        return ridesList.get(ridesList.size() - 1);
    }
}
