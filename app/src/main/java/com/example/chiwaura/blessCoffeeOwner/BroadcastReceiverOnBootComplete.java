package com.example.chiwaura.blessCoffeeOwner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by blessing on 3/18/2017.
 */

public class BroadcastReceiverOnBootComplete extends BroadcastReceiver {
    @Override

    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, FirebaseBackgroundService.class);
            context.startService(serviceIntent);

        }

    }


}
