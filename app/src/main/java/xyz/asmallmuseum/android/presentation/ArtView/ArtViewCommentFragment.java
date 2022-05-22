package xyz.asmallmuseum.android.presentation.ArtView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Comparator;
import java.util.List;

import xyz.asmallmuseum.android.domain.Comment;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.logic.CommentManager;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.CommentLoadListener;

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

        TextView inputText = (TextView) view.findViewById(R.id.comment_input);
        inputText.setOnClickListener(this);

        commManager = new CommentManager();
        commManager.setCommentListener(this);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ArtViewViewModel.class);
        viewModel.getmPath().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String path) {
                if (path != null) {
                    getCommentLoad(path);
                }
            }
        });

    }

    private void getCommentLoad(String path){
        String[] path2 = path.split("/");

        String ref = path2[3] ;
        String category = path2[1];
        String type = path2[2];
        commManager.getComment(category, type, ref);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.comment_input_button || id == R.id.comment_input){
//            commentInput();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_art_view_container, new ArtViewWritingFragment());
            transaction.addToBackStack(null);
            transaction.commit();
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

        if(!(comments.get(0).getContent().equals("null") && comments.get(0).getuEmail().equals("null"))){
//            int size = comments.size();
//            for (int i = 0; i < size; i++) {
////                Comment data = new Comment();
////                data.setuEmail(comments.get(i).getuEmail());
////                data.setPath(comments.get(i).getPath());
////                data.setContent(comments.get(i).getContent());
////                data.setCommIdx(comments.get(i).getCommIdx());
//                adapter.addItem(comments.get(i));
//            }
            adapter.updateList(comments);
            adapter.notifyDataSetChanged();
        }
    }
}
