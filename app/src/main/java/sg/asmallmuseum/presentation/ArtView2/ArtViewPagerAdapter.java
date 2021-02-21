package sg.asmallmuseum.presentation.ArtView2;

import android.net.Uri;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import sg.asmallmuseum.presentation.ArtView.ArtViewFragment;

public class ArtViewPagerAdapter extends FragmentStateAdapter {
    private List<Uri> mList;

    public ArtViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ArtViewFragment fragment = new ArtViewFragment();
        fragment.setImage(mList.get(position));
        return fragment;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<Uri> mList){
        this.mList = mList;
        this.notifyDataSetChanged();
    }
}
