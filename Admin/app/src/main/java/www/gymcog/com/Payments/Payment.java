package www.gymcog.com.Payments;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

import www.gymcog.com.MainActivity;
import www.gymcog.com.Profile.Profile;
import www.gymcog.com.R;
import www.gymcog.com.Trainer.model;
import www.gymcog.com.Utility.NetworkChangeListener;

public class Payment extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    RecyclerView sal_recview;
    Salary_Adapter adapter;
    BottomNavigationView nav_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        nav_bar=findViewById(R.id.nav_bar1);
        nav_bar.setSelectedItemId(R.id.payment);
        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.payment:
                        return true;
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        sal_recview = findViewById(R.id.salary_recycler);
        sal_recview.setLayoutManager(new LinearLayoutManager(this));

                FirebaseRecyclerOptions<model> options =
                        new FirebaseRecyclerOptions.Builder<model>()
                                .setQuery(FirebaseDatabase.getInstance().getReference()
                                        .child("trainers"), model.class).build();

                adapter = new Salary_Adapter(options);
                sal_recview.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
        adapter.stopListening();
    }

    public void onBackPressed() {
        startActivity(new Intent(Payment.this, MainActivity.class));
        overridePendingTransition(0,0);
        finish();
        super.onBackPressed();
    }

}