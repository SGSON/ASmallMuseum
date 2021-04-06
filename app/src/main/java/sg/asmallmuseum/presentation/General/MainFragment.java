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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Values;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.ArtList.ArtListImageViewAdapter;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.NumPostLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.OnBottomReachedListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;

import static sg.asmallmuseum.Domain.RequestCode.REQUEST_RANDOM;

public class MainFragment extends Fragment implements RecyclerViewOnClickListener, SwipeRefreshLayout.OnRefreshListener,
        ArtWorkLoadCompleteListener, View.OnClickListener, NumPostLoadCompleteListener {

    private final int MAX_LIST_SIZE = 10;

    private View view;
    private ArtworkManager manager;
    private ArtListImageViewAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog dialog;
    private MainMenuViewModel viewModel;
    private List<Artwork> mList;
    private Map<String, String> map;
    private List<Artwork> mRandomList;
    private ArtListImageViewAdapter mRandomAdapter;

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

//        dialog = new ProgressDialog(getContext(), android.R.style.Theme_Material_Dialog_Alert);
//        dialog.setMessage("LOADING..");
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();

        Button mMenu = (Button) view.findViewById(R.id.top_menu_button);
        mMenu.setOnClickListener(this);

        //Set a listener to Artwork Manager
        manager = new ArtworkManager();
        manager.setArtworkLoadCompleteListener(this);
        manager.setNumPostLoadCompleteListener(this);

        map = new HashMap<>();

        //Load recent upload images
        //initiate with empty data set and then the view will be updated when loads complete.
        initRecentView(new ArrayList<Artwork>());
        initRandomView();

        //Remove the back button.
        Button mBackButton = (Button)view.findViewById(R.id.back_button);
        mBackButton.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

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
        getRandomImage();
        swipeRefreshLayout.setRefreshing(false);
    }

    /***Initiate Recycler view
     * Start with a empty list. then it will show latest uploading images when image load finish***/
    private void initRecentView(List<Artwork> artworks){
        adapter = new ArtListImageViewAdapter(artworks, manager);
        adapter.setOnClickListener(this);
        adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached() {

            }
        });

        RecyclerView recent_view = (RecyclerView)view.findViewById(R.id.view_recent);
        recent_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recent_view.setAdapter(adapter);

        if (mList == null){
            manager.getRecent();
        }
        else {
            updateList(mList);
//            dialog.dismiss();
        }
    }

    private void initRandomView(){
        mRandomList = new ArrayList<>();
        mRandomAdapter = new ArtListImageViewAdapter(new ArrayList<>(), manager);
        mRandomAdapter.setOnClickListener(this);
        mRandomAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached() {

            }
        });

        RecyclerView recent_view = (RecyclerView)view.findViewById(R.id.fragment_main_recycler_random);
        recent_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recent_view.setAdapter(mRandomAdapter);

        if (mRandomList == null){
            getRandomImage();
        }
        else {
            mRandomAdapter.updateList(mRandomList);
        }
    }

    private void getRandomImage(){
        List<String> category_list = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.arts_categories)));
        List<String> type_list;
        String category;
        String type;

        int cate_size = category_list.size();

        for (int i = 0 ; i < MAX_LIST_SIZE ; i++){
            category = category_list.get(getRandomNumber(cate_size-1));
            type_list = getItemList(category);
            type = type_list.get(getRandomNumber(type_list.size()));

            manager.getNumPost(category, type, REQUEST_RANDOM);
        }

    }

    private int getRandomNumber(int size){
        return (int)(Math.random() * size);
    }

    private List<String> getItemList(String item){
        List<String> list;
        switch (item){
            case Values.ART_VISUAL:
                list = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.type_visual)));
                break;
            case Values.ART_APPLIED:
                list = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.type_applied)));
                break;
            case Values.ART_OTHERS:
                list = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.type_others)));
                break;
            case Values.ART_MUSEUM:
                list = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.museums)));
                break;
            default:
                list = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.type_fine)));
                break;
        }
        list.remove(0);
        return list;
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
    public void onArtworkLoadComplete(List<Artwork> artworks, int request_code) {
        if (request_code == REQUEST_RANDOM){
            if (artworks.size() != 0 && artworks.get(0) != null){
                mRandomList.add(artworks.get(0));
                mRandomAdapter.updateList(mRandomList);
            }
        }
        else{
            manager.sortByDate(artworks);
            mList = artworks;
            updateList(artworks);
        }

    }

    private void updateList(List<Artwork> artworks){
        adapter.updateList(artworks);
//        dialog.dismiss();
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

    @Override
    public void onNumPostLoadComplete(int result, int request_code, String category, String type) {
        if (result != 0){
            int rand = (int)(Math.random() * result + 1);
            manager.getArtInfoByNumPost(category, type, rand, REQUEST_RANDOM);
        }
    }
}