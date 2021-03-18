package sg.asmallmuseum.presentation.SignIn;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import sg.asmallmuseum.presentation.General.MainActivity;

public class SignInEmailVerifyFragment extends Fragment  implements View.OnClickListener {

    private View view;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

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
        Button singout = (Button) view.findViewById(R.id.fragment_sign_in_email_verify_sign_out);

        resendEmail.setOnClickListener(this);
        next.setOnClickListener(this);
        singout.setOnClickListener(this);

        return view;
    }

    private void reloadUser(){
        mFirebaseUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mFirebaseUser.isEmailVerified()){
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

    public void sendEmailVerification() {

        mFirebaseUser.sendEmailVerification()
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
            reloadUser();
        }
        else if (id == R.id.fragment_sign_in_email_verify_sign_out){
            mAuth.signOut();
            if (! (getActivity() instanceof MainActivity)){
                getActivity().finish();
            }
            else{
                ((MainActivity) getActivity()).replaceFragment(null);
            }
        }
    }
}