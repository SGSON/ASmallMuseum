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
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.Messages.UserEmailError;
import sg.asmallmuseum.Domain.Messages.UserPasswordError;
import sg.asmallmuseum.Domain.RequestCode;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.Domain.Values;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.logic.ValidateUser;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserLoadListener;


public class SignInFragment extends Fragment implements View.OnClickListener, UserLoadListener {

    private View view;
    private SignInViewModel viewModel;

    private FirebaseAuth mAuth;
    private UserManager userManager;

    private FirebaseUser user;
    private CallbackManager callbackManager;



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
        setFacebook();

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
        else if (id == R.id.fragment_sign_in_sign_up){
            if (getActivity() instanceof SignInActivity){
                ((SignInActivity) getActivity()).replaceFragment(RequestCode.REQUEST_GET_EMAIL);
            }
        }

        else if (id == R.id.fragment_sign_in_google){
            googleSignIn();
        }
        else if (id == R.id.back_button){
            getParentFragmentManager().popBackStack();
        }
        else if (id == R.id.top_menu_button){
            if (getActivity() instanceof SignInActivity){
                ((SignInActivity) getActivity()).replaceFragment(RequestCode.REQUEST_MENU);
            }
        }
    }

    private void setButtons(){
        Button loginBtn = (Button) view.findViewById(R.id.fragment_sign_in_button);

        SignInButton googleLoginBtn = (SignInButton) view.findViewById(R.id.fragment_sign_in_google);
        Button signUpBtn = (Button) view.findViewById(R.id.fragment_sign_in_sign_up);

        Button mBack = (Button) view.findViewById(R.id.back_button);
        Button mMenu = (Button) view.findViewById(R.id.top_menu_button);

        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        googleLoginBtn.setOnClickListener(this);

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

        if (requestCode == RequestCode.REQUEST_OTHER_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Google login", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google login", "Google sign in failed", e);
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
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

    //Sign-In Google
    private void googleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RequestCode.REQUEST_OTHER_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            viewModel.setType(Values.USER_TYPE_GOOGLE);
                            moveNextPage(user);
                            //userManager.getUserInfo(user.getEmail());
                            Log.d("Google login", "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Google login", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    //Sign-In Facebook
    private void setFacebook(){
        callbackManager = CallbackManager.Factory.create();
        LoginButton facebookLoginBtn = (LoginButton) view.findViewById(R.id.fragment_sign_in_facebook);
        facebookLoginBtn.setPermissions("email","public_profile");

        // Callback registration
        facebookLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebookLogin", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("facebookLogin", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebookLogin", "facebook:onError", error);
                // ...
            }
        });
    }

    protected void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( getActivity(),new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            viewModel.setType(Values.USER_TYPE_FACEBOOK);
                            moveNextPage(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Facebook", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}