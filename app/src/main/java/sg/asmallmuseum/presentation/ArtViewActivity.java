package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArtViewActivity extends AppCompatActivity implements ManagerListener, View.OnClickListener {
    private Artwork artwork;
    private ArtworkManager manager;
    private ViewPager mPager;
    private ViewPager mLargePager;
    private ArtViewFragmentAdapter pagerAdapter;
    private Button mEnlarge;
    private Button mClose;

    private List<String[]> review;
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_view);

        manager = new ArtworkManager();
        manager.setListener(this);
        Intent intent = getIntent();

        mPager = (ViewPager) findViewById(R.id.art_image_pager);
        pagerAdapter = new ArtViewFragmentAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

        mEnlarge = (Button) findViewById(R.id.art_button_pager);
        mEnlarge.setOnClickListener(this);
        mClose = (Button) findViewById(R.id.large_close);
        mClose.setOnClickListener(this);

        manager.getArtInfoById(intent.getStringExtra("DocPath"));

        review = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(review);
        setReview();
        initReview();
    }

    private void initReview(){
        recyclerView = (RecyclerView)findViewById(R.id.art_scroll_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewAdapter);
    }

    private void setReview(){
        String[] a = {};
        review.add(new String[]{"asdasd", "2021-01-05", "3", "qbfwghilwudfbqliwdbulvcdjqbudrvbqiuwbviqblurbqilugdblvqbuvblquvulqbqeufbvlquievbluebvlqbulefbuvliqebifl"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"accacaac", "2019-7-23", "5", "fqenodnaflkjbnljbnpoauibhvaho;4eq598749846qfeqfqevq4e87465135vdacv"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});

    }

    @Override
    public void onDownloadCompleteListener(List<Artwork> artworks) {
        List<Artwork> artList = artworks;
        artwork = artworks.get(0);
        List<ArtViewFragment> m = new ArrayList<>();

        List<StorageReference> refs = manager.getArtImages(artwork.getaType(), artwork.getaFileLoc());
        for (int i = 0 ; i < refs.size() ; i++){
            refs.get(i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    ArtViewFragment fragment = new ArtViewFragment();
                    fragment.setImage(uri);
                    m.add(fragment);

                    if(m.size() == refs.size()){
                        imageLoadFinished(m);
                    }
                }
            });
        }

        ((TextView) findViewById(R.id.art_title)).setText(artwork.getaTitle());
        ((TextView) findViewById(R.id.art_author)).setText(artwork.getaAuthor());
        ((TextView) findViewById(R.id.art_desc)).setText(artwork.getaDesc());
        ((RatingBar) findViewById(R.id.art_rating)).setRating(artwork.getaRating());

    }

    @Override
    public void onUploadCompleteListener(boolean status) {
        //has to be empty
    }

    private void imageLoadFinished(List<ArtViewFragment> list){
        list.sort(new Comparator<ArtViewFragment>() {
            @Override
            public int compare(ArtViewFragment artViewFragment, ArtViewFragment t1) {
                return artViewFragment.getUriAsString().compareTo(t1.getUriAsString());
            }
        });
        pagerAdapter.updateData(list);
        pagerAdapter.notifyDataSetChanged();

        //Log.d("POSITION: ", pagerAdapter.getItemPosition(mPager.getCurrentItem()));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Log.d("CLICKED:","CLICKED:"+id);


        if (id == R.id.art_button_pager){
            if (mLargePager == null){
                setLargeView();
            }
            viewLargeImage();
        }
        else if (id == R.id.large_close){
            ConstraintLayout largeView = (ConstraintLayout) findViewById(R.id.art_large_view);
            largeView.setVisibility(View.INVISIBLE);
        }



    }

    private void setLargeView(){
        ConstraintLayout largeView = (ConstraintLayout) findViewById(R.id.art_large_view);
        largeView.setBackgroundColor(Color.BLACK);
        mLargePager = (ViewPager) findViewById(R.id.large_view_pager);
        ArtViewFragmentAdapter largePagerAdapter = new ArtViewFragmentAdapter(getSupportFragmentManager());
        mLargePager.setAdapter(largePagerAdapter);

        List<ArtViewFragment> data = copyFragments();

        largePagerAdapter.updateData(data);
        largePagerAdapter.notifyDataSetChanged();
    }

    private void viewLargeImage(){
        ConstraintLayout largeView = (ConstraintLayout) findViewById(R.id.art_large_view);

        int currPos = mPager.getCurrentItem();
        mLargePager.setCurrentItem(currPos);

        largeView.setVisibility(View.VISIBLE);
    }

    private List<ArtViewFragment> copyFragments(){
        List<ArtViewFragment> list = new ArrayList<>();
        List<ArtViewFragment> currData = pagerAdapter.getData();

        for (int i = 0 ; i < currData.size() ; i++){
            ArtViewFragment newFragment = new ArtViewFragment();
            newFragment.setImage(currData.get(i).getUri());
            list.add(newFragment);
        }

        return list;
    }
}
