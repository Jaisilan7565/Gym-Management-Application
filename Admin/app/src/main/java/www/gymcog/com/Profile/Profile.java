package www.gymcog.com.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import www.gymcog.com.Login;
import www.gymcog.com.MainActivity;
import www.gymcog.com.Payments.Payment;
import www.gymcog.com.R;
import www.gymcog.com.Utility.NetworkChangeListener;

public class Profile extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    BottomNavigationView nav_bar;
    Toolbar toolbar;
    TextView name,email_id,phone;
    String tmp_name,tmp_email,tmp_phone;

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
                    case R.id.payment:
                        startActivity(new Intent(getApplicationContext(), Payment.class));
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
                        intent.putExtra("name",name.getText().toString());
                        intent.putExtra("phone",phone.getText().toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    case R.id.logout_profile:
                        AlertDialog.Builder builder=new AlertDialog.Builder(Profile.this);
                        builder.setMessage("Do you want to Logout?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseAuth.getInstance().signOut();
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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        String email = currentUser.getEmail();

        int length_name=email.indexOf('@');
        String username=email.substring(0,length_name);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("admins").child(username);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tmp_name = String.valueOf(dataSnapshot.child("name").getValue());
                if(tmp_name.equals("") || tmp_name.equals("null")) { tmp_name=""; }
                tmp_email = String.valueOf(dataSnapshot.child("email_id").getValue());
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
        Ref.child("trainers").addValueEventListener(new ValueEventListener() {
            TextView trainer_count;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numOfTrainer = (int) dataSnapshot.getChildrenCount();
                trainer_count=findViewById(R.id.trainer_count);
                trainer_count.setText(String.valueOf(numOfTrainer));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                trainer_count.setText("Not Fetched");
            }
        });

        Ref.child("clients").addValueEventListener(new ValueEventListener() {
            TextView client_count;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numOfClient = (int) dataSnapshot.getChildrenCount();
                client_count=findViewById(R.id.client_count);
                client_count.setText(String.valueOf(numOfClient));
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