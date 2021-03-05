package sg.asmallmuseum.presentation.General;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.ArtList.ArtListImageViewAdapter;
import sg.asmallmuseum.presentation.ArtUpload.ArtUploadPageActivity;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;

public class MainFragment extends Fragment implements RecyclerViewOnClickListener, SwipeRefreshLayout.OnRefreshListener,
        ArtWorkLoadCompleteListener, View.OnClickListener {

    private View view;
    private ArtworkManager manager;
    private ArtListImageViewAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog dialog;
    private MainMenuViewModel viewModel;

    public MainFragment() {
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
        view = inflater.inflate(R.layout.fragment_main, container, false);

        dialog = new ProgressDialog(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        dialog.setMessage("LOADING..");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button mMenu = (Button) view.findViewById(R.id.top_menu_button);
        mMenu.setOnClickListener(this);

        //Set a listener to Artwork Manager
        manager = new ArtworkManager();
        manager.setArtworkLoadCompleteListener(this);


        //Load recent upload images
        //initiate with empty data set and then the view will be updated when loads complete.
        initRecentView(new ArrayList<Artwork>());

        //Remove the back button.
        Button mBackButton = (Button)view.findViewById(R.id.back_button);
        mBackButton.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        Intent intent = new Intent(getActivity(), ArtUploadPageActivity.class);

        //Set onClickMethods for the quick button
        ImageButton mQuick = (ImageButton)view.findViewById(R.id.quick_menu_button);
        mQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                //throw new RuntimeException("Teest");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainMenuViewModel.class);
    }

    @Override
    public void onRefresh() {
        manager.getRecent();
        swipeRefreshLayout.setRefreshing(false);
    }

    /***Initiate Recycler view
     * Start with a empty list. then it will show latest uploading images when image load finish***/
    private void initRecentView(List<Artwork> artworks){
        adapter = new ArtListImageViewAdapter(artworks, manager);
        adapter.setOnClickListener(this);

        RecyclerView recent_view = (RecyclerView)view.findViewById(R.id.view_recent);
        recent_view.setLayoutManager(new LinearLayoutManager(getContext()));
        recent_view.setAdapter(adapter);

        manager.getRecent();
    }

    /***Start an activity what user clicked***/
    @Override
    public void onItemClick(int position, Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position, List<String> mList) {
        //Has to be empty
    }

    /***Get image information from ArtManager
     * Then, update the recycler view***/
    @Override
    public void onArtworkLoadComplete(List<Artwork> artworks) {
        updateList(artworks);
    }

    private void updateList(List<Artwork> artworks){
        adapter.updateList(artworks);
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.top_menu_button){
            if(getActivity() instanceof MainActivity){
                ((MainActivity) getActivity()).openMenuFragment(this);
            }
        }
    }
}