package www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Membership;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Client_ViewPage_Adapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> client_fragmentArrayList=new ArrayList<>();

    private final ArrayList<String> client_fragmentTitle=new ArrayList<>();

    public Client_ViewPage_Adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return client_fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return client_fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return client_fragmentTitle.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        client_fragmentArrayList.add(fragment);
        client_fragmentTitle.add(title);
    }

}
