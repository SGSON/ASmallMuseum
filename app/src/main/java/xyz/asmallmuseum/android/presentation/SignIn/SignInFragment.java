package xyz.asmallmuseum.android.presentation.SignIn;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.lifecycle.ViewModelProvider;

import xyz.asmallmuseum.android.customview.SnsLoginButtonView;
import xyz.asmallmuseum.android.domain.Messages.CustomException;
import xyz.asmallmuseum.android.domain.Messages.UserEmailError;
import xyz.asmallmuseum.android.domain.Messages.UserPasswordError;
import xyz.asmallmuseum.android.domain.RequestCode;
import xyz.asmallmuseum.android.domain.User;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.logic.UserManager;
import xyz.asmallmuseum.android.logic.ValidateUser;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.UserLoadListener;


public class SignInFragment extends Fragment implements View.OnClickListener, UserLoadListener, SnsLoginButtonView.OnClickListener {

    private View view;
    private SignInViewModel viewModel;

    private FirebaseAuth mAuth;
    private UserManager userManager;

    private FirebaseUser user;



    public SignInFragment() {
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

        view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mAuth = FirebaseAuth.getInstance();

        userManager = new UserManager();
        userManager.setUserLoadListener(this);

        setButtons();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SignInViewModel.class);
        viewModel.setManager(userManager);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_sign_in_button){
            getUserAccount();
        }
        else if (id == R.id.back_button){
            getActivity().finish();
        }
        else if (id == R.id.top_menu_button){
            if (getActivity() instanceof SignInActivity){
                ((SignInActivity) getActivity()).replaceFragment(RequestCode.REQUEST_MENU);
            }
        }
    }

    private void setButtons(){
        Button loginBtn = (Button) view.findViewById(R.id.fragment_sign_in_button);

        SnsLoginButtonView signUpBtn = (SnsLoginButtonView) view.findViewById(R.id.fragment_sign_in_sign_up);
        SnsLoginButtonView googleLoginBtn = (SnsLoginButtonView) view.findViewById(R.id.fragment_sign_in_google);
        SnsLoginButtonView naverLoginBtn = (SnsLoginButtonView) view.findViewById(R.id.fragment_sign_in_naver);
        SnsLoginButtonView kakaoLoginBtn = (SnsLoginButtonView) view.findViewById(R.id.fragment_sign_in_kakao);

        Button mBack = (Button) view.findViewById(R.id.back_button);
        Button mMenu = (Button) view.findViewById(R.id.top_menu_button);

        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener((SnsLoginButtonView.OnClickListener)this);
        googleLoginBtn.setOnClickListener((SnsLoginButtonView.OnClickListener)this);
        naverLoginBtn.setOnClickListener((SnsLoginButtonView.OnClickListener)this);
        kakaoLoginBtn.setOnClickListener((SnsLoginButtonView.OnClickListener)this);

        mBack.setOnClickListener(this);
        mMenu.setOnClickListener(this);

    }

    @Override
    public void getAllUser(List<String> list) {

    }

    /**
     * SIGN-IN METHODS
     * */
    @Override
    public void userInfo(User user) {
        if (getActivity() instanceof SignInActivity) {
            if (user == null) {
                ((SignInActivity) getActivity()).replaceFragment(RequestCode.REQUEST_OTHERS);
            }
            else if (user != null && !mAuth.getCurrentUser().isEmailVerified()){
                ((SignInActivity) getActivity()).replaceFragment(RequestCode.REQUEST_EMAIL_VERIFY);
            }
            else {
                ((SignInActivity) getActivity()).replaceFragment(RequestCode.REQUEST_END);
            }
        }
    }

    private void moveNextPage(FirebaseUser user){
        userManager.exists(user.getEmail());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RequestCode.REQUEST_OTHER_SIGN_IN){
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d("Google login", "firebaseAuthWithGoogle:" + account.getId());
//                firebaseAuthWithGoogle(account.getIdToken());
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w("Google login", "Google sign in failed", e);
//            }
//        }
//        else {
////            callbackManager.onActivityResult(requestCode, resultCode, data);
//        }
    }

    //Sign-In Email
    private void getUserAccount(){
        EditText emailID = (EditText) view.findViewById(R.id.fragment_sign_in_email);
        EditText password= (EditText) view.findViewById(R.id.fragment_sign_in_password);

        String uEmailID = emailID.getText().toString();
        String uPassword = password.getText().toString();
        try{
            ValidateUser.validEmail(uEmailID);
            ValidateUser.validPassword(uPassword);
            signIn(uEmailID,uPassword);
        }
        catch(CustomException e){
            if (e instanceof UserEmailError){
                emailID.setError("Invalid");
            }
            else if (e instanceof UserPasswordError){
                password.setError("Password Error");
            }
        }

    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userManager.getUserInfo(email, 0);
                        }
                        else {
                            Toast.makeText(getActivity(), "Invalid user Email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(SnsLoginButtonView.SnsLoginType snsType) {
        switch (snsType) {
            case EMAIL:
                if (getActivity() instanceof SignInActivity){
                    ((SignInActivity) getActivity()).replaceFragment(RequestCode.REQUEST_GET_EMAIL);
                }
                break;
            case GOOGLE:
                break;
            case NAVER:
                break;
            case KAKAO:
                break;
        }
    }

    //Sign-In Google
//    private void googleSignIn(){
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
//
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RequestCode.REQUEST_OTHER_SIGN_IN);
//    }
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            user = mAuth.getCurrentUser();
//                            viewModel.setType(Values.USER_TYPE_GOOGLE);
//                            moveNextPage(user);
//                            //userManager.getUserInfo(user.getEmail());
//                            Log.d("Google login", "signInWithCredential:success");
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("Google login", "signInWithCredential:failure", task.getException());
//
//                        }
//                    }
//                });
//    }


}