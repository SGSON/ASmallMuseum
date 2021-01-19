package sg.asmallmuseum.presentation;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ArtViewFragmentAdapter extends FragmentPagerAdapter {
    private List<ArtViewFragment> mData;

    public ArtViewFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
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

    public void addFragment(ArtViewFragment fragment){
        mData.add(fragment);
    }

    public void updateData(List<ArtViewFragment> list){
        mData = list;
    }
}
