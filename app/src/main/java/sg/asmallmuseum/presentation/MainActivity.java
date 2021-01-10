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
    private RecyclerView recent_view;
    private List<Artwork> mArtList;
    private ArtLinearViewAdapter adapter;
    private final int REQUEST_CODE = 20180201;

    private ImageButton mQuick;
    private boolean signedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        ArtworkManager manager = new ArtworkManager();
        manager.setListener(this);
        requestPermission();

        mQuick = (ImageButton)findViewById(R.id.quick_menu_button);
        //Intent intent = new Intent(this, ArtListActivity.class);
        mQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.getArtInfo("Books", "Literal");
                //startActivity(intent);
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

    /***Get images info from Manager class***/
    @Override
    public void onLoadCompleteListener(List<Artwork> artworks){
        mArtList = artworks;
        adapter = new ArtLinearViewAdapter(mArtList);
        adapter.setOnClickListener(this);
        initRecentView();
    }
    /***End***/

    /***Recycler view events***/
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ArtViewActivity.class);
        startActivity(intent);
    }

    private void initRecentView(){
        recent_view = (RecyclerView)findViewById(R.id.view_recent);
        recent_view.setLayoutManager(new LinearLayoutManager(this));
        recent_view.setAdapter(adapter);
    }
    /***End***/

    /***Top bar button events***/
    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onMainButtonPressed(View view) {
        Toast.makeText(this, "Pressed Main Button", Toast.LENGTH_SHORT).show();
    }

    public void onMenuButtonPressed(View view) {
        Toast.makeText(this, "Pressed Menu Button", Toast.LENGTH_SHORT).show();
        MenuAction menuAction = new MenuAction();
        menuAction.openMenu(this, signedIn);
    }

    public void onBackButtonPressed(View view) {
        Toast.makeText(this, "Pressed Back Button", Toast.LENGTH_SHORT).show();
    }
    /***End***/

    /***get storage permission***/
    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
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

    /***Google Sign-Up methods***/
    /***Google Sign-in Test***/
        /*UserManager manager = new UserManager("Google");

        mQuick = (ImageButton)findViewById(R.id.quick_menu_button);
        mQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = manager.signUPWithGoogle(view.getContext(), mAuth);
                startActivity(intent);
            }
        });*/
    /***End***/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //account.
            }
            catch (ApiException e){

            }
        }
    }
    /*
     * signUPWithGoogle: get a google sign-up page
     * firebaseSignWithGoogle: Authentication with Google*/
    public Intent signUPWithGoogle(Context context, FirebaseAuth mAuth){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSignInClient mClient = GoogleSignIn.getClient(context, gso);
        Intent intent = mClient.getSignInIntent();
        return intent;
    }

    private void firebaseSignInWithGoogle(String idToken, FirebaseAuth mAuth, Context context){
        AuthCredential mAuthCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(mAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("LOGIN: ","LOGIN SUCCESS!");
                }
                else {
                    Log.w("LOGIN: ", "FAIL TO LOGIN");
                }
            }
        });
    }
    /***End***/

}