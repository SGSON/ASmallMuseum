package sg.asmallmuseum.presentation.ArtViewMuseum;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.viewpager.widget.ViewPager;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.General.ManagerListener;

public class ArtViewMuseumFragment extends Fragment implements View.OnClickListener, ManagerListener {

    private View view;

    private TextView mTitle;
    private TextView mAuthor;
    private TextView mDesc;
    private Button mExpand;
    private Button mExpandInfo;
    private Button mExtra;
    private RatingBar mRating;
    private ViewPager mViewPager;

    private ArtworkManager manager;
    private boolean isExpand;

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
        view = inflater.inflate(R.layout.fragment_art_view_museum, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.fragment_art_view);
        mTitle = (TextView) view.findViewById(R.id.fragment_art_title);
        mAuthor = (TextView) view.findViewById(R.id.fragment_art_author);
        mDesc = (TextView) view.findViewById(R.id.fragment_art_desc);
        mExpand = (Button) view.findViewById(R.id.fragment_art_button_pager);
        mExpandInfo = (Button) view.findViewById(R.id.fragment_art_expand_button);
        mExtra = (Button) view.findViewById(R.id.fragment_art_more_button);
        mRating = (RatingBar) view.findViewById(R.id.fragment_art_rating);

        mExpand.setOnClickListener(this);
        mExpandInfo.setOnClickListener(this);
        mExtra.setOnClickListener(this);

        isExpand = true;

        manager = new ArtworkManager();
        manager.setListener(this);
        manager.getMuseumArtList("서울시립미술관");

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_art_expand_button){
            replaceInfoLayout();
        }

    }

    private void replaceInfoLayout(){
        ConstraintLayout descLayout = view.findViewById(R.id.fragment_art_desc_view);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) descLayout.getLayoutParams();

        Button button = (Button) view.findViewById(R.id.art_expand_button);

        if (isExpand){
            params.height = descLayout.getMinHeight();
            button.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.collapse, getActivity().getTheme()));
        }
        else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            button.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.expand, getActivity().getTheme()));
        }

        isExpand = !isExpand;
        descLayout.setLayoutParams(params);
    }

    @Override
    public void onDownloadCompleteListener(List<Artwork> artworks) {

    }

    @Override
    public void onUploadCompleteListener(boolean status) {

    }
}