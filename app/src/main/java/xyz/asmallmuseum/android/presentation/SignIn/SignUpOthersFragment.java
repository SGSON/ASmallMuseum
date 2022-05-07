package xyz.asmallmuseum.android.presentation.SignIn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import xyz.asmallmuseum.android.Domain.Messages.CustomException;
import xyz.asmallmuseum.android.Domain.RequestCode;
import xyz.asmallmuseum.R;
import xyz.asmallmuseum.android.logic.UserManager;
import xyz.asmallmuseum.android.logic.ValidateUser;

public class SignUpOthersFragment extends Fragment {

    private View view;

    private UserManager mManager;
    private String mType;

    public SignUpOthersFragment() {
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
        view = inflater.inflate(R.layout.fragment_sign_up_others, container, false);

        EditText nick = view.findViewById(R.id.fragment_sign_up_others_user_nick_name);
        EditText lastName = view.findViewById(R.id.fragment_sign_up_others_user_last_name);
        EditText firstName = view.findViewById(R.id.fragment_sign_up_others_user_first_name);
        EditText birth = view.findViewById(R.id.fragment_sign_up_others_user_birth);
        Button submitBtn = view.findViewById(R.id.fragment_sign_up_others_submit_button);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //UserManager userManager = new UserManager();
                String uNick = nick.getText().toString();
                String uLastName = lastName.getText().toString();
                String uFirstName = firstName.getText().toString();
                String uEmail= user.getEmail();
                String uBirth = birth.getText().toString();

                try {
                    ValidateUser.validUser(uNick,uLastName,uFirstName,uBirth);
                    mManager.addNewUser(uNick, uLastName, uFirstName, uEmail, uBirth, mType);
                    if (getActivity() instanceof SignInActivity){
                        ((SignInActivity) getActivity()).replaceFragment(RequestCode.REQUEST_END_SIGN_UP);
                    }
                } catch (CustomException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SignInViewModel viewModel = new ViewModelProvider(requireActivity()).get(SignInViewModel.class);

        getManager(viewModel);
        getType(viewModel);
    }

    private void getManager(SignInViewModel viewModel){
       viewModel.getManager().observe(getViewLifecycleOwner(), new Observer<UserManager>() {
           @Override
           public void onChanged(UserManager userManager) {
                mManager = userManager;
           }
       });
       viewModel.getType().observe(getViewLifecycleOwner(), new Observer<String>() {
           @Override
           public void onChanged(String s) {
               mType = s;
           }
       });
    }

    private void getType(SignInViewModel viewModel){
        viewModel.getType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String type) {
                mType = type;
            }
        });
    }
}