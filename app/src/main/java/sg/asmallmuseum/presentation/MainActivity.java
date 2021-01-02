package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.CustomException;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.logic.MenuAction;
import sg.asmallmuseum.logic.UserManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView recent_view;
    private List<Artwork> mArtList;
    private CardViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArtList = new ArrayList<>();
        adapter = new CardViewAdapter(mArtList);
        setData();
        initRecentView();
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

}