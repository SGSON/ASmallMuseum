package sg.asmallmuseum.presentation.UserProfile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import sg.asmallmuseum.Domain.Values;

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
            bundle.putString(Values.USER_POST_FIELD, Values.USER_POST);
        } else if (position == 1){
            bundle.putString(Values.USER_POST_FIELD, Values.USER_REVIEW);
        } else {
            bundle.putString(Values.USER_POST_FIELD, Values.USER_LIKE);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return MAX_NUM_PAGE;
    }


}
