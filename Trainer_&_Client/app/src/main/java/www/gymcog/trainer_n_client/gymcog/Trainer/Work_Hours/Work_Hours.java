package www.gymcog.trainer_n_client.gymcog.Trainer.Work_Hours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.MainActivity;
import www.gymcog.trainer_n_client.gymcog.Trainer.Profile.Profile;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Work_Hours extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    BottomNavigationView nav_bar;
    TextView name, mon_s, tue_s, wed_s, thur_s, fri_s, sat_s,
            mon_e, tue_e, wed_e, thur_e, fri_e, sat_e;

    String s_mon_s, s_tue_s, s_wed_s, s_thur_s, s_fri_s, s_sat_s,
            s_mon_e, s_tue_e, s_wed_e, s_thur_e, s_fri_e, s_sat_e;
    Button close, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_hours);

        name =findViewById(R.id.name);

        nav_bar=findViewById(R.id.nav_bar1);
        nav_bar.setSelectedItemId(R.id.working_hrs);
        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.working_hrs:
                        return true;
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        mon_s = findViewById(R.id.mon_s);
        tue_s = findViewById(R.id.tue_s);
        wed_s = findViewById(R.id.wed_s);
        thur_s = findViewById(R.id.thur_s);
        fri_s = findViewById(R.id.fri_s);
        sat_s = findViewById(R.id.sat_s);

        mon_e = findViewById(R.id.mon_e);
        tue_e = findViewById(R.id.tue_e);
        wed_e = findViewById(R.id.wed_e);
        thur_e = findViewById(R.id.thur_e);
        fri_e = findViewById(R.id.fri_e);
        sat_e = findViewById(R.id.sat_e);

        SharedPreferences preferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String temp_phone = preferences.getString("user_phone", "");

        reference.child("trainers").child(temp_phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String getName = String.valueOf(snapshot.child("name").getValue());
                name.setText(getName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("trainers")
                .child(temp_phone).child("Working_Time").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        s_mon_s = String.valueOf(dataSnapshot.child("Monday_start_time").getValue());
                        if(s_mon_s.equals("") || s_mon_s.equals("null")) { s_mon_s=""; }
                        s_tue_s = String.valueOf(dataSnapshot.child("Tuesday_start_time").getValue());
                        if(s_tue_s.equals("") || s_tue_s.equals("null")) { s_tue_s=""; }
                        s_wed_s = String.valueOf(dataSnapshot.child("Wednesday_start_time").getValue());
                        if(s_wed_s.equals("") || s_wed_s.equals("null")) { s_wed_s=""; }
                        s_thur_s = String.valueOf(dataSnapshot.child("Thursday_start_time").getValue());
                        if(s_thur_s.equals("") || s_thur_s.equals("null")) { s_thur_s=""; }
                        s_fri_s = String.valueOf(dataSnapshot.child("Friday_start_time").getValue());
                        if(s_fri_s.equals("") || s_fri_s.equals("null")) { s_fri_s=""; }
                        s_sat_s = String.valueOf(dataSnapshot.child("Saturday_start_time").getValue());
                        if(s_sat_s.equals("") || s_sat_s.equals("null")) { s_sat_s=""; }

                        s_mon_e = String.valueOf(dataSnapshot.child("Monday_end_time").getValue());
                        if(s_mon_e.equals("") || s_mon_e.equals("null")) { s_mon_e=""; }
                        s_tue_e = String.valueOf(dataSnapshot.child("Tuesday_end_time").getValue());
                        if(s_tue_e.equals("") || s_tue_e.equals("null")) { s_tue_e=""; }
                        s_wed_e = String.valueOf(dataSnapshot.child("Wednesday_end_time").getValue());
                        if(s_wed_e.equals("") || s_wed_e.equals("null")) { s_wed_e=""; }
                        s_thur_e = String.valueOf(dataSnapshot.child("Thursday_end_time").getValue());
                        if(s_thur_e.equals("") || s_thur_e.equals("null")) { s_thur_e=""; }
                        s_fri_e = String.valueOf(dataSnapshot.child("Friday_end_time").getValue());
                        if(s_fri_e.equals("") || s_fri_e.equals("null")) { s_fri_e=""; }
                        s_sat_e = String.valueOf(dataSnapshot.child("Saturday_end_time").getValue());
                        if(s_sat_e.equals("") || s_sat_e.equals("null")) { s_sat_e=""; }

                        mon_s.setText(s_mon_s);
                        tue_s.setText(s_tue_s);
                        wed_s.setText(s_wed_s);
                        thur_s.setText(s_thur_s);
                        fri_s.setText(s_fri_s);
                        sat_s.setText(s_sat_s);

                        mon_e.setText(s_mon_e);
                        tue_e.setText(s_tue_e);
                        wed_e.setText(s_wed_e);
                        thur_e.setText(s_thur_e);
                        fri_e.setText(s_fri_e);
                        sat_e.setText(s_sat_e);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });
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
        startActivity(new Intent(Work_Hours.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

}