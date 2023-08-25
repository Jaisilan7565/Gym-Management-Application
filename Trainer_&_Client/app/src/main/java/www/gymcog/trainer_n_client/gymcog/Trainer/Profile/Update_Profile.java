package www.gymcog.trainer_n_client.gymcog.Trainer.Profile;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Update_Profile extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    TextView msg;
    EditText et_name, et_email, current_pass, new_pass, confirm_new_pass;
    Button authentication, pass_change_btn, profile_update_btn;
    String user_database_pass,tmp_email,tmp_name;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
            .child("trainers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String temp_phone = preferences.getString("user_phone", "");

        reference.child(temp_phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tmp_name = String.valueOf(dataSnapshot.child("name").getValue());
                if (tmp_name.equals("") || tmp_name.equals("null")) {
                    tmp_name = "";
                }
                tmp_email = String.valueOf(dataSnapshot.child("email").getValue());
                if (tmp_email.equals("") || tmp_email.equals("null")) {
                    tmp_email = "";
                }
                user_database_pass = String.valueOf(dataSnapshot.child("pass").getValue());
                if (user_database_pass.equals("") || user_database_pass.equals("null")) {
                    user_database_pass = "";
                }

                et_name.setText(tmp_name);
                et_email.setText(tmp_email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        msg = findViewById(R.id.msg);
        et_name = findViewById(R.id.et_profile_name);
        et_email = findViewById(R.id.et_email);
        current_pass = findViewById(R.id.et_current_pass);
        new_pass = findViewById(R.id.et_new_pass);
        confirm_new_pass = findViewById(R.id.et_confirm_new_pass);
        authentication = findViewById(R.id.authenticate);
        pass_change_btn = findViewById(R.id.pass_change_btn);
        profile_update_btn = findViewById(R.id.profile_update_btn);

        profile_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().trim().isEmpty()) {
                    et_name.setError("Please Enter the Name.");
                    et_name.requestFocus();
                } else if (!et_name.getText().toString().trim().matches("^[a-zA-Z ]*$")) {
                    et_name.setError("Only Alphabets Allowed.");
                    et_name.requestFocus();
                } else if (et_email.getText().toString().trim().isEmpty()) {
                    et_email.setError("Please Enter the Email Address.");
                    et_email.requestFocus();
                } else if (!et_email.getText().toString().trim().matches("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}")) {
                    et_email.setError("Enter a Valid Email Address.");
                    et_email.requestFocus();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", et_name.getText().toString().trim());
                    map.put("email", et_email.getText().toString().trim());

                    FirebaseDatabase.getInstance().getReference().child("trainers")
                            .child(temp_phone).updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Update_Profile.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Update_Profile.this, Profile.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Update_Profile.this, "Not Updated!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


        authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_current_pass = current_pass.getText().toString().trim();
                if (user_current_pass.isEmpty()) {
                    current_pass.setError("Please Enter the Current Password.");
                    current_pass.requestFocus();
                } else if (!user_current_pass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                    current_pass.setError("Enter a Valid Password.");
                    current_pass.requestFocus();
                } else {
                    if (user_current_pass.equals(user_database_pass)) {
                        new_pass.setVisibility(View.VISIBLE);
                        confirm_new_pass.setVisibility(View.VISIBLE);
                        pass_change_btn.setVisibility(View.VISIBLE);

                        current_pass.setVisibility(View.GONE);
                        authentication.setVisibility(View.GONE);
                        msg.setText("You have been Authenticated.");
                        Toast.makeText(Update_Profile.this,
                                "Password has been Verified!\n" +
                                        "You can Change Password Now.", Toast.LENGTH_LONG).show();

                        pass_change_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String user_new_pass = new_pass.getText().toString().trim();
                                String user_confirm_new_pass = confirm_new_pass.getText().toString().trim();
                                if (user_new_pass.isEmpty()) {
                                    new_pass.setError("Please Enter a Password.");
                                    new_pass.requestFocus();
                                } else if (!user_new_pass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                                    new_pass.setError("Min 8 Characters, Including Alphabet & Number.");
                                    new_pass.requestFocus();
                                } else if (!user_confirm_new_pass.equals(user_new_pass)) {
                                    confirm_new_pass.setError("Password Doesn't Match.");
                                    confirm_new_pass.requestFocus();
                                } else {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("pass", user_confirm_new_pass);
                                    reference.child(temp_phone).updateChildren(map);
                                    Toast.makeText(Update_Profile.this, "Password has been Updated!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Update_Profile.this, Profile.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(Update_Profile.this, "Enter a Valid Password.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Update_Profile.this, Profile.class);
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