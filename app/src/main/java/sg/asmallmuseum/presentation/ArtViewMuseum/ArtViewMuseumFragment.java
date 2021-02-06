package sg.asmallmuseum.presentation.ArtViewMuseum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import sg.asmallmuseum.R;

public class ArtViewMuseumFragment extends Fragment {

    private TextView mTitle;
    private TextView mAuthor;
    private TextView mDesc;
    private Button mExpand;
    private Button mExpandInfo;
    private Button mExtra;
    private RatingBar mRating;

    public ArtViewMuseumFragment() {
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
        View view = inflater.inflate(R.layout.fragment_art_view_museum, container, false);

        mTitle = (TextView) view.findViewById(R.id.fragment_art_title);
        mAuthor = (TextView) view.findViewById(R.id.fragment_art_author);
        mDesc = (TextView) view.findViewById(R.id.fragment_art_desc);
        mExpand = (Button) view.findViewById(R.id.fragment_art_button_pager);
        mExpandInfo = (Button) view.findViewById(R.id.fragment_art_expand_button);
        mExtra = (Button) view.findViewById(R.id.fragment_art_more_button);
        mRating = (RatingBar) view.findViewById(R.id.fragment_art_rating);

        return view;
    }
}