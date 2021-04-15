package sg.asmallmuseum.presentation.ArtView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import sg.asmallmuseum.Domain.Comment;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.CommentManager;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.CommentLoadListener;

public class ArtViewCommentFragment extends Fragment implements CommentLoadListener,View.OnClickListener {
    private View view;
    private CommentRecyclerViewAdapter adapter;
    private ArtViewViewModel viewModel;
    private CommentManager commManager;
    private Button commInputBtn;
    private EditText commInput;
    private FirebaseUser fUser;
    public ArtViewCommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        view = inflater.inflate(R.layout.fragment_art_view_comment, container, false);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        commInputBtn = (Button) view.findViewById(R.id.comment_input_button);
        commInputBtn.setOnClickListener(this);

        commManager = new CommentManager();
        commManager.setCommentListener(this);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ArtViewViewModel.class);
        getCommentLoad();
    }

    private void getCommentLoad(){
        MutableLiveData<String> path = viewModel.getmPath();
        String[] path2 = path.getValue().split("/");

        String ref = path2[3] ;
        String category = path2[1];
        String type = path2[2];
        commManager.getComment(category, type, ref);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.comment_input_button){
            commentInput();
        }
    }

    private void commentInput(){
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        MutableLiveData<String> path2 = viewModel.getmPath();
        String[] arr = path2.getValue().split("/");

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        commInput = (EditText) view.findViewById(R.id.comment_input);
        String content = commInput.getText().toString();
        String commDate = simpleDateFormat.format(mDate);

        String ref = arr[3] ;
        String idx = "";
        String category = arr[1];
        String type = arr[2];
        String path = "";

        if(fUser != null && content.trim().length() > 0){
            String email = fUser.getEmail();
            Comment comment = new Comment(idx, ref, path, email, content, commDate);

            commInput.setText("");
            commManager.addComment(comment, category, type, ref);
            commManager.getRefreshComment(category,type,ref);
        }


    }

    @Override
    public void commentLoadListener(List<Comment> comments) {
        RecyclerView recyclerView = view.findViewById(R.id.comment_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CommentRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        comments.sort(new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                if (o1.getCommDate() != null && o2.getCommDate() != null){
                    return -(o1.getCommDate()).compareTo(o2.getCommDate());
                }
                else {
                    return -(o1.getCommDate()).compareTo(o2.getCommDate());
                }
            }
        });

        for (int i = 0; i < comments.size(); i++) {
            Comment data = new Comment();
            data.setuEmail(comments.get(i).getuEmail());
            data.setPath(comments.get(i).getPath());
            data.setContent(comments.get(i).getContent());
            data.setCommIdx(comments.get(i).getCommIdx());

            if(!(comments.get(0).getContent().equals("null") && comments.get(0).getuEmail().equals("null"))){
                adapter.addItem(data);
            }
        }
        adapter.notifyDataSetChanged();


    }


}