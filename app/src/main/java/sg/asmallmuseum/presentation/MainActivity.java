package sg.asmallmuseum.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ManagerListener, RecyclerViewOnClickListener{
    private FirebaseAuth mAuth;
    private final int REQUEST_CODE = 20180201;
    private ArtworkManager manager;

    private boolean signedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        manager = new ArtworkManager();
        manager.setListener(this);
        requestPermission();

        //Load recent upload images
        manager.getArtInfo("Books", "Literal");

        Intent intent = new Intent(this, ArtListActivity.class);
        ImageButton mQuick = (ImageButton)findViewById(R.id.quick_menu_button);
        mQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent.putExtra("Type", "Books");
                intent.putExtra("Genre", "Literal");
                startActivity(intent);
                //manager.uploadFile("/storage/emulated/0/Download/test.png");
                //manager.addArtwork("Picture","Land", "Amazing Stories", "SG", "2030-1-10", "AMAZING!");
                //manager.upLoadArt("/storage/emulated/0/Download/test.png",
                 //       "Books","Literal", "Amazing Stories!!", "SG", "2030-1-10", "AMAZING!");
            }
        });
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

    /***Initiate Recycler view***/
    private void initRecentView(List<Artwork> artworks){
        ArtLinearViewAdapter adapter = new ArtLinearViewAdapter(artworks, manager);
        adapter.setOnClickListener(this);

        RecyclerView recent_view = (RecyclerView)findViewById(R.id.view_recent);
        recent_view.setLayoutManager(new LinearLayoutManager(this));
        recent_view.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, Intent intent) {
        startActivity(intent);
    }

    //Get an image information from ArtManager
    @Override
    public void onLoadCompleteListener(List<Artwork> artworks){
        initRecentView(artworks);
    }
    /***End***/

    /***Top-bar button events***/
    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onMainButtonPressed(View view) {
        makeText("Pressed Main Button");
    }

    public void onMenuButtonPressed(View view) {
        makeText("Pressed Menu Button");

        //Configure the main menu
        MenuAction menuAction = new MenuAction();
        menuAction.openMenu(this, signedIn);
    }

    public void onBackButtonPressed(View view) {
        makeText("Pressed Back Button");
    }
    /***End***/

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


}