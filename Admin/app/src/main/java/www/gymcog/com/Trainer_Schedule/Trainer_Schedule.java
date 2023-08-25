package www.gymcog.com.Trainer_Schedule;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import www.gymcog.com.MainActivity;
import www.gymcog.com.R;
import www.gymcog.com.Trainer.model;
import www.gymcog.com.Utility.NetworkChangeListener;

public class Trainer_Schedule extends AppCompatActivity {
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    RecyclerView recview;
    TrainerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_schedule);
            recview=(RecyclerView)findViewById(R.id.trainer_recycler);
            recview.setLayoutManager(new LinearLayoutManager(this));

            FirebaseRecyclerOptions<model> options =
                    new FirebaseRecyclerOptions.Builder<model>()
                            .setQuery(FirebaseDatabase.getInstance().getReference()
                                    .child("trainers"), model.class).build();

            adapter=new TrainerAdapter(options);
            recview.setAdapter(adapter);

        }

        public void onBackPressed() {
            startActivity(new Intent(Trainer_Schedule.this, MainActivity.class));
            finish();
            super.onBackPressed();
        }

        @Override
        protected void onStart() {
            super.onStart();
            IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkChangeListener,filter);
            adapter.notifyDataSetChanged();
            adapter.startListening();
        }

        @Override
        protected void onStop() {
            super.onStop();
            unregisterReceiver(networkChangeListener);
            adapter.stopListening();
        }
}
