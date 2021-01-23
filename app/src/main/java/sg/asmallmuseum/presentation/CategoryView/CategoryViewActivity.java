package sg.asmallmuseum.presentation.CategoryView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.General.RecyclerViewOnClickListener;

import android.content.Intent;
import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

public class CategoryViewActivity extends AppCompatActivity implements RecyclerViewOnClickListener {
    private List<String> mTypeList;
    private List<String> mGenreList;
    private RecyclerView mTypeView;
    private RecyclerView mGenreView;
    private CategoryViewAdapter mTypeAdapter;
    private CategoryViewAdapter mGenreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

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
}