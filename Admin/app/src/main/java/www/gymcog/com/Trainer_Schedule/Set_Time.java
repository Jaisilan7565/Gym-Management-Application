package www.gymcog.com.Trainer_Schedule;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import www.gymcog.com.R;
import www.gymcog.com.Utility.NetworkChangeListener;

public class Set_Time extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    TextView name, mon_s, tue_s, wed_s, thur_s, fri_s, sat_s,
            mon_e, tue_e, wed_e, thur_e, fri_e, sat_e;

    String s_mon_s, s_tue_s, s_wed_s, s_thur_s, s_fri_s, s_sat_s,
            s_mon_e, s_tue_e, s_wed_e, s_thur_e, s_fri_e, s_sat_e;
    Button close, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);



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

        name = (TextView) findViewById(R.id.name);
        name.setText(getIntent().getStringExtra("name"));

        String tmp_phone = getIntent().getStringExtra("phone");

        close = findViewById(R.id.back_btn);
        update = findViewById(R.id.update_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Trainer_Schedule.class);
                startActivity(intent);
            }
        });
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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogPlus dialogPlus = DialogPlus.newDialog(Set_Time.this)
                        .setContentHolder(new ViewHolder(R.layout.timetable_update))
                        .setExpanded(true, 1400)
                        .setBackgroundColorResId(R.color.deep_purple).create();

                View myview = dialogPlus.getHolderView();
                final Spinner et_mon_s = myview.findViewById(R.id.et_mon_s);
                final Spinner et_tue_s = myview.findViewById(R.id.et_tue_s);
                final Spinner et_wed_s = myview.findViewById(R.id.et_wed_s);
                final Spinner et_thur_s = myview.findViewById(R.id.et_thur_s);
                final Spinner et_fri_s = myview.findViewById(R.id.et_fri_s);
                final Spinner et_sat_s = myview.findViewById(R.id.et_sat_s);

                final Spinner et_mon_e = myview.findViewById(R.id.et_mon_e);
                final Spinner et_tue_e = myview.findViewById(R.id.et_tue_e);
                final Spinner et_wed_e = myview.findViewById(R.id.et_wed_e);
                final Spinner et_thur_e = myview.findViewById(R.id.et_thur_e);
                final Spinner et_fri_e = myview.findViewById(R.id.et_fri_e);
                final Spinner et_sat_e = myview.findViewById(R.id.et_sat_e);

                Button et_update_btn = myview.findViewById(R.id.et_update_btn);
                ArrayAdapter<CharSequence> adapter=ArrayAdapter
                        .createFromResource(getApplicationContext(),R.array.times, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                et_mon_s.setAdapter(adapter);
                et_tue_s.setAdapter(adapter);
                et_wed_s.setAdapter(adapter);
                et_thur_s.setAdapter(adapter);
                et_fri_s.setAdapter(adapter);
                et_sat_s.setAdapter(adapter);

                et_mon_e.setAdapter(adapter);
                et_tue_e.setAdapter(adapter);
                et_wed_e.setAdapter(adapter);
                et_thur_e.setAdapter(adapter);
                et_fri_e.setAdapter(adapter);
                et_sat_e.setAdapter(adapter);

                et_mon_s.setSelection(adapter.getPosition(s_mon_s));
                et_tue_s.setSelection(adapter.getPosition(s_tue_s));
                et_wed_s.setSelection(adapter.getPosition(s_wed_s));
                et_thur_s.setSelection(adapter.getPosition(s_thur_s));
                et_fri_s.setSelection(adapter.getPosition(s_fri_s));
                et_sat_s.setSelection(adapter.getPosition(s_sat_s));

                et_mon_e.setSelection(adapter.getPosition(s_mon_e));
                et_tue_e.setSelection(adapter.getPosition(s_tue_e));
                et_wed_e.setSelection(adapter.getPosition(s_wed_e));
                et_thur_e.setSelection(adapter.getPosition(s_thur_e));
                et_fri_e.setSelection(adapter.getPosition(s_fri_e));
                et_sat_e.setSelection(adapter.getPosition(s_sat_e));

                dialogPlus.show();

                et_update_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p_et_mon_s=et_mon_s.getSelectedItemPosition();
                        int p_et_mon_e=et_mon_e.getSelectedItemPosition();
                        int p_et_tue_s=et_tue_s.getSelectedItemPosition();
                        int p_et_tue_e=et_tue_e.getSelectedItemPosition();
                        int p_et_wed_s=et_wed_s.getSelectedItemPosition();
                        int p_et_wed_e=et_wed_e.getSelectedItemPosition();
                        int p_et_thur_s=et_thur_s.getSelectedItemPosition();
                        int p_et_thur_e=et_thur_e.getSelectedItemPosition();
                        int p_et_fri_s=et_fri_s.getSelectedItemPosition();
                        int p_et_fri_e=et_fri_e.getSelectedItemPosition();
                        int p_et_sat_s=et_sat_s.getSelectedItemPosition();
                        int p_et_sat_e=et_sat_e.getSelectedItemPosition();

                        int last= adapter.getCount()-1;
                        int second_last= adapter.getCount()-2;

                        if(p_et_mon_s>p_et_mon_e){
                            Toast.makeText(Set_Time.this, "End Time should be after Start Time on Monday.", Toast.LENGTH_LONG).show();
                        } else if (p_et_mon_s==p_et_mon_e) {
                            et_mon_s.setSelection(second_last);
                            et_mon_e.setSelection(last);
                        } else if (p_et_tue_s>p_et_tue_e) {
                            Toast.makeText(Set_Time.this, "End Time should be after Start Time on Tuesday.", Toast.LENGTH_LONG).show();
                        }else if (p_et_tue_s==p_et_tue_e) {
                            et_tue_s.setSelection(second_last);
                            et_tue_e.setSelection(last);
                        } else if (p_et_wed_s>p_et_wed_e) {
                            Toast.makeText(Set_Time.this, "End Time should be after Start Time on Wednesday.", Toast.LENGTH_LONG).show();
                        }else if (p_et_wed_s==p_et_wed_e) {
                            et_wed_s.setSelection(second_last);
                            et_wed_e.setSelection(last);
                        } else if (p_et_thur_s>p_et_thur_e) {
                            Toast.makeText(Set_Time.this, "End Time should be after Start Time on Thursday.", Toast.LENGTH_LONG).show();
                        }else if (p_et_thur_s==p_et_thur_e) {
                            et_thur_s.setSelection(second_last);
                            et_thur_e.setSelection(last);
                        } else if (p_et_fri_s>p_et_fri_e) {
                            Toast.makeText(Set_Time.this, "End Time should be after Start Time on Friday.", Toast.LENGTH_LONG).show();
                        }else if (p_et_fri_s==p_et_fri_e) {
                            et_fri_s.setSelection(second_last);
                            et_fri_e.setSelection(last);
                        } else if (p_et_sat_s>p_et_sat_e) {
                            Toast.makeText(Set_Time.this, "End Time should be after Start Time on Saturday.", Toast.LENGTH_LONG).show();
                        }else if (p_et_sat_s==p_et_sat_e) {
                            et_sat_s.setSelection(second_last);
                            et_sat_e.setSelection(last);
                        } else {
                            Map<String, Object> map = new HashMap<>();
                            map.put("Monday_start_time", et_mon_s.getSelectedItem().toString().trim());
                            map.put("Tuesday_start_time", et_tue_s.getSelectedItem().toString().trim());
                            map.put("Wednesday_start_time", et_wed_s.getSelectedItem().toString().trim());
                            map.put("Thursday_start_time", et_thur_s.getSelectedItem().toString().trim());
                            map.put("Friday_start_time", et_fri_s.getSelectedItem().toString().trim());
                            map.put("Saturday_start_time", et_sat_s.getSelectedItem().toString().trim());

                            map.put("Monday_end_time", et_mon_e.getSelectedItem().toString().trim());
                            map.put("Tuesday_end_time", et_tue_e.getSelectedItem().toString().trim());
                            map.put("Wednesday_end_time", et_wed_e.getSelectedItem().toString().trim());
                            map.put("Thursday_end_time", et_thur_e.getSelectedItem().toString().trim());
                            map.put("Friday_end_time", et_fri_e.getSelectedItem().toString().trim());
                            map.put("Saturday_end_time", et_sat_e.getSelectedItem().toString().trim());

                            FirebaseDatabase.getInstance().getReference().child("trainers")
                                    .child(tmp_phone).child("Working_Time").updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialogPlus.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialogPlus.dismiss();
                                        }
                                    });
                        }
                    }
                });

            }
        });

        FirebaseDatabase.getInstance().getReference().child("trainers")
                .child(tmp_phone).child("Working_Time").addValueEventListener(new ValueEventListener() {
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
        startActivity(new Intent(Set_Time.this, Trainer_Schedule.class));
        finish();
        super.onBackPressed();
    }
}