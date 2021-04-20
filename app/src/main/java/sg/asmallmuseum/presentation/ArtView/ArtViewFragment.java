package sg.asmallmuseum.presentation.ArtView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.RequestCode;
import sg.asmallmuseum.Domain.Values;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtworkDeleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserPathDeleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserPostExistsListener;
import sg.asmallmuseum.presentation.SignIn.SignInActivity;

public class ArtViewFragment extends Fragment implements View.OnClickListener, ArtWorkLoadCompleteListener,
        ArtworkDeleteListener, UserPathDeleteListener, UserPostExistsListener, PopupMenu.OnMenuItemClickListener {
    private View view;
    private ArtViewViewModel viewModel;
    private ViewPager2 viewPager;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private boolean isExpanded;
    private ArtworkManager artworkManager;
    private UserManager userManager;
    private int numImages;
    private Artwork artwork;

    private ArtViewCommentFragment mArtViewCommentFragment;

    private boolean like;

    public ArtViewFragment() {
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
        view = inflater.inflate(R.layout.fragment_art_view, container, false);

        artworkManager = new ArtworkManager();
        artworkManager.setArtworkLoadCompleteListener(this);
        artworkManager.setDeleteArtworkListener(this);

        userManager = new UserManager();
        userManager.setUserPathDeleteListener(this);
        userManager.setUserPostExistsListener(this);

        mArtViewCommentFragment = new ArtViewCommentFragment();

        setButtons();
        getArtInfo();
        isExpanded = true;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ArtViewViewModel.class);
        viewModel.setmPath(getActivity().getIntent().getStringExtra(Values.DOCUMENT_PATH));

        setReviewWindow();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    private void setReviewWindow(){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_art_comment_container, mArtViewCommentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setButtons(){
        Button mExpandTitle = (Button) view.findViewById(R.id.fragment_art_expand_button);
        Button mMore = (Button) view.findViewById(R.id.fragment_art_more_button);
        Button mExpand = (Button) view.findViewById(R.id.fragment_art_pager_expand_button);
        Button mLike = (Button) view.findViewById(R.id.fragment_art_like_button);

        mExpandTitle.setOnClickListener(this);
        mExpand.setOnClickListener(this);
        mMore.setOnClickListener(this);
        mLike.setOnClickListener(this);

        Button mBack = (Button) view.findViewById(R.id.back_button);
        Button mTopMenu = (Button) view.findViewById(R.id.top_menu_button);

        mBack.setOnClickListener(this);
        mTopMenu.setOnClickListener(this);
    }

    private void showPopup(){
        PopupMenu popup = new PopupMenu(getContext(), view.findViewById(R.id.fragment_art_more_button));
        MenuInflater inflater = popup.getMenuInflater();

        Menu menu = popup.getMenu();
        if (mUser != null && artwork.getaUserID().equals(mUser.getEmail())){
            inflater.inflate(R.menu.menu_more_enable, menu);
        }
        else {
            inflater.inflate(R.menu.menu_more_disable, menu);
        }
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    private void setViewPager(List<Uri> mList){
        //run when image loads finished.
        mList.sort(new Comparator<Uri>() {
            @Override
            public int compare(Uri uri, Uri t1) {
                return uri.toString().compareTo(t1.toString());
            }
        });

        viewModel.setUriList(mList);

        viewPager = (ViewPager2) view.findViewById(R.id.fragment_art_image_pager);
        ArtViewPagerAdapter adapter = new ArtViewPagerAdapter(this);
        adapter.setData(mList, false);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.fragment_art_expand_button){
            resizeDescLayout();
        }
        else if (id == R.id.fragment_art_more_button){
            showPopup();
        }
        else if (id == R.id.fragment_art_pager_expand_button){
            if (getActivity() instanceof ArtViewActivity){
                viewModel.setmCurrentPage(viewPager.getCurrentItem());
                ((ArtViewActivity) getActivity()).replaceFragment(this);
            }
        }
        else if (id == R.id.fragment_art_like_button){
            eventLike();
        }
        else if (id == R.id.top_menu_button){
            if(getActivity() instanceof ArtViewActivity){
                ((ArtViewActivity) getActivity()).openMenuFragment();
            }
        }
        else if (id == R.id.back_button){
            getActivity().finish();
        }
    }

    private void eventLike(){
        if (mUser != null){
            String email = mUser.getEmail();
            Button button = (Button) view.findViewById(R.id.fragment_art_like_button);

            if (!like){
                userManager.updateUserPost(email, Values.ART_LIKE, artwork.getaID().getPath());
                artworkManager.updateArtwork(artwork, Values.ART_VAL_LIKE, 1);
                button.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.image_like_filled));

                Toast.makeText(getContext(), "Like Added", Toast.LENGTH_SHORT).show();
            }
            else {
                userManager.deletePath(email, artwork, Values.ART_LIKE);
                artworkManager.updateArtwork(artwork, Values.ART_VAL_LIKE, -1);
                button.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.image_like));

                Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
            }
            like = !like;
        }
        else {
            showAlert("Only verified user can use it. Please Sign-In");
            //Toast.makeText(getContext(), "Please Sign-In.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resizeDescLayout(){
        ConstraintLayout descLayout = (ConstraintLayout) view.findViewById(R.id.fragment_art_desc_view);
        //ConstraintLayout titleLayout = (ConstraintLayout)view.findViewById(R.id.fragment_art_title_layout);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) descLayout.getLayoutParams();
        //ConstraintLayout.LayoutParams titleParams = (ConstraintLayout.LayoutParams) titleLayout.getLayoutParams();

        Button button = (Button) view.findViewById(R.id.fragment_art_expand_button);

        if (isExpanded){
            params.height = descLayout.getMinHeight();
            button.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.collapse, getActivity().getTheme()));
        }
        else {
            params.height = descLayout.getMaxHeight();
            button.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.expand, getActivity().getTheme()));
        }

        isExpanded = !isExpanded;
        descLayout.setLayoutParams(params);
    }

    private void getArtInfo(){
        Intent intent = getActivity().getIntent();
        artworkManager.getSingleArtInfoByPath(intent.getStringExtra(Values.DOCUMENT_PATH));
        //manager.getSingleArtInfoByPath(intent.getStringExtra("DocPath"));
        String path = intent.getStringExtra(Values.DOCUMENT_PATH);

    }

    @Override
    public void onArtworkLoadComplete(List<Artwork> artworks, int request_code) {
        List<Artwork> mList = artworks;
        List<Uri> uriList = new ArrayList<>();
        artwork = artworks.get(0);

        if (!artwork.getaCategory().equals(Values.ART_MUSEUM)){
            List<StorageReference> refs = artworkManager.getArtImages(artwork.getaCategory(), artwork.getaFileLoc());
            for (int i = 0 ; i < refs.size() ; i++) {
                refs.get(i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uriList.add(uri);

                        if (uriList.size() == refs.size()) {
                            setViewPager(uriList);

                            if (mUser != null){
                                userManager.existsIn(Values.ART_LIKE, mUser.getEmail(), artwork);
                            }
                        }
                    }
                });
                numImages = refs.size();
            }
        }

        ((TextView) view.findViewById(R.id.fragment_art_title)).setText(artwork.getaTitle());
        ((TextView) view.findViewById(R.id.fragment_art_author)).setText(artwork.getaAuthor());
        ((TextView) view.findViewById(R.id.fragment_art_desc)).setText(artwork.getaDesc());
//        ((RatingBar) view.findViewById(R.id.fragment_art_rating)).setRating(artwork.getaRating());
    }

    @Override
    public void onArtworkDeleteComplete(boolean result, int request_code) {
        if (result){
            if (request_code == RequestCode.RESULT_ART_DELETE_OK){
                artworkManager.deleteArtworkImages(artwork);
                Toast.makeText(getContext(), "Delete Complete!", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            else if (request_code == RequestCode.RESULT_PATH_DELETE_OK){
                artworkManager.deleteArtwork(artwork);
            }
        }
        else {
            Toast.makeText(getContext(), "Fail to Delete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserPathDeleteComplete(boolean result, String field) {
        if (result && field.equals(Values.USER_POST)){
            artworkManager.deleteArtworkRecentPath(artwork);
        }
        else {
            Toast.makeText(getContext(), "Fail to Delete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserPostExists(boolean result) {
        Button button = (Button) view.findViewById(R.id.fragment_art_like_button);
        like = result;

        if (result){
            button.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.image_like_filled));
        }
        else {
            button.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.image_like));
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.menu_more_delete){
            if (mUser != null){
                userManager.deletePath(mUser.getEmail(), artwork, Values.USER_POST);
            }
        }
//        else if (id == R.id.menu_more_edit){
//            Intent intent = new Intent(getContext(), ArtUploadPageActivity.class);
//            intent.putExtra("Art", artwork);
//            startActivity(intent);
//        }
        else if (id == R.id.menu_more_report){
            if (mUser != null){
                viewModel.setArtwork(artwork);
                ArtViewReportFragment reportFragment = new ArtViewReportFragment();
                reportFragment.show(getParentFragmentManager(), "dialog");
            }
            else {
                showAlert("Please Sign-in");
            }
        }
        return false;
    }

    private void showAlert(String msg){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }
}