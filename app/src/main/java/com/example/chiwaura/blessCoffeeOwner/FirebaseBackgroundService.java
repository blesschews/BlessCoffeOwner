package com.example.chiwaura.blessCoffeeOwner;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by blessing on 3/2/2017.
 */

public class FirebaseBackgroundService extends Service {

    final FirebaseDatabase Firebase = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = Firebase.getReference("OrderTransactions");


    @Override
    public IBinder onBind(Intent dataSnapshot) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(getApplicationContext(), "BlessCoffee notifications enabled", Toast.LENGTH_LONG).show();


        long lastActive = new Date().getTime();
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        final String startDay = String.valueOf(day) + " " + String.valueOf(month) + " " + String.valueOf(year) +
                " " + "00:00:00";
        final String nowTime = String.valueOf(day) + " " + String.valueOf(month) + " " + String.valueOf(year) +
                " " + String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second);


        myRef.orderByChild("dateTimeOrdered").startAt(startDay).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DisplayNotification();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void DisplayNotification() {


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.blesscoffee);
        mBuilder.setContentTitle("New Order ");
        mBuilder.setContentText("You have a new order");
        mBuilder.setAutoCancel(true);

        Intent resultIntent = new Intent(this, Adminstrator.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Adminstrator.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }


}


