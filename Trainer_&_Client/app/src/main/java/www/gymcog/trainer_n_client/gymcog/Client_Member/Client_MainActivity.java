package www.gymcog.trainer_n_client.gymcog.Client_Member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Membership.Client_Membership;
import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Payments.Payment_History;
import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Profile.Client_Profile;
import www.gymcog.trainer_n_client.gymcog.Client_Member.Workouts.Workouts;
import www.gymcog.trainer_n_client.gymcog.Login;
import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.MainActivity;
import www.gymcog.trainer_n_client.gymcog.Trainer.Workout_Schedule.Schedule;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Client_MainActivity extends AppCompatActivity implements View.OnClickListener {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    Button logout;
    CardView membership, feedback, payment_history;
    BottomNavigationView nav_bar;
    String temp_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);

        logout = findViewById(R.id.clogout);
        payment_history = findViewById(R.id.pay_history);
        feedback = findViewById(R.id.feedback_post);
        membership = findViewById(R.id.client_membership);
        nav_bar = findViewById(R.id.client_nav_bar);
        nav_bar.setSelectedItemId(R.id.client_dashboard);

        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.workout_plan:
                        startActivity(new Intent(getApplicationContext(), Workouts.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.client_dashboard:
                        return true;
                    case R.id.client_profile:
                        startActivity(new Intent(getApplicationContext(), Client_Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        payment_history.setOnClickListener(this);
        membership.setOnClickListener(this);
        feedback.setOnClickListener(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Client_MainActivity.this);
                builder.setMessage("Do you want to Logout?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
//                                finish();
                                Intent intent_logout = new Intent(Client_MainActivity.this, Login.class);
                                startActivity(intent_logout);
                                finishAffinity();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_post:

                DialogPlus dialogPlus = DialogPlus.newDialog(Client_MainActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.client_feedback))
                        .setBackgroundColorResId(R.color.transparent)
                        .setExpanded(true, 1800)
                        .create();

                View myView = dialogPlus.getHolderView();
                EditText feedback_text = myView.findViewById(R.id.feedback_text);
                Button submit = myView.findViewById(R.id.submit_feedback);

                SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String temp_phone = preferences.getString("user_phone", "");

                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("clients")
                                .child(temp_phone).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String temp_name = String.valueOf(snapshot.child("name").getValue());

                                        if (feedback_text.getText().toString().trim().equals("")) {
                                            feedback_text.setError("You Can't Submit a Empty Feedback.");
                                            feedback_text.requestFocus();
                                        } else {

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("feedback", feedback_text.getText().toString().trim());
                                            map.put("phone", temp_phone);
                                            map.put("name", temp_name);

                                            FirebaseDatabase.getInstance().getReference().child("feedbacks")
                                                    .child(ts).updateChildren(map)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(Client_MainActivity.this, "Feedback Submitted.", Toast.LENGTH_SHORT).show();
                                                            dialogPlus.dismiss();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Client_MainActivity.this, "Failed to Submit.", Toast.LENGTH_SHORT).show();
                                                            dialogPlus.dismiss();
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                    }
                });

                break;
            case R.id.pay_history:
                Intent pay_history = new Intent(Client_MainActivity.this, Payment_History.class);
                startActivity(pay_history);
                break;
            case R.id.client_membership:
                Intent membership = new Intent(Client_MainActivity.this, Client_Membership.class);
                startActivity(membership);
                break;
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}