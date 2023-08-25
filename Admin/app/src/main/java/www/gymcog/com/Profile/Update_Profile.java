package www.gymcog.com.Profile;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import www.gymcog.com.R;
import www.gymcog.com.Utility.NetworkChangeListener;

public class Update_Profile extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    TextView msg;
    EditText et_name, et_phone, current_pass, new_pass, confirm_new_pass;
    Button authentication, pass_change_btn, profile_update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();

        int length_name = email.indexOf('@');
        String username = email.substring(0, length_name);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("admins").child(username);

        msg = findViewById(R.id.msg);
        et_name = findViewById(R.id.et_profile_name);
        et_phone = findViewById(R.id.et_phone_no);
        current_pass = findViewById(R.id.et_current_pass);
        new_pass = findViewById(R.id.et_new_pass);
        confirm_new_pass = findViewById(R.id.et_confirm_new_pass);
        authentication = findViewById(R.id.authenticate);
        pass_change_btn = findViewById(R.id.pass_change_btn);
        profile_update_btn = findViewById(R.id.profile_update_btn);

        et_name.setText(getIntent().getStringExtra("name"));
        et_phone.setText(getIntent().getStringExtra("phone"));

        profile_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().trim().isEmpty()) {
                    et_name.setError("Please Enter the Name.");
                    et_name.requestFocus();
                } else if (!et_name.getText().toString().trim().matches("^[a-zA-Z ]*$")) {
                    et_name.setError("Only Alphabets Allowed.");
                    et_name.requestFocus();
                } else if (et_phone.getText().toString().trim().isEmpty()) {
                    et_phone.setError("Please Enter the Mobile Number.");
                    et_phone.requestFocus();
                } else if (!et_phone.getText().toString().trim().matches("^[789]\\d{9}$")) {
                    et_phone.setError("Enter a Valid Mobile Number.");
                    et_phone.requestFocus();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", et_name.getText().toString().trim());
                    map.put("phone", et_phone.getText().toString().trim());

                    reference.updateChildren(map)
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

                FirebaseUser firebaseUser = fAuth.getCurrentUser();
                String user_current_pass = current_pass.getText().toString().trim();
                if (user_current_pass.isEmpty()) {
                    current_pass.setError("Please Enter the Current Password.");
                    current_pass.requestFocus();
                } else if (!user_current_pass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                    current_pass.setError("Enter a Valid Password.");
                    current_pass.requestFocus();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), user_current_pass);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
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
                                        String user_confirm_new_pass=confirm_new_pass.getText().toString().trim();
                                        if (user_new_pass.isEmpty()) {
                                            new_pass.setError("Please Enter a Password.");
                                            new_pass.requestFocus();
                                        } else if (!user_new_pass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                                            new_pass.setError("Min 8 Characters, Including Alphabet & Number.");
                                            new_pass.requestFocus();
                                        }else if (!user_confirm_new_pass.equals(user_new_pass)){
                                            confirm_new_pass.setError("Password Doesn't Match.");
                                            confirm_new_pass.requestFocus();
                                        }else {
                                            firebaseUser.updatePassword(user_new_pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(Update_Profile.this, "Password has been Updated!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Update_Profile.this, Profile.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(Update_Profile.this, "Some Error Occurred.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Update_Profile.this, Profile.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Update_Profile.this, "Enter a Valid Password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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