package geert.berkers.cartracker.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import geert.berkers.cartracker.R;
import geert.berkers.cartracker.adapters.MenuAdapter;
import geert.berkers.cartracker.adapters.RidesAdapter;
import geert.berkers.cartracker.listeners.ChildEventListener;
import geert.berkers.cartracker.manager.DatabaseManager;
import geert.berkers.cartracker.manager.RideManager;
import geert.berkers.cartracker.model.Ride;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ListView ridesListView;
    private MenuAdapter menuAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerListener;

    private RideManager rideManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();
    }

    private void createAndSetManagers() {
        rideManager = RideManager.getRideManager();

        DatabaseManager databaseManager = DatabaseManager.getDatabaseManager();
        databaseManager.setChildEventListener(new ChildEventListener(rideManager));

        rideManager.setDatabaseManager(databaseManager);
    }

    private void initControls(){
        ridesListView = (ListView) findViewById(R.id.workList);
        createAndSetManagers();

        setActionBar();
        setMenuAdapter();
        setDrawerLayout();
        setFloatingActionButton();
    }

    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));
        }
    }

    private void setMenuAdapter() {
        listView = (ListView) findViewById(R.id.drawerList);

        menuAdapter = new MenuAdapter(this);

        listView.setAdapter(menuAdapter);
        listView.setOnItemClickListener(this);
    }

    private void setDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerListener = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {
        };

        drawerLayout.addDrawerListener(drawerListener);
    }

    private void setFloatingActionButton(){
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.addRideButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rideEditorIntent = new Intent(MainActivity.this, RideEditor.class);
                startActivity(rideEditorIntent);
            }
        });
    }

    public void showRides(boolean paid) {
        List<Ride> rides = rideManager.getRidesList();
        RidesAdapter ridesAdapter = new RidesAdapter(this.getApplicationContext(), rides, paid);
        ridesListView.setAdapter(ridesAdapter);
        //TODO: Change this after a method in ChildEventListener is called
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectClickedMenuItem(position);
    }

    public void selectClickedMenuItem(int position) {
        listView.setItemChecked(position, true);

        if (menuAdapter.getItem(position).equals("Huidige Tank")) {
            setTitle("Huidige Tank");
            showRides(false);
            drawerLayout.closeDrawer(listView);
        } else if (menuAdapter.getItem(position).equals("Betaalde Tank")) {
            setTitle("Betaalde Tank");
            showRides(true);
            drawerLayout.closeDrawer(listView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerListener.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //TODO: Put this in the ActionDrawerToggle and test if there is data available
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
        listView.setAdapter(menuAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }


}
