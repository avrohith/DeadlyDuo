package com.deadlyduo.deadlyworkout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearService extends WearableListenerService {

    private static final String TAG = WearService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        Log.i(TAG, "Message received!");
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREF, MODE_PRIVATE);
        int option = sharedPreferences.getInt(MainActivity.OPTION_KEY, 0);

        if (option == 0){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sharedPreferences.getString(MainActivity.PHONE_KEY, null), null, "Help me please!!", null, null);
        }else if (option == 1){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sharedPreferences.getString(MainActivity.PHONE_KEY, null), null, "Help me please!! My Location is 42.363775, -71.087243", null, null);
        }
    }
}
