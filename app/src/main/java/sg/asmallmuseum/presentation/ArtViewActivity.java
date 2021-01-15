package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ArtViewActivity extends AppCompatActivity implements ManagerListener {
    private List<String[]> review;
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private Artwork artwork;
    private ArtworkManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_view);

        manager = new ArtworkManager();
        manager.setListener(this);
        Intent intent = getIntent();

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

        StorageReference ref = manager.getArtImages(artwork.getaType(), artwork.getaFileLoc());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ArtViewActivity.this).load(uri).into((ImageView) findViewById(R.id.art_image));
            }
        });

        ((TextView) findViewById(R.id.art_title)).setText(artwork.getaTitle());
        ((TextView) findViewById(R.id.art_author)).setText(artwork.getaAuthor());
        ((TextView) findViewById(R.id.art_desc)).setText(artwork.getaDesc());
        ((RatingBar) findViewById(R.id.art_rating)).setRating(artwork.getaRating());

    }

    @Override
    public void onUploadCompleteListener(boolean status) {
        //has to be empty
    }
}