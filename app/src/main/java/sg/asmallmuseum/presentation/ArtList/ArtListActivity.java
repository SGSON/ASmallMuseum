package sg.asmallmuseum.presentation.ArtList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.General.ManagerListener;
import sg.asmallmuseum.presentation.General.MenuEvents;
import sg.asmallmuseum.presentation.General.RecyclerViewOnClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ArtListActivity extends AppCompatActivity implements RecyclerViewOnClickListener, ManagerListener {
    private FirebaseAuth mAuth;
    private ArtworkManager manager;
    private boolean signedIn;
    private boolean mTypeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_list);

        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        manager = new ArtworkManager();
        manager.setListener(this);

        //Log.d("CLICKED:", intent.getStringExtra("Type"));
        //Log.d("CLICKED:", intent.getStringExtra("Genre"));

        manager.getArtInfoList(intent.getStringExtra("Type"), intent.getStringExtra("Genre"));

        mTypeText = (intent.getStringExtra("Type").equals("Music") || intent.getStringExtra("Type").equals("Books"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            Toast.makeText(this, "Unidentified user!", Toast.LENGTH_SHORT).show();
            signedIn = false;
        }
        else{
            Toast.makeText(this, "Signed-in user!", Toast.LENGTH_SHORT).show();
            signedIn = true;
        }
    }

    /***Top-bar events***/
    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onMainButtonPressed(View view) {
        Toast.makeText(this, "Pressed Main Button", Toast.LENGTH_SHORT).show();
    }

    public void onMenuButtonPressed(View view) {
        Toast.makeText(this, "Pressed Menu Button", Toast.LENGTH_SHORT).show();
        MenuEvents menuEvents = new MenuEvents(mAuth, this);
        menuEvents.openMenu(signedIn);
    }

    public void onBackButtonPressed(View view) {
        finish();
    }
    /***End***/

    /***Load File from DB***/
    private void initRecyclerView(List<Artwork> artworks){
        ArtListViewAdapterInterface adapter;
        if (mTypeText){
            adapter = new ArtListTextViewAdapter(artworks, manager);
        }
        else {
            adapter = new ArtListImageViewAdapter(artworks, manager);
        }
        adapter.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.art_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
    }

    @Override
    public void onDownloadCompleteListener(List<Artwork> artworks) {
        initRecyclerView(artworks);
    }

    @Override
    public void onItemClick(int position, Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position, List<String> mList) {
        //Has to be empty
    }

    /***End***/

    @Override
    public void onUploadCompleteListener(boolean status) {
        //empty
    }
}