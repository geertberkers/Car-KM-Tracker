package geert.berkers.cartracker.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Geert.
 */

@IgnoreExtraProperties
public class Ride {

    private String key;
    private String date;
    private String driver;
    private String description;
    private double startMileAge;
    private double endMileAge;
    private boolean paid;

    public Ride() {
        // Default constructor required for calls to DataSnapshot.getValue(Ride.class)
    }

    public Ride(String date, String driver, String description, double startMileAge, double endMileAge, boolean paid) {
        this.date = date;
        this.driver = driver;
        this.description = description;
        this.startMileAge = startMileAge;
        this.endMileAge = endMileAge;
        this.paid = paid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("driver", driver);
        result.put("description", description);
        result.put("startMileAge", startMileAge);
        result.put("endMileAge", endMileAge);
        result.put("paid", paid);
        return result;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public double getStartMileAge() {
        return startMileAge;
    }

    public double getEndMileAge() {
        return endMileAge;
    }

    public String getDescription() {
        return description;
    }

    public String getDriver() {
        return driver;
    }

    public boolean isPaid() {
        return paid;
    }

    @Override
    public boolean equals(Object otherRide) {
        if (!(otherRide instanceof Ride)) {
            return false;
        }

        Ride ride = (Ride) otherRide;

        return this.date.equals(ride.date)
                && this.driver.equals(ride.driver)
                && this.description.equals(ride.description)
                && this.startMileAge == ride.startMileAge
                && this.endMileAge == ride.endMileAge
                && this.paid == ride.paid;
    }
}