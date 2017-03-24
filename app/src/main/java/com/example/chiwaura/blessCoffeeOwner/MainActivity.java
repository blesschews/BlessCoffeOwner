package com.example.chiwaura.blessCoffeeOwner;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {


    LoginDataBaseAdapter loginDataBaseAdapter;
    Animation fadeIn, fadeOut, zoomIn, zoomOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startService(new Intent(this, FirebaseBackgroundService.class));







   /* @Override
    public boolean onCreateOptionsMenu(Products menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(ProductsItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}*/
        final ImageView blessCoffee = (ImageView) findViewById(R.id.icon_view);
        final TextView welcome = (TextView) findViewById(R.id.Welcome);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        zoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        // blessCoffee.startAnimation(fadeIn);
        welcome.startAnimation(fadeIn);


        Button BtnMenu = (Button) findViewById(R.id.Menu);
        Button BtnReachUs = (Button) findViewById(R.id.ReachUs);
        Button link_to_login = (Button) findViewById(R.id.link_to_login);
        TextView link_to_register = (TextView) findViewById(R.id.link_to_register);
        //TextView welcome = (TextView) findViewById(R.id.Welcome);


        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();


        // Set OnClick Listener on Products TextView
        BtnMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
// TODO Auto-generated method stub

/// Create Intent for Products Activity and Start The Activity
                Intent intentMenu = new Intent(getApplicationContext(), Products.class);
                startActivity(intentMenu);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });


       /* welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, FirebaseBackgroundService.class);
                startService(serviceIntent);
            }
        });*/


        // Set OnClick Listener on ReachUs textView
        BtnReachUs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
// TODO Auto-generated method stub

/// Create Intent for reachUs Activity and Start The Activity
                Intent intentReachUs = new Intent(getApplicationContext(), ContactDetails.class);
                startActivity(intentReachUs);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });


        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.login);
        dialog.setTitle("Login");
        final FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();

        // Set OnClick Listener on login textView
        link_to_login.setOnClickListener(new View.OnClickListener() {


            // get the Refferences of views
            final EditText editTextUserName = (EditText) dialog.findViewById(R.id.editTextUserNameToLogin);
            final EditText editTextPassword = (EditText) dialog.findViewById(R.id.editTextPasswordToLogin);
            final EditText editTextEmail = (EditText) dialog.findViewById(R.id.editTextUserNameToEmail);
            final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);


            public void onClick(View v) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                dialog.show();


                Button btnSignIn = (Button) dialog.findViewById(R.id.buttonSignIn);

                btnSignIn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        String userName = editTextUserName.getText().toString();
                        final String password = editTextPassword.getText().toString();
                        final String email = editTextEmail.getText().toString();
                        final String checkedUsername = loginDataBaseAdapter.checkUserName(userName);
                        final String fullname = loginDataBaseAdapter.getFullName(userName);
                        final String address = loginDataBaseAdapter.getAddress(userName);
                        final String phonenumber = loginDataBaseAdapter.getPhonenumber(userName);
                        if (userName.equals("") || password.equals("") || email.equals("")) {
                            Toast.makeText(getApplicationContext(), "Fill up all fields", Toast.LENGTH_LONG).show();
                        } else {

                            if (!isOnline()) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
                                builder.setMessage("Check your internet connections");
                                builder.setCancelable(false);
                                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                Dialog connected = builder.create();
                                connected.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                                connected.show();
                            }
                            // TODO Auto-generated method stub
// get The User name and Password


                            progressBar.setVisibility(View.VISIBLE);

                            //authenticate user
                            auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            progressBar.setVisibility(View.GONE);
                                            if (!task.isSuccessful()) {
                                                final AlertDialog.Builder logincreated = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
                                                logincreated.setMessage("Authentication failed." + task.getException());
                                                logincreated.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                Dialog logincreate = logincreated.create();
                                                logincreate.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                                                logincreate.show();

                                            } else {
                                                Intent intentLoggedin = new Intent(getApplicationContext(), Adminstrator.class);
                                                intentLoggedin.putExtra("address", address);
                                                intentLoggedin.putExtra("phonenumber", phonenumber);
                                                intentLoggedin.putExtra("fullname", fullname);
                                                intentLoggedin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intentLoggedin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intentLoggedin);
                                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                                finish();

                                            }
                                            dialog.dismiss();
                                        }
                                    });


                        }
                    }
                });

                TextView forgotPassword = (TextView) dialog.findViewById(R.id.forgotPassword);
                forgotPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();

                    }
                });
            }
        });


        // Set OnClick Listener on link to  register
        link_to_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
// TODO Auto-generated method stub

/// Create Intent for SignUpActivity and Start The Activity
                Intent intentRegister = new Intent(getApplicationContext(), signUp.class);
                intentRegister.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentRegister.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentRegister);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();


            }
        });

        fadeIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                welcome.startAnimation(fadeOut);

            }
        });


        fadeOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                welcome.startAnimation(fadeIn);

            }
        });


        zoomIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // blessCoffee.startAnimation(zoomOut);

            }
        });


        zoomOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // blessCoffee.startAnimation(zoomIn);

            }
        });


    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        // Close The Database
        loginDataBaseAdapter.close();

    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        // Log.d("AndroidBash", "Subscribed");
        //Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        // Log.d("AndroidBash", token);
        // Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
    }


    public boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
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
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }


}
