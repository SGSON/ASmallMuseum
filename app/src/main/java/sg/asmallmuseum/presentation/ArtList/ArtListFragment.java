package sg.asmallmuseum.presentation.ArtList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.NumPostLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.OnBottomReachedListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;

public class ArtListFragment extends Fragment implements RecyclerViewOnClickListener, SwipeRefreshLayout.OnRefreshListener,
        NumPostLoadCompleteListener, ArtWorkLoadCompleteListener, View.OnClickListener {

    private View view;

    private ArtworkManager manager;
    private boolean isTypeText;
    private boolean isMuseum;
    private ProgressDialog dialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArtListViewAdapterInterface adapter;

    private String mCategory;
    private String mType;
    private String[] mItems;

    private final int REQUEST_USER = 2010;

    private int totalPost;
    private int currentPost;

    public ArtListFragment() {
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
        view = inflater.inflate(R.layout.fragment_art_list, container, false);

        dialog = new ProgressDialog(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        dialog.setMessage("LOADING..");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Intent intent = getActivity().getIntent();
        setButtons();

        manager = new ArtworkManager();
        manager.setArtworkLoadCompleteListener(this);
        manager.setNumPostLoadCompleteListener(this);

        //manager.getNumPost(intent.getStringExtra("Category"), intent.getStringExtra("Type"), REQUEST_USER);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.art_list_swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        isMuseum = intent.getStringExtra("Category").equals("Museums");

        initRecyclerView(new ArrayList<Artwork>());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArtListViewModel viewModel = new ViewModelProvider(requireActivity()).get(ArtListViewModel.class);
        viewModel.getItems().observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                mItems = strings;
                manager.getNumPost(mItems[0], mItems[1], REQUEST_USER);
                isMuseum = mItems[0].equals("Museums");
            }
        });
    }

    @Override
    public void onRefresh() {
        Intent intent = getActivity().getIntent();
        manager.getNumPost(intent.getStringExtra("Category"), intent.getStringExtra("Type"), REQUEST_USER);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setButtons(){
        Button mTopMenu = (Button) view.findViewById(R.id.top_menu_button);
        Button mBackButton = (Button) view.findViewById(R.id.back_button);

        mTopMenu.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
    }

    /***Load File from DB***/
    private void initRecyclerView(List<Artwork> artworks){
        Intent intent = getActivity().getIntent();

        if (isMuseum){
            adapter = new ArtListMuseumViewAdapter(artworks);
        }
        else {
            adapter = new ArtListImageViewAdapter(artworks, manager);
        }
        adapter.setOnClickListener(this);
        adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
                //manager.getArtInfoList(intent.getStringExtra("Category"), intent.getStringExtra("Type"), currentPost);
                manager.getArtInfoList(mItems[0], mItems[1], currentPost);
                currentPost -= 10;
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.art_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);

        dialog.dismiss();
    }

    private void updateList(List<Artwork> list){
        adapter.updateList(list);
        dialog.dismiss();
    }

    @Override
    public void onArtworkLoadComplete(List<Artwork> artworks, int request_code) {
        manager.sortByPostNum(artworks);
        updateList(artworks);
    }

    @Override
    public void onNumPostLoadComplete(int result, int request_code, String category, String type) {
        totalPost = result;
        currentPost = result-10;

        Log.d("NUM", ""+result);
//        Intent intent = getActivity().getIntent();
//        manager.getArtInfoList(intent.getStringExtra("Category"), intent.getStringExtra("Type"), totalPost);
        manager.getArtInfoList(mItems[0], mItems[1], totalPost);
    }

    @Override
    public void onItemClick(int position, Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position, List<String> mList) {
        //Has to be empty
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.top_menu_button){
            if (getActivity() instanceof ArtListActivity){
                ((ArtListActivity) getActivity()).openMenuFragment();
            }
        }
        else if (id == R.id.back_button){
            getActivity().finish();
        }
    }

    /***End***/
}