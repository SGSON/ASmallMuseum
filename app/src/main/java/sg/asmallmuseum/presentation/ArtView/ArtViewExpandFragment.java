package sg.asmallmuseum.presentation.ArtView;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import sg.asmallmuseum.R;


public class ArtViewExpandFragment extends Fragment implements View.OnClickListener{
    private View view;

    public ArtViewExpandFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_art_view_expand, container, false);

        Button mClose = (Button) view.findViewById(R.id.fragment_large_close);
        mClose.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArtViewViewModel viewModel = new ViewModelProvider(requireActivity()).get(ArtViewViewModel.class);
        viewModel.getUriList().observe(getViewLifecycleOwner(), new Observer<List<Uri>>() {
            @Override
            public void onChanged(List<Uri> uris) {
                viewModel.getmCurrentPage().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        setViewPager(uris, integer);
                    }
                });
            }
        });
    }

    private void setViewPager(List<Uri> mList, int currPage){
        ArtViewPagerAdapter adapter = new ArtViewPagerAdapter(this);
        adapter.setData(mList, true);
        ViewPager2 viewPager = (ViewPager2) view.findViewById(R.id.fragment_large_view_pager);

        viewPager.setAdapter(adapter);
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(currPage, true);
            }
        }, 100);

        String text = (currPage+1) + " / " + mList.size();
        setText(text);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                String text = (position+1) + " / " + mList.size();
                setText(text);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fragment_large_close){
            if (getActivity() instanceof ArtViewActivity){
                ((ArtViewActivity) getActivity()).replaceFragment(this);
            }
        }
    }

    private void setText(String text){
        TextView textview = (TextView) view.findViewById(R.id.fragment_large_num_pages);
        textview.setText(text);
    }
}