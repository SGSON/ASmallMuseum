package sg.asmallmuseum.presentation.General;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.ArtList.ArtListImageViewAdapter;
import sg.asmallmuseum.presentation.ArtUpload.ArtUploadPageActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ManagerListener, RecyclerViewOnClickListener, SwipeRefreshLayout.OnRefreshListener{
    private FirebaseAuth mAuth;
    private final int REQUEST_CODE = 1;
    private ArtworkManager manager;
    private ArtListImageViewAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog dialog;
    private boolean signedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this, android.R.style.Theme_Material_Dialog_Alert);
        dialog.setMessage("LOADING..");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mAuth = FirebaseAuth.getInstance();

        //Set a listener to Artwork Manager
        manager = new ArtworkManager();
        manager.setListener(this);

        //will be move to the upload activity
        requestPermission();

        //Load recent upload images
        //initiate with empty data set and then the view will be updated when loads complete.
        initRecentView(new ArrayList<Artwork>());

        //Remove the back button.
        Button mBackButton = (Button)findViewById(R.id.back_button);
        mBackButton.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        Intent intent = new Intent(this, ArtUploadPageActivity.class);

        //Set onClickMethods for the quick button
        ImageButton mQuick = (ImageButton)findViewById(R.id.quick_menu_button);
        mQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                //throw new RuntimeException("Teest");
            }
        });
    }

    /***Verify a signed-in user***/
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

    @Override
    public void onRefresh() {
        manager.getRecent();
        swipeRefreshLayout.setRefreshing(false);
    }

    /***Initiate Recycler view
     * Start with a empty list. then it will show latest uploading images when image load finish***/
    private void initRecentView(List<Artwork> artworks){
        adapter = new ArtListImageViewAdapter(artworks, manager);
        adapter.setOnClickListener(this);

        RecyclerView recent_view = (RecyclerView)findViewById(R.id.view_recent);
        recent_view.setLayoutManager(new LinearLayoutManager(this));
        recent_view.setAdapter(adapter);

        manager.getRecent();
    }

    /***Start an activity what user clicked***/
    @Override
    public void onItemClick(int position, Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position, List<String> mList) {
        //Has to be empty
    }

    /***Get image information from ArtManager
     * Then, update the recycler view***/
    @Override
    public void onDownloadCompleteListener(List<Artwork> artworks){
        updateList(artworks);
    }

    private void updateList(List<Artwork> artworks){
        adapter.updateList(artworks);
        adapter.notifyDataSetChanged();
        dialog.dismiss();
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
        MenuEvents menuEvents = new MenuEvents(mAuth ,this);
        menuEvents.openMenu(signedIn);
    }

    /***Nothing***/
    public void onBackButtonPressed(View view) {
        //do not insert codes
    }

    /***Get a storage access permission***/
    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

        } /*else if (shouldShowRequestPermissionRationale(...)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            showInContextUI(...);*/
        else {
            // You can directly ask for the permission.
            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_CODE);
        }
    }
    /***End***/

    @Override
    public void onUploadCompleteListener(boolean status) {
        //empty
    }


}