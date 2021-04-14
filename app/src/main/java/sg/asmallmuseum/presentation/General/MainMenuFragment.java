package sg.asmallmuseum.presentation.General;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Values;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.ArtList.ArtListActivity;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.MainMenuOnClickListener;

public class MainMenuFragment extends Fragment implements View.OnClickListener {

    private final int MENU_LIST = 0;
    private final int MENU_ITEM = 1;

    private View view;
    private MainMenuViewModel viewModel;
    private MainMenuAdapter mCateAdapter;
    private MainMenuAdapter mTypeAdapter;

    private Map<String, String> map;

    public MainMenuFragment() {
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
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        RecyclerView mCategory = (RecyclerView) view.findViewById(R.id.fragment_main_menu_list_menu);
        RecyclerView mItem = (RecyclerView) view.findViewById(R.id.fragment_main_menu_item_list);
        mCateAdapter = setMenuList(mCategory, MENU_LIST);
        mTypeAdapter = setMenuList(mItem, MENU_ITEM);

        Button mClose = (Button) view.findViewById(R.id.fragment_main_menu_close);
        mClose.setOnClickListener(this);

        map = new HashMap<>();
        map.put(Values.ART_CATEGORY, Values.ART_FINE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setProfileContainer(FirebaseAuth.getInstance().getCurrentUser());
//        viewModel = new ViewModelProvider(requireActivity()).get(MainMenuViewModel.class);
//        viewModel.getFirebaseUser().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
//            @Override
//            public void onChanged(FirebaseUser firebaseUser) {
//                setProfileContainer(firebaseUser);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfileContainer(FirebaseAuth.getInstance().getCurrentUser());
    }

    private void setProfileContainer(FirebaseUser user){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (user == null){
            fragmentTransaction.replace(R.id.fragment_main_menu_container, new MainMenuWelcomeFragment());
        }
        else {
            fragmentTransaction.replace(R.id.fragment_main_menu_container, new MainMenuProfileFragment());
        }
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private MainMenuAdapter setMenuList(RecyclerView recyclerView, int type){
        MainMenuAdapter adapter = new MainMenuAdapter(type, getActivity());
        adapter.setRecyclerViewOnClickListener(type, new MainMenuOnClickListener() {
            @Override
            public void onItemClick(String item, int request_code) {
                if (request_code == MENU_LIST){
                    setMenuItem(item);
                }
                else {
                    initNextActivity(item);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return adapter;
    }

    private void closeFragment(){
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();
        if (id == R.id.fragment_main_menu_close){
            closeFragment();
        }
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
        return list;
    }

    private void setMenuItem(String item){
        List<String> mList = getItemList(item);
        map.put(Values.ART_CATEGORY, item);
        mTypeAdapter.updateList(mList);
    }

    private void initNextActivity(String item){
        if (getActivity() instanceof ArtListActivity){
            ((ArtListActivity) getActivity()).openNewFragment(map.get(Values.ART_CATEGORY), item);
        }
        else if (map.containsKey(Values.ART_CATEGORY)){
            Intent intent = new Intent(getActivity(), ArtListActivity.class);
            intent.putExtra(Values.ART_CATEGORY, map.get(Values.ART_CATEGORY));
            intent.putExtra(Values.ART_TYPE, item);

            startActivity(intent);
            //closeFragment();
        }
    }
}