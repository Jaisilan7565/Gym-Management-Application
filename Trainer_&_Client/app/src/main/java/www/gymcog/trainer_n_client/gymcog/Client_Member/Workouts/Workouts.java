package www.gymcog.trainer_n_client.gymcog.Client_Member.Workouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_MainActivity;
import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Profile.Client_Profile;
import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.Workout_Schedule.Schedule;
import www.gymcog.trainer_n_client.gymcog.Trainer.Workout_Schedule.Workout_Schedule;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Workouts extends AppCompatActivity implements View.OnClickListener {

    BottomNavigationView nav_bar;
    TextView name, monday, tuesday, wednesday, thursday, friday, saturday;

    TextView title, no_set, no_rep, workout_1, workout_2, workout_3, workout_4, workout_5, workout_6;
    String title_value, no_set_value, no_rep_value, workout_1_value, workout_2_value,
            workout_3_value, workout_4_value, workout_5_value, workout_6_value;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
            .child("clients");

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        nav_bar=findViewById(R.id.client_nav_bar3);
        nav_bar.setSelectedItemId(R.id.client_dashboard);

        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.workout_plan:
                        return true;
                    case R.id.client_dashboard:
                        startActivity(new Intent(getApplicationContext(), Client_MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.client_profile:
                        startActivity(new Intent(getApplicationContext(), Client_Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        SharedPreferences preferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String temp_phone = preferences.getString("user_phone", "");

        name = findViewById(R.id.name);
        reference.child(temp_phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tmp_name = String.valueOf(snapshot.child("name").getValue());
                name.setText(tmp_name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        monday.setOnClickListener(this);
        tuesday.setOnClickListener(this);
        wednesday.setOnClickListener(this);
        thursday.setOnClickListener(this);
        friday.setOnClickListener(this);
        saturday.setOnClickListener(this);

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
        startActivity(new Intent(this, Client_MainActivity.class));
        overridePendingTransition(0, 0);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences preferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String temp_phone = preferences.getString("user_phone", "");
        switch (v.getId()) {
            case R.id.monday:
                DialogPlus mondayDialogPlus = DialogPlus.newDialog(Workouts.this)
                        .setContentHolder(new ViewHolder(R.layout.client_workout_dialogue))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View mondayView = mondayDialogPlus.getHolderView();
                no_set = mondayView.findViewById(R.id.cno_sets);
                no_rep = mondayView.findViewById(R.id.cno_reps);
                title = mondayView.findViewById(R.id.title);
                workout_1 = mondayView.findViewById(R.id.cworkout_1);
                workout_2 = mondayView.findViewById(R.id.cworkout_2);
                workout_3 = mondayView.findViewById(R.id.cworkout_3);
                workout_4 = mondayView.findViewById(R.id.cworkout_4);
                workout_5 = mondayView.findViewById(R.id.cworkout_5);
                workout_6 = mondayView.findViewById(R.id.cworkout_6);

                DatabaseReference monday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(temp_phone).child("monday");

                monday.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        title_value = String.valueOf(dataSnapshot.child("title").getValue());
                        if (title_value.equals("null")){ title_value=""; }
                        no_set_value = String.valueOf(dataSnapshot.child("no_set").getValue());
                        if (no_set_value.equals("null")){ no_set_value=""; }
                        no_rep_value = String.valueOf(dataSnapshot.child("no_rep").getValue());
                        if (no_rep_value.equals("null")){ no_rep_value=""; }
                        workout_1_value = String.valueOf(dataSnapshot.child("workout_1").getValue());
                        if (workout_1_value.equals("null")){ workout_1_value=""; }
                        workout_2_value = String.valueOf(dataSnapshot.child("workout_2").getValue());
                        if (workout_2_value.equals("null")){ workout_2_value=""; }
                        workout_3_value = String.valueOf(dataSnapshot.child("workout_3").getValue());
                        if (workout_3_value.equals("null")){ workout_3_value=""; }
                        workout_4_value = String.valueOf(dataSnapshot.child("workout_4").getValue());
                        if (workout_4_value.equals("null")){ workout_4_value=""; }
                        workout_5_value = String.valueOf(dataSnapshot.child("workout_5").getValue());
                        if (workout_5_value.equals("null")){ workout_5_value=""; }
                        workout_6_value = String.valueOf(dataSnapshot.child("workout_6").getValue());
                        if (workout_6_value.equals("null")){ workout_6_value=""; }

                        title.setText(title_value);
                        no_set.setText(no_set_value);
                        no_rep.setText(no_rep_value);
                        workout_1.setText(workout_1_value);
                        workout_2.setText(workout_2_value);
                        workout_3.setText(workout_3_value);
                        workout_4.setText(workout_4_value);
                        workout_5.setText(workout_5_value);
                        workout_6.setText(workout_6_value);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });
                mondayDialogPlus.show();

                break;
            case R.id.tuesday:
                DialogPlus tuesdayDialogPlus = DialogPlus.newDialog(Workouts.this)
                        .setContentHolder(new ViewHolder(R.layout.client_workout_dialogue))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View tuesdayview = tuesdayDialogPlus.getHolderView();
                no_set = tuesdayview.findViewById(R.id.cno_sets);
                no_rep = tuesdayview.findViewById(R.id.cno_reps);
                title = tuesdayview.findViewById(R.id.title);
                workout_1 = tuesdayview.findViewById(R.id.cworkout_1);
                workout_2 = tuesdayview.findViewById(R.id.cworkout_2);
                workout_3 = tuesdayview.findViewById(R.id.cworkout_3);
                workout_4 = tuesdayview.findViewById(R.id.cworkout_4);
                workout_5 = tuesdayview.findViewById(R.id.cworkout_5);
                workout_6 = tuesdayview.findViewById(R.id.cworkout_6);

                DatabaseReference tuesday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(temp_phone).child("tuesday");

                tuesday.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        title_value = String.valueOf(dataSnapshot.child("title").getValue());
                        if (title_value.equals("null")){ title_value=""; }
                        no_set_value = String.valueOf(dataSnapshot.child("no_set").getValue());
                        if (no_set_value.equals("null")){ no_set_value=""; }
                        no_rep_value = String.valueOf(dataSnapshot.child("no_rep").getValue());
                        if (no_rep_value.equals("null")){ no_rep_value=""; }
                        workout_1_value = String.valueOf(dataSnapshot.child("workout_1").getValue());
                        if (workout_1_value.equals("null")){ workout_1_value=""; }
                        workout_2_value = String.valueOf(dataSnapshot.child("workout_2").getValue());
                        if (workout_2_value.equals("null")){ workout_2_value=""; }
                        workout_3_value = String.valueOf(dataSnapshot.child("workout_3").getValue());
                        if (workout_3_value.equals("null")){ workout_3_value=""; }
                        workout_4_value = String.valueOf(dataSnapshot.child("workout_4").getValue());
                        if (workout_4_value.equals("null")){ workout_4_value=""; }
                        workout_5_value = String.valueOf(dataSnapshot.child("workout_5").getValue());
                        if (workout_5_value.equals("null")){ workout_5_value=""; }
                        workout_6_value = String.valueOf(dataSnapshot.child("workout_6").getValue());
                        if (workout_6_value.equals("null")){ workout_6_value=""; }

                        title.setText(title_value);
                        no_set.setText(no_set_value);
                        no_rep.setText(no_rep_value);
                        workout_1.setText(workout_1_value);
                        workout_2.setText(workout_2_value);
                        workout_3.setText(workout_3_value);
                        workout_4.setText(workout_4_value);
                        workout_5.setText(workout_5_value);
                        workout_6.setText(workout_6_value);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });

                tuesdayDialogPlus.show();

                break;
            case R.id.wednesday:

                DialogPlus wednesdayDialogPlus = DialogPlus.newDialog(Workouts.this)
                        .setContentHolder(new ViewHolder(R.layout.client_workout_dialogue))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View wednesdayView = wednesdayDialogPlus.getHolderView();
                no_set = wednesdayView.findViewById(R.id.cno_sets);
                no_rep = wednesdayView.findViewById(R.id.cno_reps);
                title = wednesdayView.findViewById(R.id.title);
                workout_1 = wednesdayView.findViewById(R.id.cworkout_1);
                workout_2 = wednesdayView.findViewById(R.id.cworkout_2);
                workout_3 = wednesdayView.findViewById(R.id.cworkout_3);
                workout_4 = wednesdayView.findViewById(R.id.cworkout_4);
                workout_5 = wednesdayView.findViewById(R.id.cworkout_5);
                workout_6 = wednesdayView.findViewById(R.id.cworkout_6);

                DatabaseReference wednesday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(temp_phone).child("wednesday");

                wednesday.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        title_value = String.valueOf(dataSnapshot.child("title").getValue());
                        if (title_value.equals("null")){ title_value=""; }
                        no_set_value = String.valueOf(dataSnapshot.child("no_set").getValue());
                        if (no_set_value.equals("null")){ no_set_value=""; }
                        no_rep_value = String.valueOf(dataSnapshot.child("no_rep").getValue());
                        if (no_rep_value.equals("null")){ no_rep_value=""; }
                        workout_1_value = String.valueOf(dataSnapshot.child("workout_1").getValue());
                        if (workout_1_value.equals("null")){ workout_1_value=""; }
                        workout_2_value = String.valueOf(dataSnapshot.child("workout_2").getValue());
                        if (workout_2_value.equals("null")){ workout_2_value=""; }
                        workout_3_value = String.valueOf(dataSnapshot.child("workout_3").getValue());
                        if (workout_3_value.equals("null")){ workout_3_value=""; }
                        workout_4_value = String.valueOf(dataSnapshot.child("workout_4").getValue());
                        if (workout_4_value.equals("null")){ workout_4_value=""; }
                        workout_5_value = String.valueOf(dataSnapshot.child("workout_5").getValue());
                        if (workout_5_value.equals("null")){ workout_5_value=""; }
                        workout_6_value = String.valueOf(dataSnapshot.child("workout_6").getValue());
                        if (workout_6_value.equals("null")){ workout_6_value=""; }

                        title.setText(title_value);
                        no_set.setText(no_set_value);
                        no_rep.setText(no_rep_value);
                        workout_1.setText(workout_1_value);
                        workout_2.setText(workout_2_value);
                        workout_3.setText(workout_3_value);
                        workout_4.setText(workout_4_value);
                        workout_5.setText(workout_5_value);
                        workout_6.setText(workout_6_value);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });

                wednesdayDialogPlus.show();

                break;
            case R.id.thursday:

                DialogPlus thursdayDialogPlus = DialogPlus.newDialog(Workouts.this)
                        .setContentHolder(new ViewHolder(R.layout.client_workout_dialogue))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View thursdayView = thursdayDialogPlus.getHolderView();
                no_set = thursdayView.findViewById(R.id.cno_sets);
                no_rep = thursdayView.findViewById(R.id.cno_reps);
                title = thursdayView.findViewById(R.id.title);
                workout_1 = thursdayView.findViewById(R.id.cworkout_1);
                workout_2 = thursdayView.findViewById(R.id.cworkout_2);
                workout_3 = thursdayView.findViewById(R.id.cworkout_3);
                workout_4 = thursdayView.findViewById(R.id.cworkout_4);
                workout_5 = thursdayView.findViewById(R.id.cworkout_5);
                workout_6 = thursdayView.findViewById(R.id.cworkout_6);

                DatabaseReference thursday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(temp_phone).child("thursday");

                thursday.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        title_value = String.valueOf(dataSnapshot.child("title").getValue());
                        if (title_value.equals("null")){ title_value=""; }
                        no_set_value = String.valueOf(dataSnapshot.child("no_set").getValue());
                        if (no_set_value.equals("null")){ no_set_value=""; }
                        no_rep_value = String.valueOf(dataSnapshot.child("no_rep").getValue());
                        if (no_rep_value.equals("null")){ no_rep_value=""; }
                        workout_1_value = String.valueOf(dataSnapshot.child("workout_1").getValue());
                        if (workout_1_value.equals("null")){ workout_1_value=""; }
                        workout_2_value = String.valueOf(dataSnapshot.child("workout_2").getValue());
                        if (workout_2_value.equals("null")){ workout_2_value=""; }
                        workout_3_value = String.valueOf(dataSnapshot.child("workout_3").getValue());
                        if (workout_3_value.equals("null")){ workout_3_value=""; }
                        workout_4_value = String.valueOf(dataSnapshot.child("workout_4").getValue());
                        if (workout_4_value.equals("null")){ workout_4_value=""; }
                        workout_5_value = String.valueOf(dataSnapshot.child("workout_5").getValue());
                        if (workout_5_value.equals("null")){ workout_5_value=""; }
                        workout_6_value = String.valueOf(dataSnapshot.child("workout_6").getValue());
                        if (workout_6_value.equals("null")){ workout_6_value=""; }

                        title.setText(title_value);
                        no_set.setText(no_set_value);
                        no_rep.setText(no_rep_value);
                        workout_1.setText(workout_1_value);
                        workout_2.setText(workout_2_value);
                        workout_3.setText(workout_3_value);
                        workout_4.setText(workout_4_value);
                        workout_5.setText(workout_5_value);
                        workout_6.setText(workout_6_value);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });

                thursdayDialogPlus.show();

                break;
            case R.id.friday:

                DialogPlus fridayDialogPlus = DialogPlus.newDialog(Workouts.this)
                        .setContentHolder(new ViewHolder(R.layout.client_workout_dialogue))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View fridayView = fridayDialogPlus.getHolderView();
                no_set = fridayView.findViewById(R.id.cno_sets);
                no_rep = fridayView.findViewById(R.id.cno_reps);
                title = fridayView.findViewById(R.id.title);
                workout_1 = fridayView.findViewById(R.id.cworkout_1);
                workout_2 = fridayView.findViewById(R.id.cworkout_2);
                workout_3 = fridayView.findViewById(R.id.cworkout_3);
                workout_4 = fridayView.findViewById(R.id.cworkout_4);
                workout_5 = fridayView.findViewById(R.id.cworkout_5);
                workout_6 = fridayView.findViewById(R.id.cworkout_6);

                DatabaseReference friday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(temp_phone).child("friday");

                friday.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        title_value = String.valueOf(dataSnapshot.child("title").getValue());
                        if (title_value.equals("null")){ title_value=""; }
                        no_set_value = String.valueOf(dataSnapshot.child("no_set").getValue());
                        if (no_set_value.equals("null")){ no_set_value=""; }
                        no_rep_value = String.valueOf(dataSnapshot.child("no_rep").getValue());
                        if (no_rep_value.equals("null")){ no_rep_value=""; }
                        workout_1_value = String.valueOf(dataSnapshot.child("workout_1").getValue());
                        if (workout_1_value.equals("null")){ workout_1_value=""; }
                        workout_2_value = String.valueOf(dataSnapshot.child("workout_2").getValue());
                        if (workout_2_value.equals("null")){ workout_2_value=""; }
                        workout_3_value = String.valueOf(dataSnapshot.child("workout_3").getValue());
                        if (workout_3_value.equals("null")){ workout_3_value=""; }
                        workout_4_value = String.valueOf(dataSnapshot.child("workout_4").getValue());
                        if (workout_4_value.equals("null")){ workout_4_value=""; }
                        workout_5_value = String.valueOf(dataSnapshot.child("workout_5").getValue());
                        if (workout_5_value.equals("null")){ workout_5_value=""; }
                        workout_6_value = String.valueOf(dataSnapshot.child("workout_6").getValue());
                        if (workout_6_value.equals("null")){ workout_6_value=""; }

                        title.setText(title_value);
                        no_set.setText(no_set_value);
                        no_rep.setText(no_rep_value);
                        workout_1.setText(workout_1_value);
                        workout_2.setText(workout_2_value);
                        workout_3.setText(workout_3_value);
                        workout_4.setText(workout_4_value);
                        workout_5.setText(workout_5_value);
                        workout_6.setText(workout_6_value);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });

                fridayDialogPlus.show();

                break;
            case R.id.saturday:

                DialogPlus saturdayDialogPlus = DialogPlus.newDialog(Workouts.this)
                        .setContentHolder(new ViewHolder(R.layout.client_workout_dialogue))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View saturdayView = saturdayDialogPlus.getHolderView();
                no_set = saturdayView.findViewById(R.id.cno_sets);
                no_rep = saturdayView.findViewById(R.id.cno_reps);
                title = saturdayView.findViewById(R.id.title);
                workout_1 = saturdayView.findViewById(R.id.cworkout_1);
                workout_2 = saturdayView.findViewById(R.id.cworkout_2);
                workout_3 = saturdayView.findViewById(R.id.cworkout_3);
                workout_4 = saturdayView.findViewById(R.id.cworkout_4);
                workout_5 = saturdayView.findViewById(R.id.cworkout_5);
                workout_6 = saturdayView.findViewById(R.id.cworkout_6);

                DatabaseReference saturday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(temp_phone).child("saturday");

                saturday.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        title_value = String.valueOf(dataSnapshot.child("title").getValue());
                        if (title_value.equals("null")){ title_value=""; }
                        no_set_value = String.valueOf(dataSnapshot.child("no_set").getValue());
                        if (no_set_value.equals("null")){ no_set_value=""; }
                        no_rep_value = String.valueOf(dataSnapshot.child("no_rep").getValue());
                        if (no_rep_value.equals("null")){ no_rep_value=""; }
                        workout_1_value = String.valueOf(dataSnapshot.child("workout_1").getValue());
                        if (workout_1_value.equals("null")){ workout_1_value=""; }
                        workout_2_value = String.valueOf(dataSnapshot.child("workout_2").getValue());
                        if (workout_2_value.equals("null")){ workout_2_value=""; }
                        workout_3_value = String.valueOf(dataSnapshot.child("workout_3").getValue());
                        if (workout_3_value.equals("null")){ workout_3_value=""; }
                        workout_4_value = String.valueOf(dataSnapshot.child("workout_4").getValue());
                        if (workout_4_value.equals("null")){ workout_4_value=""; }
                        workout_5_value = String.valueOf(dataSnapshot.child("workout_5").getValue());
                        if (workout_5_value.equals("null")){ workout_5_value=""; }
                        workout_6_value = String.valueOf(dataSnapshot.child("workout_6").getValue());
                        if (workout_6_value.equals("null")){ workout_6_value=""; }

                        title.setText(title_value);
                        no_set.setText(no_set_value);
                        no_rep.setText(no_rep_value);
                        workout_1.setText(workout_1_value);
                        workout_2.setText(workout_2_value);
                        workout_3.setText(workout_3_value);
                        workout_4.setText(workout_4_value);
                        workout_5.setText(workout_5_value);
                        workout_6.setText(workout_6_value);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                    }
                });

                saturdayDialogPlus.show();

                break;
        }
    }
}