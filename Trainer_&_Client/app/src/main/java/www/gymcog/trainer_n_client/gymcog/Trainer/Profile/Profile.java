package www.gymcog.trainer_n_client.gymcog.Trainer.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Profile.Client_Profile;
import www.gymcog.trainer_n_client.gymcog.Login;
import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.MainActivity;
import www.gymcog.trainer_n_client.gymcog.Trainer.Work_Hours.Work_Hours;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Profile extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    BottomNavigationView nav_bar;
    Toolbar toolbar;
    TextView name,email_id,phone;
    String tmp_name,tmp_email,tmp_phone;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
            .child("trainers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name=findViewById(R.id.profile_name);
        email_id=findViewById(R.id.profile_email);
        phone=findViewById(R.id.profile_phone_no);
        toolbar=findViewById(R.id.prof_toolbar);

        nav_bar=findViewById(R.id.nav_bar2);
        nav_bar.setSelectedItemId(R.id.profile);

        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.working_hrs:
                        startActivity(new Intent(getApplicationContext(), Work_Hours.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.profile_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch(item.getItemId()) {
                    case R.id.update_profile:
                        Intent intent=new Intent(Profile.this, Update_Profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.logout_profile:
                        AlertDialog.Builder builder=new AlertDialog.Builder(Profile.this);
                        builder.setMessage("Do you want to Logout?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences preferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.clear();
                                        editor.apply();
                                        Intent intent_logout = new Intent(Profile.this, Login.class);
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
                        return true;
                }

                return false;
            }
        });

        count();

        SharedPreferences preferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String temp_phone = preferences.getString("user_phone", "");

        reference.child(temp_phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tmp_name = String.valueOf(dataSnapshot.child("name").getValue());
                if(tmp_name.equals("") || tmp_name.equals("null")) { tmp_name=""; }
                tmp_email = String.valueOf(dataSnapshot.child("email").getValue());
                if(tmp_email.equals("") || tmp_email.equals("null")) { tmp_email=""; }
                tmp_phone = String.valueOf(dataSnapshot.child("phone").getValue());
                if(tmp_phone.equals("") || tmp_phone.equals("null")) { tmp_phone=""; }

                name.setText(tmp_name);
                email_id.setText(tmp_email);
                phone.setText(tmp_phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void count(){
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.child("clients").addValueEventListener(new ValueEventListener() {
            TextView client_count;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numOfClient = (int) dataSnapshot.getChildrenCount();
                client_count=findViewById(R.id.client_count);
                client_count.setText(numOfClient + " Clients");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                client_count.setText("Not Fetched");
            }
        });

    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0, 0);
        finish();
        super.onBackPressed();
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

}