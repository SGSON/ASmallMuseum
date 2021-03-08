package sg.asmallmuseum.presentation.UserProfile;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;


public class UserProfileUpdateInfoFragment extends Fragment implements View.OnClickListener{

    private View view;
    private UserProfileViewModel viewModel;

    private UserManager userManager;
    private FirebaseUser fUser;
    private EditText nickname;
    private EditText lastName;
    private EditText firstName;
    private EditText birth;
    private String type;

    public UserProfileUpdateInfoFragment() {
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
        view = inflater.inflate(R.layout.fragment_user_profile_update_info, container, false);

        Button backBtn = (Button) view.findViewById(R.id.home_button);
        Button updateBtn = (Button) view.findViewById(R.id.update_user_information);

        backBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    setTexts(user);
                }
            }
        });

        viewModel.getType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null){
                    setType(s);
                }
            }
        });

    }

    private void setTexts(User user){
        nickname = (EditText) view.findViewById(R.id.user_nickname);
        lastName = (EditText) view.findViewById(R.id.user_lastname);
        firstName = (EditText) view.findViewById(R.id.user_firstname);
        birth = (EditText) view.findViewById(R.id.user_birth);

        nickname.setText(user.getuNick());
        firstName.setText(user.getuFirstName());
        lastName.setText(user.getuLastName());
        birth.setText(user.getuBirth());
    }

    private void setType(String type){
        this.type = type;

        if(type.equals("email")){
            userManager = new UserManager("email");
        }else if(type.equals("facebook")){
            userManager = new UserManager("facebook");
        }else if(type.equals("google")){
            userManager = new UserManager("google");
        }
    }


    public void alertMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setMessage("change your profile ?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fUser = FirebaseAuth.getInstance().getCurrentUser();
                String uEmail = fUser.getEmail();
                String uNickname = nickname.getText().toString();
                String uLastName = lastName.getText().toString();
                String uFirstName = firstName.getText().toString();
                String uBirth = birth.getText().toString();

                try {
                    userManager.addNewUser(uNickname,uLastName,uFirstName,uEmail,uBirth);
                } catch (CustomException e) {
                    e.printStackTrace();
                }

                User tempUser = new User(uNickname,uLastName,uFirstName,uEmail,uBirth);
                viewModel.setUser(tempUser);
                closeWindow();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void closeWindow(){
//        FragmentManager fragmentManager = getParentFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.remove(this);
//        fragmentTransaction.commit();

        if (getActivity() instanceof UserProfileActivity){
            ((UserProfileActivity) getActivity()).replaceFragment(UserProfileActivity.REQUEST_END);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.home_button){
            closeWindow();
        }
        else if (id == R.id.update_user_information){
            alertMessage();
        }
    }
}