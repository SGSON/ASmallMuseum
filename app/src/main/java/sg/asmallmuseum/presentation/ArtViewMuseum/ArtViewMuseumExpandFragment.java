package sg.asmallmuseum.presentation.ArtViewMuseum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;
import sg.asmallmuseum.R;


public class ArtViewMuseumExpandFragment extends Fragment {

    private Button mClose;
    private TextView mPages;
    private ViewPager mViewPager;

    public ArtViewMuseumExpandFragment() {
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
        View view = inflater.inflate(R.layout.fragment_art_view_museum_expand, container, false);

        mClose = (Button) view.findViewById(R.id.fragment_large_close);
        mPages = (TextView) view.findViewById(R.id.fragment_large_num_pages);
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_large_view_pager);

        ArtViewMuseumPagerAdapter adapter = new ArtViewMuseumPagerAdapter(getActivity().getSupportFragmentManager(), 0);
        mViewPager.setAdapter(adapter);
        return view;
    }
}