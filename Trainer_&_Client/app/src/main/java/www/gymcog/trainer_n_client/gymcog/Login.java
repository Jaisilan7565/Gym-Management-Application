package www.gymcog.trainer_n_client.gymcog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_MainActivity;
import www.gymcog.trainer_n_client.gymcog.Trainer.MainActivity;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Login extends AppCompatActivity {
//public static String PREFS_NAME="MyPrefsFile";
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    EditText phone, password;
    Button login_btn;
    ImageView eye;
    LottieAnimationView progress_bar;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

//    DatabaseReference loginDBref=FirebaseDatabase.getInstance().getReference();
    SharedPreferences mPrefs;

    String role="", user_phone="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        progress_bar = findViewById(R.id.progress_bar);

        eye=findViewById(R.id.eye);
        eye.setImageResource(R.drawable.baseline_visibility_off);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.baseline_visibility_off);
                }else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye.setImageResource(R.drawable.baseline_visibility);
                }
            }
        });

        mPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (mPrefs.getBoolean("loggedIn", false)) {
            String role = mPrefs.getString("role", "");
            if (role.equals("trainer")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (role.equals("client")) {
                Intent intent = new Intent(this, Client_MainActivity.class);
                startActivity(intent);
                finish();
            }
        }


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String temp_phone = phone.getText().toString().trim();
                final String temp_password = password.getText().toString().trim();

                if (temp_phone.isEmpty() && temp_password.isEmpty()) {
                    Toast.makeText(Login.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                }

                if (temp_phone.isEmpty()) {
                    phone.setError("Please Enter the Mobile Number.");
                    phone.requestFocus();
                } else if (!temp_phone.matches("^[789]\\d{9}$")) {
                    phone.setError("Enter a Valid Mobile Number.");
                    phone.requestFocus();
                } else if (temp_password.isEmpty()) {
                    password.setError("Please Enter the Password.");
                    password.requestFocus();
                }  else if (!temp_password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
                    password.setError("Min 8 Characters, Including Alphabet & Number.");
                    password.requestFocus();
                }  else {
                    progress_bar.playAnimation();

                    reference.child("trainers").child(temp_phone).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String getPass= (String) snapshot.child("pass").getValue();
                            if (temp_password.equals(getPass)){
                                    Toast.makeText(Login.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                    role="trainer";
                                    user_phone=temp_phone;

                                SharedPreferences.Editor editor = mPrefs.edit();
                                editor.putBoolean("loggedIn", true);
                                editor.putString("role", role);
                                editor.putString("user_phone", user_phone);
                                editor.apply();

                                progress_bar.pauseAnimation();
                                } else {
                                reference.child("clients").child(temp_phone).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String getPass= (String) snapshot.child("pass").getValue();
                                        if (temp_password.equals(getPass)){
                                            Toast.makeText(Login.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Login.this, Client_MainActivity.class));
                                            finish();
                                            role="client";
                                            user_phone=temp_phone;

                                            SharedPreferences.Editor editor = mPrefs.edit();
                                            editor.putBoolean("loggedIn", true);
                                            editor.putString("role", role);
                                            editor.putString("user_phone", user_phone);
                                            editor.apply();

                                            progress_bar.pauseAnimation();
                                        } else {
                                            Toast.makeText(Login.this, "Invalid Credentials.", Toast.LENGTH_SHORT).show();
                                            progress_bar.pauseAnimation();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(Login.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Login.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

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

}