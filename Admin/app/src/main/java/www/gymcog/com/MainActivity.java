package www.gymcog.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import www.gymcog.com.Membership.Membership_Plans;
import www.gymcog.com.Payments.Payment;
import www.gymcog.com.Profile.Profile;
import www.gymcog.com.Trainee.Trainee;
import www.gymcog.com.Trainer.Trainer;
import www.gymcog.com.Trainer_Schedule.Trainer_Schedule;
import www.gymcog.com.Utility.NetworkChangeListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    Button logout;
    CardView trainer,trainee,membership,schedule;

    BottomNavigationView nav_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        logout = findViewById(R.id.logout);
        trainer=findViewById(R.id.trainer);
        trainee=findViewById(R.id.trainee);
        membership=findViewById(R.id.membership);
        schedule=findViewById(R.id.schedule);
        nav_bar=findViewById(R.id.nav_bar);
        nav_bar.setSelectedItemId(R.id.dashboard);

        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.payment:
                        startActivity(new Intent(getApplicationContext(), Payment.class));
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

        trainer.setOnClickListener(this);
        trainee.setOnClickListener(this);
        membership.setOnClickListener(this);
        schedule.setOnClickListener(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to Logout?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
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
            case R.id.trainer:
                Intent trainer= new Intent(MainActivity.this, Trainer.class);
                startActivity(trainer);
                break;
            case R.id.trainee:
                Intent trainee= new Intent(MainActivity.this, Trainee.class);
                startActivity(trainee);
                break;
            case R.id.membership:
                Intent membership= new Intent(MainActivity.this, Membership_Plans.class);
                startActivity(membership);
                break;
            case R.id.schedule:
                Intent schedule= new Intent(MainActivity.this, Trainer_Schedule.class);
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