package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ArtListActivity extends AppCompatActivity implements RecyclerViewOnClickListener {
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ArtGridViewAdapter adapter;
    private ArrayList<Artwork> mArtList;
    private boolean signedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_list);

        mAuth = FirebaseAuth.getInstance();
        mArtList = new ArrayList<>();
        adapter = new ArtGridViewAdapter(mArtList, new ArtworkManager());

        setData();
        adapter.setOnClickListener(this);
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            Toast.makeText(this, "Unidentified user!", Toast.LENGTH_SHORT);
            signedIn = false;
        }
        else{
            signedIn = true;
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ArtViewActivity.class);
        startActivity(intent);
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
        menuAction.openMenu(this, signedIn);
    }

    public void onBackButtonPressed(View view) {
        Toast.makeText(this, "Pressed Back Button", Toast.LENGTH_SHORT).show();
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.art_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private void setData(){
        //mArtList.add(new Picture("123089123","asd","asdasdasd","asdasd","asdasdasd"));
        //mArtList.add(new Picture("183902","fdwwdfw","wegwbv","svc","atehaebba"));
        //mArtList.add(new Picture("1348140","wfdscvs","vscvc","dfw","a"));
        //mArtList.add(new Picture("918376481","jryrjt","svcvvcsv","bwfbw","wn"));
        //mArtList.add(new Picture("34958043","ero6l","scvscv","htt","qbe"));
    }
}