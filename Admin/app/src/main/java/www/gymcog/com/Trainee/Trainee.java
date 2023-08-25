package www.gymcog.com.Trainee;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import www.gymcog.com.MainActivity;
import www.gymcog.com.R;
import www.gymcog.com.Trainer.model;
import www.gymcog.com.Utility.NetworkChangeListener;

public class Trainee extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    RecyclerView recview;
    Trainee_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee);

        setTitle("Search here..");

        recview=(RecyclerView)findViewById(R.id.trainee_recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("clients"), model.class)
                        .build();

        adapter = new Trainee_Adapter(options);
        recview.setAdapter(adapter);

    }

    public void onBackPressed() {
        startActivity(new Intent(Trainee.this, MainActivity.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.searchmenu,menu);

        MenuItem item=menu.findItem(R.id.search);

        SearchView searchView=(SearchView)item.getActionView();

        searchView.setQueryHint("Search by Phone No");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s) {

                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s)
    {
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("clients")
                                .orderByChild("phone").startAt(s).endAt(s+"\uf8ff"), model.class)
                        .build();

        adapter=new Trainee_Adapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }

}