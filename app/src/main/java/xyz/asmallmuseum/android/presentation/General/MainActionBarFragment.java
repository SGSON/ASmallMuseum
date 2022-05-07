package xyz.asmallmuseum.android.presentation.General;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import xyz.asmallmuseum.R;

/**
 * never used
 * */

public class MainActionBarFragment extends Fragment implements View.OnClickListener{

    private View view;

    public MainActionBarFragment() {
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
        view = inflater.inflate(R.layout.fragment_main_action_bar, container, false);
        Button mMenuButton = (Button) view.findViewById(R.id.fragment_main_action_bar_menu_button);
        Button mBackButton = (Button) view.findViewById(R.id.fragment_main_action_bar_back_button);

        mMenuButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_main_action_bar_back_button){
            getParentFragmentManager().popBackStack();
        }
        else if (id == R.id.fragment_main_action_bar_menu_button){

        }
    }
}