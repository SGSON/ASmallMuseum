package sg.asmallmuseum.presentation.SignIn;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;

public class SignInEmailVerifyFragment extends Fragment  implements View.OnClickListener {

    private View view;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private User mUser;
    private String mType;

    public SignInEmailVerifyFragment() {
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
        view = inflater.inflate(R.layout.fragment_sign_in_email_verify, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        Button resendEmail = (Button) view.findViewById(R.id.fragment_sign_in_email_verify_resend);
        Button next = (Button) view.findViewById(R.id.fragment_sign_in_email_verify_next);

        resendEmail.setOnClickListener(this);
        next.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SignInViewModel viewModel = new ViewModelProvider(requireActivity()).get(SignInViewModel.class);
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                mUser = user;
            }
        });
    }

    public void sendEmailVerification() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getActivity(),"Success, confirm your email",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_sign_in_email_verify_resend){
            sendEmailVerification();
        }
        else if (id == R.id.fragment_sign_in_email_verify_next){
            mFirebaseUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (mFirebaseUser.isEmailVerified()){
                        UserManager userManager = new UserManager();
                        userManager.addNewUser(mUser);
                        if (getActivity() instanceof SignInActivity){
                            ((SignInActivity) getActivity()).replaceFragment(SignInActivity.REQUEST_CODE_END_SIGN_UP);
                        }
                    }
                    else {
                        //show alert
                    }
                }
            });
        }
    }
}