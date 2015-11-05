package com.example.zakar.travelexpert;

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    static List<String[]> bustops;
    TextView lblLoc;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   setSupportActionBar(toolbar);
        context = this;
        lblLoc = (TextView) findViewById(R.id.lblLoc);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("BUS"));
        tabLayout.addTab(tabLayout.newTab().setText("TUBE"));
        tabLayout.addTab(tabLayout.newTab().setText("RAIL"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        loadBustops();


        

lblLoc.setVisibility(View.GONE);

        lblLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities utilities = new Utilities();

                Location location = utilities.getCurrentLocation(context);

                if (location != null) {
                  //  lblLoc.setText("Latitude: " + location.getLatitude() + "   Longitude: " + location.getLongitude());
                    lblLoc.setText(location.getTime() + "");
                   // lblLoc.setText(""+location.getSpeed());
                } else {
                    //  utilities.showGPSDisabledAlert("Please enable your location or connect to cellular network.", context);
                }
            }
        });


    }



    public void loadBustops() {
        InputStream inputStream = getResources().openRawResource(R.raw.busstops);

        String next[] = {};
        bustops = new ArrayList<>();

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

            while (true) {

                next = reader.readNext();
                if (next != null) {
                    bustops.add(next);

                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}



