package sg.asmallmuseum.presentation.UserProfile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class UserHistoryViewPagerAdapter extends FragmentStateAdapter {

    private final int MAX_NUM_PAGE = 3;

    public UserHistoryViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new UserHistoryPostFragment();
        Bundle bundle = new Bundle();
        if (position == 0) {
            bundle.putString("Type", "Posts");
        } else if (position == 1){
            bundle.putString("Type", "Reviews");
        } else {
            bundle.putString("Type", "Like");
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return MAX_NUM_PAGE;
    }


}
