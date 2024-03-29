package xyz.asmallmuseum.android.presentation.ArtView;

import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ArtViewPagerAdapter extends FragmentStateAdapter {
    private List<Uri> mList;
    private List<ArtViewImageFragment> mFragmentList;
    private boolean zoomable;

    public ArtViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
        mList = new ArrayList<>();
        mFragmentList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ArtViewImageFragment fragment = new ArtViewImageFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("Position", position);

        fragment.setImage(mList.get(position));
        fragment.setZoomable(zoomable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<Uri> mList, boolean zoomable){
        this.mList = mList;
        this.zoomable = zoomable;
        this.notifyDataSetChanged();
    }

}
