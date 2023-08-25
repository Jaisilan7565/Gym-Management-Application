package www.gymcog.trainer_n_client.gymcog.Trainer.Feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.MainActivity;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.Trainee;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.model;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.myadapter;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Feedback extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    RecyclerView recview;
    Feedback_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        recview=(RecyclerView)findViewById(R.id.feedback_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recview.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Feedback_Model> options =
                new FirebaseRecyclerOptions.Builder<Feedback_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("feedbacks"), Feedback_Model.class)
                        .build();

        adapter = new Feedback_Adapter(options);
        recview.setAdapter(adapter);

    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
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