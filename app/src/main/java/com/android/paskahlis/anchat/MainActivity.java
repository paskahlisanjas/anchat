package com.android.paskahlis.anchat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.paskahlis.anchat.adapter.MainViewPagerAdapter;
import com.android.paskahlis.anchat.entity.EntityUser;
import com.android.paskahlis.anchat.fragment.ChatFragment;
import com.android.paskahlis.anchat.fragment.ContactFragment;
import com.android.paskahlis.anchat.service.MessagingInstanceIdService;
import com.android.paskahlis.anchat.widget.NavBarViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewPagerAdapter mainViewPagerAdapter;
    private NavBarViewPager mViewPager;
    private List<Fragment> mFrags;

    private FirebaseAuth auth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = db.getReference();

    private String selfId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contact:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_chat:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_settings:
                    mViewPager.setCurrentItem(0);
                    return true;
            }
            return false;
        }
    };

    public static double distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        selfId = auth.getCurrentUser().getUid();

        getSupportActionBar().setTitle("AnChat");
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());

        mainViewPagerAdapter.addFragment(ContactFragment.newInstance());
        mainViewPagerAdapter.addFragment(ChatFragment.newInstance());
        mViewPager = findViewById(R.id.main_view_pager);
        mViewPager.setAdapter(mainViewPagerAdapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_chat);
    }

    private void initMessaging() {
        String token = FirebaseInstanceId.getInstance().getToken();
        registerUserMessagingToken(token);
    }

    private void registerUserMessagingToken(String token) {
        dbReference.child(EntityUser.USER_ROOT).child(selfId).child(EntityUser.USER_MSG_TOKEN)
                .setValue(token);
    }

    /*@Override
    public void onLocationChanged(Location location) {
        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        distance = distance(location.getLatitude(),-5,location.getLongitude(),108);
        if(distance<2000000){
            userNearby.setText("Lala");
        }
        else{
            userNearby.setText("No One");
        }
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {

        }

    }
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }*/

}
