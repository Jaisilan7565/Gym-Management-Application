package www.gymcog.trainer_n_client.gymcog.Trainer.Trainee;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class adddata extends AppCompatActivity {
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    EditText name, phone, email, pass;
    Button submit;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddata);

        name = (EditText) findViewById(R.id.add_name);
        email = (EditText) findViewById(R.id.add_email);
        phone = (EditText) findViewById(R.id.add_phone);
        pass = (EditText) findViewById(R.id.add_password);

        back = (ImageButton) findViewById(R.id.add_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Trainee.class));
                finish();
            }
        });

        submit = (Button) findViewById(R.id.add_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processinsert();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(adddata.this, Trainee.class));
        finish();
        super.onBackPressed();
    }

    private void processinsert() {
        String temp_name = name.getText().toString().trim();
        String temp_phone = phone.getText().toString().trim();
        String temp_email = email.getText().toString().trim();
        String temp_pass = pass.getText().toString().trim();



        if (temp_email.isEmpty() && temp_name.isEmpty()
                && temp_phone.isEmpty() && temp_pass.isEmpty()) {
            Toast.makeText(this, "Fields are Empty", Toast.LENGTH_SHORT).show();
        }
        if (temp_name.isEmpty()) {
            name.setError("Please Enter the Name.");
            name.requestFocus();
        } else if (!temp_name.matches("^[a-zA-Z ]*$")) {
            name.setError("Only Alphabets Allowed.");
            name.requestFocus();
        } else if (temp_email.isEmpty()) {
            email.setError("Please Enter an Email Address.");
            email.requestFocus();
        } else if (!temp_email.matches("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}")) {
            email.setError("Enter a Valid Email Address.");
            email.requestFocus();
        } else if (temp_phone.isEmpty()) {
            phone.setError("Please Enter the Mobile Number.");
            phone.requestFocus();
        } else if (!temp_phone.matches("^[789]\\d{9}$")){
            phone.setError("Enter a Valid Mobile Number.");
            phone.requestFocus();
        }else if (temp_pass.isEmpty()) {
            pass.setError("Please Enter the Password.");
            pass.requestFocus();
        } else if (!temp_pass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")) {
            pass.setError("Min 8 Characters, Including Alphabet & Number.");
            pass.requestFocus();
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("name", temp_name);
            map.put("phone", temp_phone);
            map.put("email", temp_email);
            map.put("pass", temp_pass);
            FirebaseDatabase.getInstance().getReference().child("clients").child(temp_phone)
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            name.setText("");
                            phone.setText("");
                            email.setText("");
                            pass.setText("");
                            Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Trainee.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Could not insert", Toast.LENGTH_LONG).show();
                        }
                    });
        }
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