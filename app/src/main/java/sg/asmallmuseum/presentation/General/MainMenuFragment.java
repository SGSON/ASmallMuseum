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
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;

public class MainMenuFragment extends Fragment implements View.OnClickListener {

    private final int MENU_LIST = 0;
    private final int MENU_ITEM = 1;

    private View view;
    private MainMenuViewModel viewModel;
    private MainMenuAdapter mCateAdapter;
    private MainMenuAdapter mTypeAdapter;

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainMenuViewModel.class);
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                setProfileContainer(firebaseUser);
            }
        });
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
        adapter.setRecyclerViewOnClickListener(new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(int position, Intent intent) {

            }

            @Override
            public void onItemClick(int position, List<String> mList) {
                setMenuItem(position);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return adapter;
    }

    private void setInitMenu(){

    }

    private void closeFragment(){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
        fragmentManager.popBackStack();
    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();
        if (id == R.id.fragment_main_menu_close){
            closeFragment();
        }
    }

    private void setMenuItem(int pos){
        List<String> items;
        switch (pos){
            case 1:
                items = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.type_visual)));
                break;
            case 2:
                items = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.type_applied)));
                break;
            case 3:
                items = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.type_others)));
                break;
            case 4:
                items = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.museums)));
                break;
            default:
                items = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.type_fine)));
                break;
        }
        mTypeAdapter.updateList(items);
    }
}