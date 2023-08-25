package www.gymcog.trainer_n_client.gymcog.Trainer.Workout_Schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.MailTo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.MainActivity;
import www.gymcog.trainer_n_client.gymcog.Trainer.Membership_Plans.Membership_Plans;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.Trainee;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Schedule extends AppCompatActivity implements View.OnClickListener {

    TextView name, monday, tuesday, wednesday, thursday, friday, saturday;

    String tmp_phone;

    EditText title,no_set,no_rep,workout_1,workout_2,workout_3,workout_4,workout_5,workout_6;
    String title_value,no_set_value,no_rep_value,workout_1_value,workout_2_value,
            workout_3_value,workout_4_value,workout_5_value,workout_6_value;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);

        name = findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("name"));

        tmp_phone = getIntent().getStringExtra("phone");

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
        startActivity(new Intent(this, Workout_Schedule.class));
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.monday:
                DialogPlus mondayDialogPlus = DialogPlus.newDialog(Schedule.this)
                        .setContentHolder(new ViewHolder(R.layout.update_workout))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View mondayView = mondayDialogPlus.getHolderView();
                no_set = mondayView.findViewById(R.id.no_sets);
                no_rep = mondayView.findViewById(R.id.no_reps);
                title = mondayView.findViewById(R.id.title);
                workout_1 = mondayView.findViewById(R.id.workout_1);
                workout_2 = mondayView.findViewById(R.id.workout_2);
                workout_3 = mondayView.findViewById(R.id.workout_3);
                workout_4 = mondayView.findViewById(R.id.workout_4);
                workout_5 = mondayView.findViewById(R.id.workout_5);
                workout_6 = mondayView.findViewById(R.id.workout_6);
                Button mondaySet = mondayView.findViewById(R.id.set);

                DatabaseReference monday= FirebaseDatabase.getInstance().getReference()
                                .child("client_workout").child(tmp_phone).child("monday");

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

                mondaySet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("title", title.getText().toString().trim());
                        map.put("no_set", no_set.getText().toString().trim());
                        map.put("no_rep", no_rep.getText().toString().trim());
                        map.put("workout_1", workout_1.getText().toString().trim());
                        map.put("workout_2", workout_2.getText().toString().trim());
                        map.put("workout_3", workout_3.getText().toString().trim());
                        map.put("workout_4", workout_4.getText().toString().trim());
                        map.put("workout_5", workout_5.getText().toString().trim());
                        map.put("workout_6", workout_6.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference().child("client_workout")
                                .child(tmp_phone).child("monday").updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Schedule.this, "Workout Successfully Updated.", Toast.LENGTH_SHORT).show();
                                        mondayDialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Schedule.this, "Failed to Update.", Toast.LENGTH_SHORT).show();
                                        mondayDialogPlus.dismiss();
                                    }
                                });
                    }
                });

                break;
            case R.id.tuesday:
                DialogPlus tuesdayDialogPlus = DialogPlus.newDialog(Schedule.this)
                        .setContentHolder(new ViewHolder(R.layout.update_workout))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View tuesdayview = tuesdayDialogPlus.getHolderView();
                no_set = tuesdayview.findViewById(R.id.no_sets);
                no_rep = tuesdayview.findViewById(R.id.no_reps);
                title = tuesdayview.findViewById(R.id.title);
                workout_1 = tuesdayview.findViewById(R.id.workout_1);
                workout_2 = tuesdayview.findViewById(R.id.workout_2);
                workout_3 = tuesdayview.findViewById(R.id.workout_3);
                workout_4 = tuesdayview.findViewById(R.id.workout_4);
                workout_5 = tuesdayview.findViewById(R.id.workout_5);
                workout_6 = tuesdayview.findViewById(R.id.workout_6);
                Button tuesdaySet = tuesdayview.findViewById(R.id.set);

                DatabaseReference tuesday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(tmp_phone).child("tuesday");

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

                tuesdaySet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("title", title.getText().toString().trim());
                        map.put("no_set", no_set.getText().toString().trim());
                        map.put("no_rep", no_rep.getText().toString().trim());
                        map.put("workout_1", workout_1.getText().toString().trim());
                        map.put("workout_2", workout_2.getText().toString().trim());
                        map.put("workout_3", workout_3.getText().toString().trim());
                        map.put("workout_4", workout_4.getText().toString().trim());
                        map.put("workout_5", workout_5.getText().toString().trim());
                        map.put("workout_6", workout_6.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference().child("client_workout")
                                .child(tmp_phone).child("tuesday").updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Schedule.this, "Workout Successfully Updated.", Toast.LENGTH_SHORT).show();
                                        tuesdayDialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Schedule.this, "Failed to Update.", Toast.LENGTH_SHORT).show();
                                        tuesdayDialogPlus.dismiss();
                                    }
                                });
                    }
                });

                break;
            case R.id.wednesday:

                DialogPlus wednesdayDialogPlus = DialogPlus.newDialog(Schedule.this)
                        .setContentHolder(new ViewHolder(R.layout.update_workout))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View wednesdayView = wednesdayDialogPlus.getHolderView();
                no_set = wednesdayView.findViewById(R.id.no_sets);
                no_rep = wednesdayView.findViewById(R.id.no_reps);
                title = wednesdayView.findViewById(R.id.title);
                workout_1 = wednesdayView.findViewById(R.id.workout_1);
                workout_2 = wednesdayView.findViewById(R.id.workout_2);
                workout_3 = wednesdayView.findViewById(R.id.workout_3);
                workout_4 = wednesdayView.findViewById(R.id.workout_4);
                workout_5 = wednesdayView.findViewById(R.id.workout_5);
                workout_6 = wednesdayView.findViewById(R.id.workout_6);
                Button wednesdaySet = wednesdayView.findViewById(R.id.set);

                DatabaseReference wednesday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(tmp_phone).child("wednesday");

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

                wednesdaySet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("title", title.getText().toString().trim());
                        map.put("no_set", no_set.getText().toString().trim());
                        map.put("no_rep", no_rep.getText().toString().trim());
                        map.put("workout_1", workout_1.getText().toString().trim());
                        map.put("workout_2", workout_2.getText().toString().trim());
                        map.put("workout_3", workout_3.getText().toString().trim());
                        map.put("workout_4", workout_4.getText().toString().trim());
                        map.put("workout_5", workout_5.getText().toString().trim());
                        map.put("workout_6", workout_6.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference().child("client_workout")
                                .child(tmp_phone).child("wednesday").updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Schedule.this, "Workout Successfully Updated.", Toast.LENGTH_SHORT).show();
                                        wednesdayDialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Schedule.this, "Failed to Update.", Toast.LENGTH_SHORT).show();
                                        wednesdayDialogPlus.dismiss();
                                    }
                                });
                    }
                });

                break;
            case R.id.thursday:

                DialogPlus thursdayDialogPlus = DialogPlus.newDialog(Schedule.this)
                        .setContentHolder(new ViewHolder(R.layout.update_workout))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View thursdayView = thursdayDialogPlus.getHolderView();
                no_set = thursdayView.findViewById(R.id.no_sets);
                no_rep = thursdayView.findViewById(R.id.no_reps);
                title = thursdayView.findViewById(R.id.title);
                workout_1 = thursdayView.findViewById(R.id.workout_1);
                workout_2 = thursdayView.findViewById(R.id.workout_2);
                workout_3 = thursdayView.findViewById(R.id.workout_3);
                workout_4 = thursdayView.findViewById(R.id.workout_4);
                workout_5 = thursdayView.findViewById(R.id.workout_5);
                workout_6 = thursdayView.findViewById(R.id.workout_6);
                Button thursdaySet = thursdayView.findViewById(R.id.set);

                DatabaseReference thursday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(tmp_phone).child("thursday");

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

                thursdaySet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("title", title.getText().toString().trim());
                        map.put("no_set", no_set.getText().toString().trim());
                        map.put("no_rep", no_rep.getText().toString().trim());
                        map.put("workout_1", workout_1.getText().toString().trim());
                        map.put("workout_2", workout_2.getText().toString().trim());
                        map.put("workout_3", workout_3.getText().toString().trim());
                        map.put("workout_4", workout_4.getText().toString().trim());
                        map.put("workout_5", workout_5.getText().toString().trim());
                        map.put("workout_6", workout_6.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference().child("client_workout")
                                .child(tmp_phone).child("thursday").updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Schedule.this, "Workout Successfully Updated.", Toast.LENGTH_SHORT).show();
                                        thursdayDialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Schedule.this, "Failed to Update.", Toast.LENGTH_SHORT).show();
                                        thursdayDialogPlus.dismiss();
                                    }
                                });
                    }
                });

                break;
            case R.id.friday:

                DialogPlus fridayDialogPlus = DialogPlus.newDialog(Schedule.this)
                        .setContentHolder(new ViewHolder(R.layout.update_workout))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View fridayView = fridayDialogPlus.getHolderView();
                no_set = fridayView.findViewById(R.id.no_sets);
                no_rep = fridayView.findViewById(R.id.no_reps);
                title = fridayView.findViewById(R.id.title);
                workout_1 = fridayView.findViewById(R.id.workout_1);
                workout_2 = fridayView.findViewById(R.id.workout_2);
                workout_3 = fridayView.findViewById(R.id.workout_3);
                workout_4 = fridayView.findViewById(R.id.workout_4);
                workout_5 = fridayView.findViewById(R.id.workout_5);
                workout_6 = fridayView.findViewById(R.id.workout_6);
                Button fridaySet = fridayView.findViewById(R.id.set);

                DatabaseReference friday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(tmp_phone).child("friday");

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

                fridaySet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("title", title.getText().toString().trim());
                        map.put("no_set", no_set.getText().toString().trim());
                        map.put("no_rep", no_rep.getText().toString().trim());
                        map.put("workout_1", workout_1.getText().toString().trim());
                        map.put("workout_2", workout_2.getText().toString().trim());
                        map.put("workout_3", workout_3.getText().toString().trim());
                        map.put("workout_4", workout_4.getText().toString().trim());
                        map.put("workout_5", workout_5.getText().toString().trim());
                        map.put("workout_6", workout_6.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference().child("client_workout")
                                .child(tmp_phone).child("friday").updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Schedule.this, "Workout Successfully Updated.", Toast.LENGTH_SHORT).show();
                                        fridayDialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Schedule.this, "Failed to Update.", Toast.LENGTH_SHORT).show();
                                        fridayDialogPlus.dismiss();
                                    }
                                });
                    }
                });

                break;
            case R.id.saturday:

                DialogPlus saturdayDialogPlus = DialogPlus.newDialog(Schedule.this)
                        .setContentHolder(new ViewHolder(R.layout.update_workout))
                        .setBackgroundColorResId(R.color.grey_blue)
                        .setExpanded(true, 2000)
                        .create();

                View saturdayView = saturdayDialogPlus.getHolderView();
                no_set = saturdayView.findViewById(R.id.no_sets);
                no_rep = saturdayView.findViewById(R.id.no_reps);
                title = saturdayView.findViewById(R.id.title);
                workout_1 = saturdayView.findViewById(R.id.workout_1);
                workout_2 = saturdayView.findViewById(R.id.workout_2);
                workout_3 = saturdayView.findViewById(R.id.workout_3);
                workout_4 = saturdayView.findViewById(R.id.workout_4);
                workout_5 = saturdayView.findViewById(R.id.workout_5);
                workout_6 = saturdayView.findViewById(R.id.workout_6);
                Button saturdaySet = saturdayView.findViewById(R.id.set);

                DatabaseReference saturday= FirebaseDatabase.getInstance().getReference()
                        .child("client_workout").child(tmp_phone).child("saturday");

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

                saturdaySet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, Object> map = new HashMap<>();
                        map.put("title", title.getText().toString().trim());
                        map.put("no_set", no_set.getText().toString().trim());
                        map.put("no_rep", no_rep.getText().toString().trim());
                        map.put("workout_1", workout_1.getText().toString().trim());
                        map.put("workout_2", workout_2.getText().toString().trim());
                        map.put("workout_3", workout_3.getText().toString().trim());
                        map.put("workout_4", workout_4.getText().toString().trim());
                        map.put("workout_5", workout_5.getText().toString().trim());
                        map.put("workout_6", workout_6.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference().child("client_workout")
                                .child(tmp_phone).child("saturday").updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Schedule.this, "Workout Successfully Updated.", Toast.LENGTH_SHORT).show();
                                        saturdayDialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Schedule.this, "Failed to Update.", Toast.LENGTH_SHORT).show();
                                        saturdayDialogPlus.dismiss();
                                    }
                                });
                    }
                });

                break;
        }
    }
}