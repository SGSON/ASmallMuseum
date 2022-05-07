package xyz.asmallmuseum.android.presentation.General;

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

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import xyz.asmallmuseum.android.Domain.User;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.presentation.ArtUpload.ArtUploadPageActivity;
import xyz.asmallmuseum.android.presentation.UserProfile.UserProfileActivity;


public class MainMenuProfileFragment extends Fragment implements View.OnClickListener{

    private View view;
    private User mUser;
    private String mType;

    private FirebaseAuth mAuth;
    private MainMenuViewModel viewModel;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainMenuViewModel.class);
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setUser(user);
            }
        });
        viewModel.getType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                setType(s);
            }
        });
    }

    private void setUser(User mUser){
        this.mUser = mUser;
    }

    private void setType(String mType){
        this.mType = mType;
    }

    private void setButtons(){
        Button mPosting = (Button) view.findViewById(R.id.fragment_main_menu_profile_posting_button);
        Button mProfile = (Button) view.findViewById(R.id.fragment_main_menu_profile_view_profile_button);
        Button mSupport = (Button) view.findViewById(R.id.fragment_main_menu_profile_support_button);
        Button mSignOut = (Button) view.findViewById(R.id.fragment_main_menu_profile_sign_out_button);

        mProfile.setOnClickListener(this);
        mPosting.setOnClickListener(this);
        mSupport.setOnClickListener(this);
        mSignOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_main_menu_profile_posting_button){
            Intent intent = new Intent(getContext(), ArtUploadPageActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.fragment_main_menu_profile_view_profile_button){
            Intent intent = new Intent(getContext(), UserProfileActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.fragment_main_menu_profile_support_button){
            Intent intent = new Intent(getContext(), SupportActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.fragment_main_menu_profile_sign_out_button){
            FirebaseAuth.getInstance().signOut();
            viewModel.setFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
            getParentFragmentManager().popBackStack();
        }
    }
}