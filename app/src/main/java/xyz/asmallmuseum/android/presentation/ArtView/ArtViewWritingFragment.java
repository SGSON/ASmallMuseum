package xyz.asmallmuseum.android.presentation.ArtView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import xyz.asmallmuseum.android.Domain.Comment;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.logic.CommentManager;


public class ArtViewWritingFragment extends Fragment implements View.OnClickListener{
    private View view;
    private FirebaseUser fUser;
    private String path;

    public ArtViewWritingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_art_view_writing, container, false);
        Button mBack = (Button) view.findViewById(R.id.fragment_art_view_writing_back);
        Button mSubmit = (Button) view.findViewById(R.id.fragment_art_view_writing_submit);

        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArtViewViewModel viewModel = new ViewModelProvider(requireActivity()).get(ArtViewViewModel.class);
        viewModel.getmPath().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                path = s;
            }
        });
    }

    private void commentInput(){
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        String[] arr = path.split("/");
        CommentManager commManager = new CommentManager();

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        EditText commInput = (EditText) view.findViewById(R.id.fragment_art_view_writing_comment);
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
//            commManager.getRefreshComment(category,type,ref);
        }

        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_art_view_writing_submit){
            commentInput();
        }
        else if (id == R.id.fragment_art_view_writing_back){
            getParentFragmentManager().popBackStack();
        }
    }
}