package com.example.smartassistant.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smartassistant.Adapter.EventFragmentAdapter;
import com.example.smartassistant.Adapter.LocationBasedAdapter;
import com.example.smartassistant.Adapter.TimeBasedAdapter;
import com.example.smartassistant.DI.ViewModelFactory;
import com.example.smartassistant.Model.LocationBasedEvent;
import com.example.smartassistant.Model.TimeBasedEvent;
import com.example.smartassistant.R;
import com.example.smartassistant.ViewModel.LocationBasedEventViewModel;
import com.example.smartassistant.ViewModel.TimeBasedEventViewModel;
import com.example.smartassistant.databinding.FragmentHomeBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class HomeFragment extends Fragment implements View.OnClickListener {
    FragmentHomeBinding homeBinding;
    NavController navController;
    EventFragmentAdapter adapter;

    private static final String TAG = "HomeFragment";

    @Inject
    SharedPreferences preferences;
    @Inject
    SharedPreferences.Editor sfEditor;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getActivity().getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        boolean hasData = preferences.contains("isConfigured");

        if (!hasData) {
            HomeFragmentPermissionsDispatcher.ConfigureTheAppWithPermissionCheck(this);


            // navController.navigate(HomeFragmentDirections.actionHomeToConfigurationFragment());
        }
        homeBinding.timeCard.setOnClickListener(this);
        homeBinding.locationCard.setOnClickListener(this);
        homeBinding.settingCard.setOnClickListener(this);
        homeBinding.timelineCard.setOnClickListener(this);
        homeBinding.emergencyCard.setOnClickListener(this);
        homeBinding.aboutMeCard.setOnClickListener(this);


    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    public void ConfigureTheApp() {
        sfEditor.putString("msgText", "Message");
        sfEditor.putString("notificationTitle", "Notification Title");
        sfEditor.putString("notificationDescription", "Notification Description");
        sfEditor.putBoolean("isConfigured", true);
        sfEditor.putInt("selectedSim", 0);
        sfEditor.commit();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.time_card) {
            navController.navigate(R.id.action_home_to_addTimeEventFragment);
        } else if (view.getId() == R.id.location_card) {
            navController.navigate(R.id.action_home_to_addLocationEventFragment);
        } else if (view.getId() == R.id.setting_card) {
            navController.navigate(R.id.action_home_to_editConfigurationFragment);
        } else if (view.getId() == R.id.timeline_card) {
            navController.navigate(R.id.action_home_to_viewAllFragment);
        } else if (view.getId() == R.id.emergency_card) {
            navController.navigate(R.id.action_home_to_emargencyHelpLineFragment);
        } else if (view.getId() == R.id.about_me_card) {
            navController.navigate(R.id.action_home_to_aboutUsFragment);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomeFragmentPermissionsDispatcher.onRequestPermissionsResult(HomeFragment.this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_PHONE_STATE)
    void showRationale(final PermissionRequest request) {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Permission needed")
                .setMessage("App needs the permission for its service ")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                request.proceed();
                            }
                        }
                ).show();


    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    void OnDenied() {
        Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    void onNeverAskAgain() {
        Toast.makeText(getContext(), "Never asking again", Toast.LENGTH_SHORT).show();
    }


}

