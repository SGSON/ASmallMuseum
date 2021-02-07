package sg.asmallmuseum.presentation.ArtViewMuseum;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ArtViewMuseumPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mData;

    public ArtViewMuseumPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public void addFragment(Fragment fragment){
        mData.add(fragment);
    }
}
