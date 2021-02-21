package sg.asmallmuseum.presentation.ArtView2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.ArtView.ArtViewFragment;
import sg.asmallmuseum.presentation.General.ManagerListener;

public class ArtView2Fragment extends Fragment implements View.OnClickListener, ManagerListener {
    private View view;
    private ArtView2ViewModel viewModel;

    private boolean isExpanded;
    private ArtworkManager manager;
    private int numImages;

    public ArtView2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        view = inflater.inflate(R.layout.fragment_art_view2, container, false);

        manager = new ArtworkManager();
        manager.setListener(this);

        setButtons();
        getArtInfo();
        isExpanded = true;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ArtView2ViewModel.class);
    }

    private void setButtons(){
        Button mExpandTitle = (Button) view.findViewById(R.id.fragment_art_expand_button);
        Button mMore = (Button) view.findViewById(R.id.fragment_art_more_button);
        Button mExpand = (Button) view.findViewById(R.id.fragment_art_pager_expand_button);

        mExpandTitle.setOnClickListener(this);
        mExpand.setOnClickListener(this);
        mMore.setOnClickListener(this);
    }

    private void setViewPager(List<Uri> mList){
        //run when image loads finished.

        ViewPager2 viewPager = (ViewPager2) view.findViewById(R.id.fragment_art_image_pager);
        ArtViewPagerAdapter adapter = new ArtViewPagerAdapter(this);
        adapter.setData(mList);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.fragment_art_expand_button){
            resizeDescLayout();
        }
        else if (id == R.id.fragment_art_more_button){

        }
        else if (id == R.id.fragment_art_pager_expand_button){
            if (getActivity() instanceof ArtView2Activity){
                ((ArtView2Activity) getActivity()).replaceFragment(this);
            }
        }
    }

    private void resizeDescLayout(){
        ConstraintLayout descLayout = (ConstraintLayout) view.findViewById(R.id.fragment_art_desc_view);
        ConstraintLayout titleLayout = (ConstraintLayout)view.findViewById(R.id.fragment_art_title_layout);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) descLayout.getLayoutParams();
        ConstraintLayout.LayoutParams titleParams = (ConstraintLayout.LayoutParams) titleLayout.getLayoutParams();

        Button button = (Button) view.findViewById(R.id.fragment_art_expand_button);

        if (isExpanded){
            params.height = titleParams.height;
            button.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.collapse, getActivity().getTheme()));
        }
        else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            button.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.expand, getActivity().getTheme()));
        }

        isExpanded = !isExpanded;
        descLayout.setLayoutParams(params);
    }

    private void getArtInfo(){
        Intent intent = getActivity().getIntent();
        manager.getSingleArtInfoByPath(intent.getStringExtra("DocPath"));
    }

    @Override
    public void onDownloadCompleteListener(List<Artwork> artworks) {
        List<Artwork> mList = artworks;
        List<Uri> uriList = new ArrayList<>();
        Artwork artwork = artworks.get(0);

        if (!artwork.getaType().equals("Museums")){
            List<StorageReference> refs = manager.getArtImages(artwork.getaType(), artwork.getaFileLoc());
            for (int i = 0 ; i < refs.size() ; i++) {
                refs.get(i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uriList.add(uri);

                        if (uriList.size() == refs.size()) {
                            setViewPager(uriList);
                        }
                    }
                });
                numImages = refs.size();
            }
        }

        ((TextView) view.findViewById(R.id.fragment_art_title)).setText(artwork.getaTitle());
        ((TextView) view.findViewById(R.id.fragment_art_author)).setText(artwork.getaAuthor());
        ((TextView) view.findViewById(R.id.fragment_art_desc)).setText(artwork.getaDesc());
        ((RatingBar) view.findViewById(R.id.fragment_art_rating)).setRating(artwork.getaRating());
    }

    @Override
    public void onUploadCompleteListener(boolean status) {
        //empty
    }

    @Override
    public void onNumPostLoadComplete(int result) {
        //empty
    }
}