package xyz.asmallmuseum.android.presentation.UserProfile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xyz.asmallmuseum.android.Domain.Artwork;
import xyz.asmallmuseum.android.Domain.User;
import xyz.asmallmuseum.android.Domain.Values;
import xyz.asmallmuseum.R;
import xyz.asmallmuseum.android.logic.ArtworkManager;
import xyz.asmallmuseum.android.logic.UserManager;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.ArtWorkLoadCompleteListener;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.UserPostLoadCompleteListener;

public class UserHistoryPostFragment extends Fragment implements ArtWorkLoadCompleteListener, UserPostLoadCompleteListener, RecyclerViewOnClickListener {

    private View view;
    private User mUser;
    private UserHistoryViewAdapter adapter;
    private ArtworkManager mArtworkManager;
    private UserManager mUserManager;
    private String mField;


    public UserHistoryPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_history_post, container, false);

        mArtworkManager = new ArtworkManager();
        mArtworkManager.setArtworkLoadCompleteListener(this);
        mUserManager = new UserManager();
        mUserManager.setUserPostLoadListener(this);

        setRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserProfileViewModel viewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                getData(user);
            }
        });
    }

    private void getData(User user){
        Bundle args = getArguments();
        mField = args.getString(Values.USER_POST_FIELD);
        mUserManager.getUserPosting(user.getuEmail(), mField);
    }

    private void setRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_user_history_post_recycler_view);
        adapter = new UserHistoryViewAdapter(new ArtworkManager());
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onArtworkLoadComplete(List<Artwork> artworks, int request_code) {
        mArtworkManager.sortByDate(artworks);
        if (getParentFragment() instanceof UserProfileViewHistoryFragment){
            ((UserProfileViewHistoryFragment) getParentFragment()).setNumPosts(mField, artworks.size());
        }
        adapter.updateList(artworks);
    }

    @Override
    public void onUserPostLoadComplete(List<String> posts) {
        if (mField.equals(Values.USER_COMMENTS)){
            List<String> path = getArtPath(posts);
            mArtworkManager.getMultipleArtInfoByPath(path);
        }
        else {
            mArtworkManager.getMultipleArtInfoByPath(posts);
        }
    }

    private List<String> getArtPath(List<String> commentPath){
        List<String> path = new ArrayList<>();
        int size = commentPath.size();

        for (int i = 0 ; i < size ; i++){
            String[] tempPath = commentPath.get(i).split("/");
            String artPath = tempPath[0] + "/" + tempPath[1] + "/" + tempPath[2] + "/" + tempPath[3];
            Log.d("Path", artPath);
            path.add(artPath);
        }

        return path;
    }

    @Override
    public void onItemClick(int position, Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position, List<String> mList) {

    }
}