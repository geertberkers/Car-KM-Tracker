package geert.berkers.cartracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import geert.berkers.cartracker.R;
import geert.berkers.cartracker.adapters.MenuAdapter;
import geert.berkers.cartracker.adapters.RidesAdapter;
import geert.berkers.cartracker.listeners.ChildEventListener;
import geert.berkers.cartracker.manager.DatabaseManager;
import geert.berkers.cartracker.manager.RideManager;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private EditText driverName;
    private ListView ridesListView;
    private MenuAdapter menuAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerListener;

    private RideManager rideManager;
    private RidesAdapter ridesAdapter;

    private boolean showPaidRides;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();
    }

    private void initControls() {
        ridesListView = (ListView) findViewById(R.id.workList);
        createAndSetManagers();

        setActionBar();
        setMenuAdapter();
        setDrawerLayout();
        setFloatingActionButton();
        initSharedPreferences();
    }

    private void createAndSetRidesAdapter() {
        ridesAdapter = new RidesAdapter(MainActivity.this, rideManager.getRidesList(showPaidRides));
        ridesListView.setAdapter(ridesAdapter);
    }

    private void createAndSetManagers() {
        rideManager = RideManager.getRideManager();

        createAndSetRidesAdapter();

        DatabaseManager databaseManager = DatabaseManager.getDatabaseManager();
        databaseManager.setChildEventListener(new ChildEventListener(rideManager, this));

        rideManager.setDatabaseManager(databaseManager);
    }

    private void setActionBar() {
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

    private void setFloatingActionButton() {
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.addRideButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startMileAgeValue = String.valueOf(rideManager.getLastRide().getEndMileAge());

                Intent rideEditorIntent = new Intent(MainActivity.this, RideEditor.class);
                rideEditorIntent.putExtra("startMileAge", startMileAgeValue);
                if(getSharedPreferencesValue("driverName") == null){
                    createAskNameDialog(rideEditorIntent);
                } else {
                    startActivity(rideEditorIntent);
                }
            }
        });
    }

    public void showRides() {
        ridesAdapter.updateRides(rideManager.getRidesList(showPaidRides));
        ridesAdapter.notifyDataSetChanged();
    }

    public void initSharedPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectClickedMenuItem(position);
    }

    public void selectClickedMenuItem(int position) {
        listView.setItemChecked(position, true);

        if (menuAdapter.getItem(position).equals("Huidige Tank")) {
            setTitle("Huidige Tank");
            showPaidRides = false;
        } else if (menuAdapter.getItem(position).equals("Betaalde Tank")) {
            setTitle("Betaalde Tank");
            showPaidRides = true;
        }

        showRides();
        drawerLayout.closeDrawer(listView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_setting) {
            createAskNameDialog(null);
            return true;
        } else if (drawerListener.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAskNameDialog(final Intent intent) {
        final View view = createDialogView();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Instellingen");
        alert.setView(view);

        alert.setPositiveButton("Klaar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String driverNameValue = driverName.getText().toString();
                if (driverName.length() >= 3) {
                    saveSharedPreferences("driverName", driverNameValue);
                } else {
                    Toast.makeText(MainActivity.this, "Voer een geldige naam in!", Toast.LENGTH_SHORT).show();
                }

                if(intent != null){
                    startActivity(intent);
                }
            }
        });

        alert.setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private View createDialogView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(55, 0, 55, 0);

        driverName = new EditText(MainActivity.this);

        driverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverName.setText(null);
            }
        });

        String currentDriver = getSharedPreferencesValue("driverName");
        driverName.setText(currentDriver);
        driverName.setHint("Naam bestuurder");
        driverName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        TextInputLayout textInputLayout = new TextInputLayout(MainActivity.this);
        textInputLayout.addView(driverName, params);

        return textInputLayout;
    }

    private void saveSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSharedPreferencesValue(String key) {
        return sharedPreferences.getString(key, null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
        listView.setAdapter(menuAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
