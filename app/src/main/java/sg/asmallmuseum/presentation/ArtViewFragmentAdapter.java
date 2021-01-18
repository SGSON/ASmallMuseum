package sg.asmallmuseum.presentation;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ArtViewFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mData;

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

    public void setPages(Uri list){
        ArtImageFragment fragment = new ArtImageFragment();
        fragment.setImage(list);
        mData.add(fragment);

        /*for (int i = 0 ; i < list.size() ; i++){
            ArtImageFragment fragment = new ArtImageFragment();
            fragment.setImage(list.get(i));
            mData.add(fragment);
        }*/
        //notify();
    }

    public void addFragment(ArtImageFragment fragment){
        mData.add(fragment);
    }
}
