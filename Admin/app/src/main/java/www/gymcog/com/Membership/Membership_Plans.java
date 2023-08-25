package www.gymcog.com.Membership;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import www.gymcog.com.MainActivity;
import www.gymcog.com.Payments.Payment;
import www.gymcog.com.R;
import www.gymcog.com.Utility.NetworkChangeListener;

public class Membership_Plans extends AppCompatActivity{
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_plans);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        Membership_ViewPager_Adapter adapter=new Membership_ViewPager_Adapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        adapter.addFragment(new Monthly_Plans(),"Monthly");
        adapter.addFragment(new Yearly_Plans(),"Yearly");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Membership_Plans.this, MainActivity.class));
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