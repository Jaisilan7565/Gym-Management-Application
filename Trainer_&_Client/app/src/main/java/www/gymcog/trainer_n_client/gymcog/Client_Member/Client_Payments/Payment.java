package www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Payments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Membership.Client_Membership;
import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Payment extends AppCompatActivity implements PaymentResultWithDataListener {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    TextView date;
    TextView amount, name, email, phone, member_type;
    Button send;

    String tmp_name, tmp_email, tmp_phone, currentDate,newDate;
    String tmp_pay_name, tmp_pay_email, tmp_pay_phone, tmp_pay_amount, tmp_member_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        date = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime());
        date.setText(currentDate);



        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String temp_phone = preferences.getString("user_phone", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("clients");


        name = findViewById(R.id.pay_name);
        email = findViewById(R.id.pay_email);
        phone = findViewById(R.id.pay_phone);
        amount = findViewById(R.id.pay_amount);
        member_type = findViewById(R.id.membership_type);

        member_type.setText(getIntent().getStringExtra("member_type"));
        amount.setText(getIntent().getStringExtra("amount"));

        reference.child(temp_phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tmp_name = String.valueOf(snapshot.child("name").getValue());
                if (tmp_name.equals("") || tmp_name.equals("null")) {
                    tmp_name = "";
                }
                tmp_email = String.valueOf(snapshot.child("email").getValue());
                if (tmp_email.equals("") || tmp_email.equals("null")) {
                    tmp_email = "";
                }
                tmp_phone = String.valueOf(snapshot.child("phone").getValue());
                if (tmp_phone.equals("") || tmp_phone.equals("null")) {
                    tmp_phone = "";
                }

                name.setText(tmp_name);
                email.setText(tmp_email);
                phone.setText(tmp_phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send = findViewById(R.id.buy_plan);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tmp_pay_name = name.getText().toString().trim();
                tmp_pay_email = email.getText().toString().trim();
                tmp_pay_phone = phone.getText().toString().trim();
                int a = Integer.parseInt(amount.getText().toString().trim());
                int actual_amount=a*100;
                tmp_pay_amount=String.valueOf(actual_amount);
                tmp_member_type = member_type.getText().toString().trim();
                makePayment();
            }
        });

    }

    private void makePayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_BDSopirxm2jCrN");
        checkout.setImage(R.drawable.logo);

        final Activity activity = this;

        try {

            JSONObject options = new JSONObject();
            options.put("name", "GymCog Membership");
            options.put("description", "Pay and use the Benefits of this Membership Plan.");
//            options.put("send_sms_hash",true);
//            options.put("allow_rotation", true);
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", tmp_pay_amount);

            JSONObject preFill = new JSONObject();
            preFill.put("email",tmp_pay_email );
            preFill.put("contact", tmp_pay_phone);
            options.put("prefill", preFill);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e(TAG, "Error in starting RazorPay Checkout", e);
        }
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
        startActivity(new Intent(this, Client_Membership.class));
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try{
            SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String temp_phone = preferences.getString("user_phone", "");
            String payment_Id=s;

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            Calendar ncalendar = Calendar.getInstance();
        if (tmp_member_type.equals("1 Month Platinum") ||
                tmp_member_type.equals("1 Month Gold") ||
                tmp_member_type.equals("1 Month Silver")){
        ncalendar.add(Calendar.DAY_OF_YEAR, 30);
        }
        else {
            ncalendar.add(Calendar.DAY_OF_YEAR, 365);
        }
            Date date = ncalendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            newDate = dateFormat.format(date);

            Map<String, Object> map1 = new HashMap<>();
            map1.put("plan_valid_till", newDate);
            map1.put("lastPlan", tmp_member_type);
            FirebaseDatabase.getInstance().getReference().child("clients")
                    .child(temp_phone).updateChildren(map1);

            Map<String, Object> map = new HashMap<>();
            map.put("name", tmp_pay_name);
            map.put("payment_id", payment_Id);
            map.put("payment_date", currentDate);
            map.put("amount", amount.getText().toString());
            map.put("membership_type", tmp_member_type);

            FirebaseDatabase.getInstance().getReference().child("client_payments")
                    .child(temp_phone).child(ts).updateChildren(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(Payment.this, "Paid Successfully !", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Payment.this, Client_Membership.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Payment.this, "Not Updated!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try{
//            alertDialogBuilder.setMessage("Payment Failed:\nPayment Data: "+paymentData.getData());
//            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}