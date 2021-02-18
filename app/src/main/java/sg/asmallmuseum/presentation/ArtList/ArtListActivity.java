package sg.asmallmuseum.presentation.ArtList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.General.ManagerListener;
import sg.asmallmuseum.presentation.General.MenuEvents;
import sg.asmallmuseum.presentation.General.RecyclerViewOnClickListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ArtListActivity extends AppCompatActivity implements RecyclerViewOnClickListener, ManagerListener, SwipeRefreshLayout.OnRefreshListener {
    private FirebaseAuth mAuth;
    private ArtworkManager manager;
    private boolean signedIn;
    private boolean isTypeText;
    private boolean isMuseum;
    private ProgressDialog dialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArtListViewAdapterInterface adapter;

    private final int REQUEST_USER = 2010;

    private int totalPost;
    private int currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_list);

        dialog = new ProgressDialog(this, android.R.style.Theme_Material_Dialog_Alert);
        dialog.setMessage("LOADING..");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();

        manager = new ArtworkManager();
        manager.setListener(this);

        manager.getNumPost(intent.getStringExtra("Type"), intent.getStringExtra("Genre"), REQUEST_USER);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.art_list_swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        isMuseum = intent.getStringExtra("Type").equals("Museums");
        isTypeText = (intent.getStringExtra("Type").equals("Music") || intent.getStringExtra("Type").equals("Books"));

        initRecyclerView(new ArrayList<Artwork>());
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

    @Override
    public void onRefresh() {
        Intent intent = getIntent();
        manager.getNumPost(intent.getStringExtra("Type"), intent.getStringExtra("Genre"), REQUEST_USER);
        mSwipeRefreshLayout.setRefreshing(false);
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
        if (isMuseum){
            adapter = new ArtListMuseumViewAdapter(artworks);
        }
        else if (isTypeText){
            adapter = new ArtListTextViewAdapter(artworks, manager);
        }
        else {
            adapter = new ArtListImageViewAdapter(artworks, manager);
        }
        adapter.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.art_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);

        dialog.dismiss();
    }

    private void updateList(List<Artwork> list){
        adapter.updateList(list);
        ((RecyclerView.Adapter) adapter).notifyDataSetChanged();
        dialog.dismiss();
    }

    @Override
    public void onDownloadCompleteListener(List<Artwork> artworks) {
        updateList(artworks);
    }

    @Override
    public void onNumPostLoadComplete(int result) {
        totalPost = result;
        currentPost = result;

        Log.d("NUM", ""+result);
        Intent intent = getIntent();
        manager.getArtInfoList(intent.getStringExtra("Type"), intent.getStringExtra("Genre"), totalPost);
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
