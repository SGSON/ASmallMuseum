package sg.asmallmuseum.presentation.General;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sg.asmallmuseum.R;

public class MainMenuWelcomeFragment extends Fragment implements View.OnClickListener{

    private View view;

    public MainMenuWelcomeFragment() {
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
        view = inflater.inflate(R.layout.fragment_main_menu_welcome, container, false);
        Button mSignInButton = (Button) view.findViewById(R.id.fragment_main_menu_welcome_sign_in);
        Button mSupportButton = (Button) view.findViewById(R.id.fragment_main_menu_welcome_support);

        mSignInButton.setOnClickListener(this);
        mSupportButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_main_menu_welcome_sign_in){

        }
        else if (id == R.id.fragment_main_menu_welcome_support){

        }
    }
}