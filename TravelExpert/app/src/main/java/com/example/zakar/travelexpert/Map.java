package com.example.zakar.travelexpert;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map extends SupportMapFragment {



    GoogleMap googleMap;

    private LatLng mPosFija = new LatLng(37.878901,-4.779396);

    public Map() {
        super();
    }


    public static Map newInstance(LatLng posicion) {
        Map frag = new Map();
        frag.mPosFija = posicion;
        return frag;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initMap();
    }


    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);

       // googleMap = getMap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //initMap();
        return view;
    }

    private void initMap() {
        UiSettings settings = getMap().getUiSettings();
        settings.setAllGesturesEnabled(false);
        settings.setMyLocationButtonEnabled(false);

        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(mPosFija, 16));
        getMap().addMarker(
                new MarkerOptions().position(mPosFija)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.busboxes)));
    }

}
