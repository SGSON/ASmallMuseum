package sg.asmallmuseum.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.MenuAction;
import sg.asmallmuseum.logic.UserManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView recent_view;
    private List<Artwork> mArtList;
    private CardViewAdapter adapter;
    private final int REQUEST_CODE = 20180201;

    private ImageButton mQuick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mQuick = (ImageButton)findViewById(R.id.quick_menu_button);
        Intent intent = new Intent(this, ArtViewActivity.class);
        mQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        mArtList = new ArrayList<>();
        adapter = new CardViewAdapter(mArtList);
        setData();
        initRecentView();
    }

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

    private void initRecentView(){
        recent_view = (RecyclerView)findViewById(R.id.view_recent);
        recent_view.setLayoutManager(new LinearLayoutManager(this));
        recent_view.setAdapter(adapter);
    }

    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onMainButtonPressed(View view) {
        Toast.makeText(this, "Pressed Main Button", Toast.LENGTH_SHORT).show();
    }

    public void onMenuButtonPressed(View view) {
        Toast.makeText(this, "Pressed Menu Button", Toast.LENGTH_SHORT).show();
        MenuAction menuAction = new MenuAction();
        menuAction.openMenu(this);
    }

    public void onBackButtonPressed(View view) {
        Toast.makeText(this, "Pressed Back Button", Toast.LENGTH_SHORT).show();
    }

    private void setData(){
        mArtList.add(new Picture("123089123","asd","asdasdasd","asdasd","asdasdasd"));
        mArtList.add(new Picture("183902","fdwwdfw","wegwbv","svc","atehaebba"));
        mArtList.add(new Picture("1348140","wfdscvs","vscvc","dfw","a"));
        mArtList.add(new Picture("918376481","jryrjt","svcvvcsv","bwfbw","wn"));
        mArtList.add(new Picture("34958043","ero6l","scvscv","htt","qbe"));
    }

    /***Google Sign-Up methods***/
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