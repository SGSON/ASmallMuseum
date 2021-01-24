package sg.asmallmuseum.presentation.CategoryView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.General.MenuEvents;
import sg.asmallmuseum.presentation.General.RecyclerViewOnClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class CategoryViewActivity extends AppCompatActivity implements RecyclerViewOnClickListener {
    private List<String> mTypeList;
    private List<String> mGenreList;
    private RecyclerView mTypeView;
    private RecyclerView mGenreView;
    private CategoryViewAdapter mTypeAdapter;
    private CategoryViewAdapter mGenreAdapter;

    private boolean signedIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        mAuth = FirebaseAuth.getInstance();

        mTypeList = Arrays.asList(getResources().getStringArray(R.array.types));
        mGenreList = Arrays.asList(getResources().getStringArray(R.array.genre_book));

        mTypeView = (RecyclerView) findViewById(R.id.category_type_list);
        mGenreView = (RecyclerView) findViewById(R.id.category_genre_list);
        mTypeAdapter = new CategoryViewAdapter(this, R.id.category_type_list, mTypeList);
        mGenreAdapter = new CategoryViewAdapter(this, R.id.category_genre_list, mGenreList);

        mTypeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mGenreView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mTypeView.setAdapter(mTypeAdapter);
        mGenreView.setAdapter(mGenreAdapter);
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

    @Override
    public void onItemClick(int position, Intent intent) {

    }

    @Override
    public void onItemClick(int position, List<String> mList) {
        String type = mList.get(position);
        List<String> newList;
        switch (type){
            case "Music":
                newList = Arrays.asList(getResources().getStringArray(R.array.genre_music));
                break;
            case "Pictures":
                newList = Arrays.asList(getResources().getStringArray(R.array.genre_picture));
                break;
            case "Paints":
                newList = Arrays.asList(getResources().getStringArray(R.array.genre_paints));
                break;
            case "Etc..":
                newList = Arrays.asList(getResources().getStringArray(R.array.etc));
                break;
            default:
                newList = Arrays.asList(getResources().getStringArray(R.array.genre_book));
                break;
        }
        mGenreAdapter.updateList(newList);
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