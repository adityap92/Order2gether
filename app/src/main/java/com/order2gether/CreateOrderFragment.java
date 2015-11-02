package com.order2gether;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;


/**
 * Created by Aditya on 10/19/2015.
 */
public class CreateOrderFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    View createOrder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createOrder = inflater.inflate(R.layout.create_order_fragment, container, false);


        return createOrder;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //googleMap.setLocationSource(new LatLong(51.5033630,-0.1276250));
    }
}
