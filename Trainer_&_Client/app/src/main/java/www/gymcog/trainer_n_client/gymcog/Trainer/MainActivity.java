package www.gymcog.trainer_n_client.gymcog.Trainer;

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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Profile.Client_Profile;
import www.gymcog.trainer_n_client.gymcog.Login;
import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.Feedback.Feedback;
import www.gymcog.trainer_n_client.gymcog.Trainer.Membership_Plans.Membership_Plans;
import www.gymcog.trainer_n_client.gymcog.Trainer.Profile.Profile;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.Trainee;
import www.gymcog.trainer_n_client.gymcog.Trainer.Work_Hours.Work_Hours;
import www.gymcog.trainer_n_client.gymcog.Trainer.Workout_Schedule.Workout_Schedule;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    Button logout;
    CardView trainee,membership,workout_schedule,feedback;
    BottomNavigationView nav_bar;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);
        feedback=findViewById(R.id.feedback);
        trainee=findViewById(R.id.trainee);
        membership=findViewById(R.id.membership);
        workout_schedule=findViewById(R.id.workout_schedule);
        nav_bar=findViewById(R.id.nav_bar);
        nav_bar.setSelectedItemId(R.id.dashboard);

        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.working_hrs:
                        startActivity(new Intent(getApplicationContext(), Work_Hours.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dashboard:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        trainee.setOnClickListener(this);
        membership.setOnClickListener(this);
        workout_schedule.setOnClickListener(this);
        feedback.setOnClickListener(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to Logout?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences preferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
//                                finish();
                                Intent intent_logout = new Intent(MainActivity.this, Login.class);
                                startActivity(intent_logout);
                                finishAffinity();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog= builder.create();
                alertDialog.show();
            }
        });
        
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.feedback:
                Intent feedback= new Intent(MainActivity.this, Feedback.class);
                startActivity(feedback);
                break;
            case R.id.trainee:
                Intent trainee= new Intent(MainActivity.this, Trainee.class);
                startActivity(trainee);
                break;
            case R.id.membership:
                Intent membership= new Intent(MainActivity.this, Membership_Plans.class);
                startActivity(membership);
                break;
            case R.id.workout_schedule:
                Intent schedule= new Intent(MainActivity.this, Workout_Schedule.class);
                startActivity(schedule);
                break;
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
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
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }

}