package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArtViewActivity extends AppCompatActivity implements ManagerListener {
    private List<String[]> review;
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private Artwork artwork;
    private ArtworkManager manager;
    private ViewPager mPager;
    private ArtViewFragmentAdapter pagerAdapter;

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

        manager.getArtInfoById(intent.getStringExtra("DocPath"));

        review = new ArrayList<>();
        adapter = new ReviewAdapter(review);
        setReview();
        initReview();
    }

    private void initReview(){
        recyclerView = (RecyclerView)findViewById(R.id.art_scroll_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
                return artViewFragment.getUri().compareTo(t1.getUri());
            }
        });
        pagerAdapter.updateData(list);
        pagerAdapter.notifyDataSetChanged();
    }
}