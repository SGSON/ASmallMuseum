package sg.asmallmuseum.presentation.General;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;

public class MainMenuFragment extends Fragment implements View.OnClickListener {

    private final int MENU_LIST = 0;
    private final int MENU_ITEM = 1;

    private View view;

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
        setMenuList(mCategory, MENU_LIST);
        setMenuList(mItem, MENU_ITEM);

        Button mClose = (Button) view.findViewById(R.id.fragment_main_menu_close);
        mClose.setOnClickListener(this);

        return view;
    }

    private void setProfileContainer(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    }

    private void setMenuList(RecyclerView recyclerView, int type){
        MainMenuAdapter adapter = new MainMenuAdapter(type);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();
        if (id == R.id.fragment_main_menu_close){
            //close
        }
    }
}