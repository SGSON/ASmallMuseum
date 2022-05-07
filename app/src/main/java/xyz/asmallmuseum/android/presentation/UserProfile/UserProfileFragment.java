package xyz.asmallmuseum.android.presentation.UserProfile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import xyz.asmallmuseum.android.Domain.RequestCode;
import xyz.asmallmuseum.android.Domain.User;
import xyz.asmallmuseum.android.Domain.Values;
import xyz.asmallmuseum.R;

public class UserProfileFragment extends Fragment implements View.OnClickListener{

    private View view;
    private UserProfileViewModel viewModel;

    private User user;
    private String type;

    public UserProfileFragment() {
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
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        Button updatePass = (Button) view.findViewById(R.id.fragment_user_profile_update_password_button);
        Button home = (Button) view.findViewById(R.id.fragment_user_profile_home_button);
        Button updateInfo = (Button) view.findViewById(R.id.fragment_user_profile_update_user_information_button);

        updatePass.setOnClickListener(this);
        home.setOnClickListener(this);
        updateInfo.setOnClickListener(this);

        Button mBack = view.findViewById(R.id.back_button);
        Button mMenu = view.findViewById(R.id.top_menu_button);

        mBack.setOnClickListener(this);
        mMenu.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        getProfileFromViewModel();
    }

    private void getProfileFromViewModel(){
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    setTexts(user);
                }
            }
        });
    }

    private void setTexts(User user){
        this.user = user;
        setType(user.getuType());

        TextView nickname = (TextView) view.findViewById(R.id.fragment_user_profile_user_nickname);
        TextView firstName = (TextView) view.findViewById(R.id.fragment_user_profile_user_firstname);
        TextView lastName = (TextView) view.findViewById(R.id.fragment_user_profile_user_lastname);
        TextView birth = (TextView) view.findViewById(R.id.fragment_user_profile_user_birth);

        nickname.setText(user.getuNick());
        firstName.setText(user.getuFirstName());
        lastName.setText(user.getuLastName());
        birth.setText(user.getuBirth());
    }

    private void setType(String type){
        this.type = type;
        if (!this.type.equals(Values.USER_TYPE_EMAIL)){
            Button updatePass = (Button) view.findViewById(R.id.fragment_user_profile_update_password_button);
            updatePass.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fragment_user_profile_update_password_button){
            if (getActivity() instanceof UserProfileActivity){
                ((UserProfileActivity) getActivity()).replaceFragment(RequestCode.REQUEST_PASSWORD);
            }
        }
        else if (id == R.id.fragment_user_profile_home_button){
            getActivity().finish();
        }
        else if (id == R.id.fragment_user_profile_update_user_information_button){
            if (getActivity() instanceof UserProfileActivity){
                ((UserProfileActivity) getActivity()).replaceFragment(RequestCode.REQUEST_INFO);
            }
        }
        else if (id == R.id.top_menu_button){
            if (getActivity() instanceof UserProfileActivity){
                ((UserProfileActivity) getActivity()).openMainMenu();
            }
        }
        else if (id == R.id.back_button){
            getParentFragmentManager().popBackStack();
        }
    }
}