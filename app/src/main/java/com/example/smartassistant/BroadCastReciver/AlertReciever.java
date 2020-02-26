package com.example.smartassistant.BroadCastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.smartassistant.Service.AlertIntentService;

import static com.example.smartassistant.View.AddTimeEventFragment.EVENT;

public class AlertReciever extends BroadcastReceiver {
    private static final String TAG = "Reciever";
    @Override
    public void onReceive(Context context, Intent intent) {

        long eventId=intent.getLongExtra(EVENT,0);
         Intent alertIntent=new Intent(context, AlertIntentService.class);
         AlertIntentService.enqueueAlert(context,alertIntent,eventId);




    }
}