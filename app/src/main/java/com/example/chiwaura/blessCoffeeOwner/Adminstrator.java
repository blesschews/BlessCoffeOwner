package com.example.chiwaura.blessCoffeeOwner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by blessing on 1/24/2017.
 */

public class Adminstrator extends Activity {


    //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    //final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);

    final FirebaseDatabase Firebase = FirebaseDatabase.getInstance();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator);

        final DatabaseReference myRef = Firebase.getReference("OrderTransactions");


        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        String startDay = String.valueOf(day) + " " + String.valueOf(month) + " " + String.valueOf(year) + " "
                + "00:00:00";


        String endDay = String.valueOf(day - 1) + " " + String.valueOf(month) + " " + String.valueOf(year) + " "
                + "23:59:59";


        myRef.orderByChild("dateTimeOrdered").startAt(startDay).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //DisplayNotification();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting the data from snapshot
                    OrderTransactions orderTransactions = postSnapshot.getValue(OrderTransactions.class);

                    //Adding it to a string
                    String string = "\n\nCustomer details: " + orderTransactions.getCustomerDetails() + "\nOrdered: "
                            + orderTransactions.getOrderedItems() + "AmountDue: " + orderTransactions.getAmountDue() +
                            "\nPayment method:" + orderTransactions.getPaymentMethod() + "\nOrderTime:" +
                            orderTransactions.getDateTimeOrdered() + "\n";
                    TextView textView = (TextView) findViewById(R.id.OrderTransactions);

                    //Displaying it on textview


                    textView.setText(textView.getText() + string + "");
                    ScrollView mScrollView = (ScrollView) findViewById(R.id.SCROLLER_ID);
                    mScrollView.fullScroll(View.FOCUS_DOWN);


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }

    @Override
    public void onBackPressed() {

        goBack();
    }

    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();

    }


}







