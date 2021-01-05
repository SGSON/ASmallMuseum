package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ArtViewActivity extends AppCompatActivity {
    private List<String[]> review;
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_view);

        review = new ArrayList<>();
        adapter = new ReviewAdapter(review);
        setReview();
        initReview();
    }

    private void initReview(){
        recyclerView = (RecyclerView)findViewById(R.id.art_scroll_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setReview(){
        String[] a = {};
        review.add(new String[]{"asdasd", "2021-01-05", "3", "qbfwghilwudfbqliwdbulvcdjqbudrvbqiuwbviqblurbqilugdblvqbuvblquvulqbqeufbvlquievbluebvlqbulefbuvliqebifl"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"accacaac", "2019-7-23", "5", "fqenodnaflkjbnljbnpoauibhvaho;4eq598749846qfeqfqevq4e87465135vdacv"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});
        review.add(new String[]{"qwrqq", "2020-10-05", "2", "nononno"});

    }
}