package sg.asmallmuseum.presentation.UserProfile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserPostLoadCompleteListener;

public class UserHistoryPostFragment extends Fragment implements ArtWorkLoadCompleteListener, UserPostLoadCompleteListener, RecyclerViewOnClickListener {

    private View view;
    private User mUser;
    private UserHistoryViewAdapter adapter;
    private ArtworkManager mArtworkManager;
    private UserManager mUserManager;
    private String mType;


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
        mType = args.getString("Type");
        mUserManager.getUserPosting(user.getuEmail(), mType);
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
            ((UserProfileViewHistoryFragment) getParentFragment()).setNumPosts(mType, artworks.size());
        }
        adapter.updateList(artworks);
    }

    @Override
    public void onUserPostLoadComplete(List<String> posts) {
        mArtworkManager.getMultipleArtInfoByPath(posts);
    }

    @Override
    public void onItemClick(int position, Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position, List<String> mList) {

    }
}