package www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Membership;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_MainActivity;
import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.MainActivity;
import www.gymcog.trainer_n_client.gymcog.Trainer.Membership_Plans.Membership_Plans;
import www.gymcog.trainer_n_client.gymcog.Trainer.Membership_Plans.Membership_ViewPager_Adapter;
import www.gymcog.trainer_n_client.gymcog.Trainer.Membership_Plans.Monthly_Plans;
import www.gymcog.trainer_n_client.gymcog.Trainer.Membership_Plans.Yearly_Plans;
import www.gymcog.trainer_n_client.gymcog.Utility.NetworkChangeListener;

public class Client_Membership extends AppCompatActivity {

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_membership);

        tabLayout=findViewById(R.id.client_tabLayout);
        viewPager=findViewById(R.id.client_viewPager);

        tabLayout.setupWithViewPager(viewPager);

        Membership_ViewPager_Adapter adapter=new Membership_ViewPager_Adapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        adapter.addFragment(new Client_Monthly_Plan(),"Monthly");
        adapter.addFragment(new Client_Yearly_Plans(),"Yearly");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Client_MainActivity.class));
        overridePendingTransition(0,0);
        finish();
        super.onBackPressed();
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