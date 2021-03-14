package sg.asmallmuseum.presentation.SignIn;

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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;


public class SignInWelcomeFragment extends Fragment implements View.OnClickListener{

    private View view;

    private UserManager mManager;
    private String mType;

    public SignInWelcomeFragment() {
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
        view = inflater.inflate(R.layout.fragment_sign_in_welcome, container, false);

        TextView userNickname = view.findViewById(R.id.fragment_sign_in_welcome_user_nick);
        Button homeBtn = view.findViewById(R.id.fragment_sign_in_welcome_home);

        homeBtn.setOnClickListener(this);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SignInViewModel viewModel = new ViewModelProvider(requireActivity()).get(SignInViewModel.class);

        getManager(viewModel);
        getType(viewModel);
    }

    private void getType(SignInViewModel viewModel){
        viewModel.getManager().observe(getViewLifecycleOwner(), new Observer<UserManager>() {
            @Override
            public void onChanged(UserManager userManager) {
                mManager = userManager;
            }
        });
    }

    private void getManager(SignInViewModel viewModel){
        viewModel.getType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mType = s;
            }
        });
    }

    private void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setMessage("Click button after confirm your email");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_sign_in_welcome_home){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(mType.equals("email")){
                if(user.isEmailVerified()){
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    mManager.getUserInfo(user.getEmail(), 0);

                }else {
                    alert();
                }

            }else if(!mType.equals("email")){
                user = FirebaseAuth.getInstance().getCurrentUser();
                mManager.getUserInfo(user.getEmail(), 0);

            }
        }
    }
}