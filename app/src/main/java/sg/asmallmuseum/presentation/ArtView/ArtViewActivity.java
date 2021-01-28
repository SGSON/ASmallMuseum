package sg.asmallmuseum.presentation.ArtView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.General.ManagerListener;
import sg.asmallmuseum.presentation.General.MenuEvents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private Button mMore;
//    private RecyclerView mPreview;
    private TextView mNumPages;
    private int numImages;
    private boolean isExpand;

    private List<String[]> review;
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;

    private boolean signedIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_view);

        mAuth = FirebaseAuth.getInstance();

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

        mMore = (Button) findViewById(R.id.art_more_button);
        mMore.setOnClickListener(this);

        Button mExpand = (Button) findViewById(R.id.art_expand_button);
        mExpand.setOnClickListener(this);
        isExpand = true;

        manager.getSingleArtInfoByPath(intent.getStringExtra("DocPath"));

        review = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(review);
        setReview();
        initReview();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            signedIn = false;
        }
        else {
            signedIn = true;
        }
    }

    private void initReview(){
        recyclerView = (RecyclerView)findViewById(R.id.art_scroll_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewAdapter);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //Log.d("X:", ""+dy);
//                resizeLayout(dy);
//            }
//        });
    }

    private void resizeLayout(){
        ConstraintLayout descLayout = (ConstraintLayout) findViewById(R.id.art_desc_view);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) descLayout.getLayoutParams();

        Button button = (Button) findViewById(R.id.art_expand_button);

        Log.d("X:", "Clicked");

        if (isExpand){
            params.height = descLayout.getMinHeight();
            button.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.collapse, getTheme()));
        }
        else {
            params.height = descLayout.getMaxHeight();
            button.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.expand, getTheme()));
        }

        isExpand = !isExpand;
        descLayout.setLayoutParams(params);
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
        List<ArtViewFragment> fragmentList = new ArrayList<>();

        List<StorageReference> refs = manager.getArtImages(artwork.getaType(), artwork.getaFileLoc());
        for (int i = 0 ; i < refs.size() ; i++){
            refs.get(i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    ArtViewFragment fragment = new ArtViewFragment();
                    fragment.setImage(uri);
                    fragmentList.add(fragment);

                    if(fragmentList.size() == refs.size()){
                        imageLoadFinished(fragmentList);
                    }
                }
            });
        }

        ((TextView) findViewById(R.id.art_title)).setText(artwork.getaTitle());
        ((TextView) findViewById(R.id.art_author)).setText(artwork.getaAuthor());
        ((TextView) findViewById(R.id.art_desc)).setText(artwork.getaDesc());
        ((RatingBar) findViewById(R.id.art_rating)).setRating(artwork.getaRating());

        numImages = refs.size();
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
        else if(id == R.id.art_more_button){
            createPopupWindow();
        }
        else if(id == R.id.art_expand_button){
            resizeLayout();
        }

    }

    private void createPopupWindow(){
        PopupMenu popupMenu = new PopupMenu(this, mMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_enable, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(ArtViewActivity.this, "a"+menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popupMenu.show();
    }

    /***Enlarge View Methods***/
    //Init large view
    private void setLargeView(){
        //Set ViewPager
        ConstraintLayout largeView = (ConstraintLayout) findViewById(R.id.art_large_view);
        mLargePager = (ViewPager) findViewById(R.id.large_view_pager);
        ArtViewFragmentAdapter largePagerAdapter = new ArtViewFragmentAdapter(getSupportFragmentManager());
        mLargePager.setAdapter(largePagerAdapter);

        List<ArtViewFragment> data = copyFragments();

        largePagerAdapter.updateData(data);
        largePagerAdapter.notifyDataSetChanged();

        //Set Preview in Iteration 2
//        mPreview = (RecyclerView) findViewById(R.id.large_preview);
//        ArtViewPreviewAdapter mPreviewAdapter = new ArtViewPreviewAdapter(getUriList());
//
//        mPreview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        mPreview.setHorizontalScrollBarEnabled(true);
//        mPreview.setAdapter(mPreviewAdapter);

        //Set number of pages and current page number
        mNumPages = (TextView) findViewById(R.id.large_num_pages);
        mLargePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String text = (position+1) + " / " + data.size();
                mNumPages.setText(text);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    //Shows large view
    private void viewLargeImage(){
        ConstraintLayout largeView = (ConstraintLayout) findViewById(R.id.art_large_view);

        int currPos = mPager.getCurrentItem();
        mLargePager.setCurrentItem(currPos);

        String setNum = (currPos+1) + " / " + numImages;
        mNumPages.setText(setNum);

//        mPreview.requestFocus(currPos);
//        mPreview.scrollToPosition(currPos);

        largeView.setVisibility(View.VISIBLE);
    }

    //Copy the data list for large view pager
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

    private List<Uri> getUriList(){
        List<Uri> list = new ArrayList<>();
        List<ArtViewFragment> currData = pagerAdapter.getData();

        for (int i = 0 ; i < currData.size() ; i++){
            list.add(currData.get(i).getUri());
        }

        return list;
    }

    /***
     * Top-bar events
     * ***/
    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /***Move to the main activity***/
    public void onMainButtonPressed(View view) {
        makeText("Pressed Main Button");
    }

    /***Open the menu window***/
    public void onMenuButtonPressed(View view) {
        makeText("Pressed Menu Button");

        //Configure the main menu
        MenuEvents menuEvents = new MenuEvents(mAuth, this);
        menuEvents.openMenu(signedIn);
    }

    /***Nothing***/
    public void onBackButtonPressed(View view) {
        finish();
    }
}
