package www.gymcog.com;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import www.gymcog.com.Utility.NetworkChangeListener;


public class Login extends AppCompatActivity {


    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    TextView forgot_btn;
    EditText email, password;
    Button login_btn;
    ImageView eye;
    LottieAnimationView progress_bar;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        forgot_btn = findViewById(R.id.forgot_btn);
        progress_bar = findViewById(R.id.progress_bar);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

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

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String temp_email = email.getText().toString().trim();
                final String temp_password = password.getText().toString().trim();

                if (temp_email.isEmpty() && temp_password.isEmpty()) {
                    Toast.makeText(Login.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                }

                if (temp_email.isEmpty()) {
                    email.setError("Please Enter the Email.");
                    email.requestFocus();
                } else if (!temp_email.matches("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}")) {
                    email.setError("Enter a Valid Email Address.");
                    email.requestFocus();
                } else if (temp_password.isEmpty()) {
                    password.setError("Please Enter the Password.");
                    password.requestFocus();
                } else {
                    progress_bar.playAnimation();
                    fAuth.signInWithEmailAndPassword(temp_email, temp_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                int length_name=temp_email.indexOf('@');
                                String username=temp_email.substring(0,length_name);
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference refrence = FirebaseDatabase.getInstance().getReference()
                                        .child("admins").child(username);

                                    refrence.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String tmp_useremail = String.valueOf(dataSnapshot.child("email_id").getValue());
                                            if (tmp_useremail != temp_email) {
                                                Map<String, Object> map = new HashMap<>();
                                                map.put("email_id", temp_email);
                                                refrence.updateChildren(map);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();
                                progress_bar.pauseAnimation();
                            } else if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Invalid Email or Password.", Toast.LENGTH_SHORT).show();
                                progress_bar.pauseAnimation();
                            }
                        }
                    });
                }

            }
        });

        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_email = email.getText().toString().trim();

                if (temp_email.isEmpty()) {
                    Toast.makeText(getApplication(), "Enter Your Registered Email Address.", Toast.LENGTH_SHORT).show();
                    email.setError("Please Enter the Email Address.");
                    email.requestFocus();
                }else if (!temp_email.matches("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}")) {
                    email.setError("Enter a Valid Email Address.");
                    email.requestFocus();
                }else {
                    progress_bar.playAnimation();

                    fAuth.sendPasswordResetEmail(temp_email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Login.this, "Failed to send reset email, Enter a valid email.", Toast.LENGTH_SHORT).show();
                                    }
                                    progress_bar.pauseAnimation();
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