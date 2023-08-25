package www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Payments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_MainActivity;
import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.MainActivity;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.Trainee;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.model;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.myadapter;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Payment_History extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    RecyclerView pay_history_recview;
    Payment_History_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        pay_history_recview=(RecyclerView)findViewById(R.id.pay_history_recview);
//        pay_history_recview.setLayoutManager(new LinearLayoutManager(this));
//        layoutManager.setReverseLayout(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        pay_history_recview.setLayoutManager(layoutManager);

        SharedPreferences preferences =getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String temp_phone = preferences.getString("user_phone", "");

        FirebaseRecyclerOptions<payment_model> options =
                new FirebaseRecyclerOptions.Builder<payment_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("client_payments").child(temp_phone), payment_model.class)
                        .build();

        adapter = new Payment_History_Adapter(options);
        pay_history_recview.setAdapter(adapter);

    }

    public void onBackPressed() {
        startActivity(new Intent(this, Client_MainActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.notifyDataSetChanged();
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        unregisterReceiver(networkChangeListener);
    }

}