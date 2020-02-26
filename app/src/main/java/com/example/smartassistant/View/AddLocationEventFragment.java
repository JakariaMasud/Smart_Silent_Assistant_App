package com.example.smartassistant.View;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartassistant.BroadCastReciver.LocationBasedEventReciever;
import com.example.smartassistant.Model.LocationBasedEvent;
import com.example.smartassistant.R;
import com.example.smartassistant.ViewModel.LocationBasedEventViewModel;
import com.example.smartassistant.databinding.FragmentAddLocationEventBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.schibstedspain.leku.LocationPickerActivity;

import static android.app.Activity.RESULT_OK;
import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddLocationEventFragment extends Fragment  {
    FragmentAddLocationEventBinding locationEventBinding;
    int LOCATION_REQUEST_CODE =123;
    Intent locationPickerIntent;
    private GeofencingClient geofencingClient;
    String GEOFENCE_REQ_ID;
    private float RADIUS;
    double latitude;
    double longitude;
    String address;
    String title;
    private PendingIntent geofencePendingIntent;
    LocationBasedEventViewModel locationBasedEventViewModel;


    public AddLocationEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        locationEventBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_add_location_event, container, false);
        return locationEventBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationBasedEventViewModel=new ViewModelProvider(this).get(LocationBasedEventViewModel.class);
        geofencingClient = LocationServices.getGeofencingClient(getContext());
        locationEventBinding.locationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationPickerIntent =new LocationPickerActivity.Builder()
                        .withLocation(23.810331, 90.412521)
                        .withGeolocApiKey("AIzaSyAvHCTyXb0ef26I2i5SPwrygEnblHOweBs")
                        .withSearchZone("bn")
                        .shouldReturnOkOnBackPressed()
                        .withMapStyle(R.raw.my_style)
                        .withGooglePlacesEnabled()
                        .withGoogleTimeZoneEnabled()
                        .withUnnamedRoadHidden()
                        .build(getContext());

                startActivityForResult(locationPickerIntent, LOCATION_REQUEST_CODE);
            }
        });
        locationEventBinding.addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkingUserInput();


                LocationBasedEvent event=new LocationBasedEvent(title,RADIUS,latitude,longitude,address);
                long req_id=locationBasedEventViewModel.insert(event);
                GEOFENCE_REQ_ID=String.valueOf(req_id);
                Geofence geofence = new Geofence.Builder()
                        .setRequestId(GEOFENCE_REQ_ID) // Geofence ID
                        .setCircularRegion( latitude, longitude, RADIUS) // defining fence region
                        .setExpirationDuration( Geofence.NEVER_EXPIRE ) // expiring date
                        // Transition types that it should look for
                        .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT )
                        .build();

                GeofencingRequest request = new GeofencingRequest.Builder()
                        // Notification to trigger when the Geofence is created
                        .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                        .addGeofence( geofence ) // add a Geofence
                        .build();
                geofencingClient.addGeofences(request,getGeofencePendingIntent())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Log.e("geofence","successfully added geofences");
                        }
                        else {
                            Log.e("geofence","failed to add geofences");
                            Snackbar.make(locationEventBinding.rootLayoutLocation,"something went wrong",Snackbar.LENGTH_LONG).show();
                        }
                    }
                });



            }
        });

    }

    private void checkingUserInput() {
        String radius=locationEventBinding.areaET.getText().toString();
        title=locationEventBinding.titleET.getText().toString();
        ;
        if(TextUtils.isEmpty(title)){
            locationEventBinding.titleET.setError("Title Field Can not be empty");
            return;
        }
        else {
            if(TextUtils.isEmpty(address)){
                Snackbar.make(locationEventBinding.rootLayoutLocation,"Location Must be Selected",Snackbar.LENGTH_LONG).show();
                return;
            }
            else {
                if(TextUtils.isEmpty(radius)){
                    locationEventBinding.areaET.setError("area field cannot be empty");
                    return;
                }
                else {
                    RADIUS=Float.parseFloat(radius);
                }

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==LOCATION_REQUEST_CODE && data!=null){
            latitude = data.getDoubleExtra(LATITUDE, 0.0);
            longitude  = data.getDoubleExtra(LONGITUDE, 0.0);
            address = data.getStringExtra(LOCATION_ADDRESS);
            locationEventBinding.selectedLocationTV.setText(address);


        }


    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(getContext(), LocationBasedEventReciever.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }


}
