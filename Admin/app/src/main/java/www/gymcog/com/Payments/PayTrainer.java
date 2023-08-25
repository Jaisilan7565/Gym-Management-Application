package www.gymcog.com.Payments;

import static www.gymcog.com.Utility.Common.isConnectedToInternet;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import www.gymcog.com.R;
import www.gymcog.com.Utility.NetworkChangeListener;

public class PayTrainer extends AppCompatActivity{

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    TextView date;
    EditText amount, name, upi_id;
    Button send;
    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_trainer);

        date=findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.LONG).format(calendar.getTime());
        date.setText(currentDate);

        name =findViewById(R.id.pay_name);
        name.setText(getIntent().getStringExtra("name"));
        upi_id=findViewById(R.id.pay_upi_id);
        upi_id.setText(getIntent().getStringExtra("upi_id"));
        amount=findViewById(R.id.pay_amount);
        amount.setText(getIntent().getStringExtra("salary"));

        send=findViewById(R.id.pay_trainer);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tmp_pay_name=name.getText().toString().trim();
                String tmp_pay_upi_id=upi_id.getText().toString().trim();
                String tmp_pay_amount=amount.getText().toString().trim();
                String tmp_pay_note="GymCog : Salary for this month is Paid on "+currentDate;

                payUsingUpi(tmp_pay_name, tmp_pay_upi_id, tmp_pay_amount,tmp_pay_note);
//                makePayment();
            }
        });

    }

//    private void makePayment() {
//        final String[] tmp_phone = new String[1];
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser= mAuth.getCurrentUser();
//        String email = currentUser.getEmail();
//
//        int length_name=email.indexOf('@');
//        String username=email.substring(0,length_name);
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
//                .child("admins").child(username);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                tmp_phone[0] = String.valueOf(dataSnapshot.child("phone").getValue());
//                if (tmp_phone[0].equals("") || tmp_phone[0].equals("null")) {
//                    tmp_phone[0] = "";
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        int sal= Integer.parseInt(model.getSalary())*100;
//        String salary= String.valueOf(sal);

//        Checkout checkout=new Checkout();
//        checkout.setKeyID("rzp_test_BDSopirxm2jCrN");
//        checkout.setImage(R.drawable.logo);
//
//        final Activity activity=this;
//
//        try {
//
//            JSONObject options = new JSONObject();
//            options.put("name", "GymCog Salary");
//            options.put("description", "Salary");
//            options.put("send_sms_hash",true);
//            options.put("allow_rotation", true);
//            options.put("theme.color","#3399cc");
//            options.put("currency", "INR");
//            options.put("amount", "100");
//
//            JSONObject preFill = new JSONObject();
//            preFill.put("email", "jaysheelannadar2003@gmail.com");
//            preFill.put("contact", "9292839389");
//            options.put("prefill", preFill);
//
//            checkout.open(activity, options);
//
//        }catch (Exception e){
//            Log.e(TAG,"Error in starting RazorPay Checkout",e);
//        }
//    }

    void payUsingUpi(String tmp_pay_name, String tmp_pay_upi_id, String tmp_pay_amount,String tmp_pay_note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pn", tmp_pay_name)
                .appendQueryParameter("pa", tmp_pay_upi_id)
                .appendQueryParameter("am", tmp_pay_amount)
                .appendQueryParameter("tn", tmp_pay_note)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PayTrainer.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String paymentStatus = data.getStringExtra("Status");
                Toast.makeText(this, "Payment successful: " + paymentStatus, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == UPI_PAYMENT) {
            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.d("UPI", "onActivityResult: " + trxt);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectedToInternet(PayTrainer.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String[] response = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String[] equalStr = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PayTrainer.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PayTrainer.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(PayTrainer.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PayTrainer.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
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

    public void onBackPressed() {
        startActivity(new Intent(PayTrainer.this, Payment.class));
        finishAffinity();
        super.onBackPressed();
    }

}