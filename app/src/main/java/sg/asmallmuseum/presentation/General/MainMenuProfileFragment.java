package sg.asmallmuseum.presentation.General;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sg.asmallmuseum.R;


public class MainMenuProfileFragment extends Fragment implements View.OnClickListener{

    private View view;

    public MainMenuProfileFragment() {
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
        view = inflater.inflate(R.layout.fragment_main_menu_profile, container, false);

        setButtons();

        return view;
    }

    private void setButtons(){
        Button mProfile = (Button) view.findViewById(R.id.fragment_main_menu_profile_view_profile_button);
        Button mHistory = (Button) view.findViewById(R.id.fragment_main_menu_profile_view_history_button);
        Button mSupport = (Button) view.findViewById(R.id.fragment_main_menu_profile_support_button);
        Button mSignOut = (Button) view.findViewById(R.id.fragment_main_menu_profile_sign_out_button);

        mProfile.setOnClickListener(this);
        mHistory.setOnClickListener(this);
        mSupport.setOnClickListener(this);
        mSignOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_main_menu_profile_view_profile_button){

        }
        else if (id == R.id.fragment_main_menu_profile_view_history_button){

        }
        else if (id == R.id.fragment_main_menu_profile_support_button){

        }
        else if (id == R.id.fragment_main_menu_profile_sign_out_button){

        }
    }
}