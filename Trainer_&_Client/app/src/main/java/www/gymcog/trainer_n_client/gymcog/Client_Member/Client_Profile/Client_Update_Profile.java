package www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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

import java.util.HashMap;
import java.util.Map;

import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.Profile.Profile;
import www.gymcog.trainer_n_client.gymcog.Trainer.Profile.Update_Profile;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Client_Update_Profile extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    TextView cmsg;
    EditText etc_name, etc_email, ccurrent_pass, cnew_pass, cconfirm_new_pass;
    Button cauthentication, cpass_change_btn, cprofile_update_btn;
    String cuser_database_pass,ctmp_email,ctmp_name;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
            .child("clients");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_update_profile);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String temp_phone = preferences.getString("user_phone", "");

        reference.child(temp_phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ctmp_name = String.valueOf(dataSnapshot.child("name").getValue());
                if (ctmp_name.equals("") || ctmp_name.equals("null")) {
                    ctmp_name = "";
                }
                ctmp_email = String.valueOf(dataSnapshot.child("email").getValue());
                if (ctmp_email.equals("") || ctmp_email.equals("null")) {
                    ctmp_email = "";
                }
                cuser_database_pass = String.valueOf(dataSnapshot.child("pass").getValue());
                if (cuser_database_pass.equals("") || cuser_database_pass.equals("null")) {
                    cuser_database_pass = "";
                }

                etc_name.setText(ctmp_name);
                etc_email.setText(ctmp_email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        cmsg = findViewById(R.id.cmsg);
        etc_name = findViewById(R.id.etc_profile_name);
        etc_email = findViewById(R.id.etc_email);
        ccurrent_pass = findViewById(R.id.etc_current_pass);
        cnew_pass = findViewById(R.id.etc_new_pass);
        cconfirm_new_pass = findViewById(R.id.etc_confirm_new_pass);
        cauthentication = findViewById(R.id.cauthenticate);
        cpass_change_btn = findViewById(R.id.cpass_change_btn);
        cprofile_update_btn = findViewById(R.id.cprofile_update_btn);

        cprofile_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etc_name.getText().toString().trim().isEmpty()) {
                    etc_name.setError("Please Enter the Name.");
                    etc_name.requestFocus();
                } else if (!etc_name.getText().toString().trim().matches("^[a-zA-Z ]*$")) {
                    etc_name.setError("Only Alphabets Allowed.");
                    etc_name.requestFocus();
                } else if (etc_email.getText().toString().trim().isEmpty()) {
                    etc_email.setError("Please Enter the Email Address.");
                    etc_email.requestFocus();
                } else if (!etc_email.getText().toString().trim().matches("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}")) {
                    etc_email.setError("Enter a Valid Email Address.");
                    etc_email.requestFocus();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", etc_name.getText().toString().trim());
                    map.put("email", etc_email.getText().toString().trim());

                    FirebaseDatabase.getInstance().getReference().child("clients")
                            .child(temp_phone).updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Client_Update_Profile.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Client_Update_Profile.this, Client_Profile.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Client_Update_Profile.this, "Not Updated!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        cauthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_current_pass = ccurrent_pass.getText().toString().trim();
                if (user_current_pass.isEmpty()) {
                    ccurrent_pass.setError("Please Enter the Current Password.");
                    ccurrent_pass.requestFocus();
                } else if (!user_current_pass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                    ccurrent_pass.setError("Enter a Valid Password.");
                    ccurrent_pass.requestFocus();
                } else {
                    if (user_current_pass.equals(cuser_database_pass)) {
                        cnew_pass.setVisibility(View.VISIBLE);
                        cconfirm_new_pass.setVisibility(View.VISIBLE);
                        cpass_change_btn.setVisibility(View.VISIBLE);

                        ccurrent_pass.setVisibility(View.GONE);
                        cauthentication.setVisibility(View.GONE);
                        cmsg.setText("You have been Authenticated.");
                        Toast.makeText(Client_Update_Profile.this,
                                "Password has been Verified!\n" +
                                        "You can Change Password Now.", Toast.LENGTH_LONG).show();

                        cpass_change_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String cuser_new_pass = cnew_pass.getText().toString().trim();
                                String cuser_confirm_new_pass = cconfirm_new_pass.getText().toString().trim();
                                if (cuser_new_pass.isEmpty()) {
                                    cnew_pass.setError("Please Enter a Password.");
                                    cnew_pass.requestFocus();
                                } else if (!cuser_new_pass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                                    cnew_pass.setError("Min 8 Characters, Including Alphabet & Number.");
                                    cnew_pass.requestFocus();
                                } else if (!cuser_confirm_new_pass.equals(cuser_new_pass)) {
                                    cconfirm_new_pass.setError("Password Doesn't Match.");
                                    cconfirm_new_pass.requestFocus();
                                } else {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("pass", cuser_confirm_new_pass);
                                    reference.child(temp_phone).updateChildren(map);
                                    Toast.makeText(Client_Update_Profile.this, "Password has been Updated!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Client_Update_Profile.this, Client_Profile.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(Client_Update_Profile.this, "Enter a Valid Password.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Client_Update_Profile.this, Client_Profile.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
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

}