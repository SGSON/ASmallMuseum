package xyz.asmallmuseum.android.presentation.SignIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import xyz.asmallmuseum.android.domain.RequestCode;
import xyz.asmallmuseum.android.domain.User;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.presentation.General.MainMenuFragment;
import xyz.asmallmuseum.android.presentation.General.MainMenuViewModel;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.UserLoadListener;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class SignInActivity extends AppCompatActivity implements UserLoadListener {

    private MainMenuViewModel mainMenuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SignInViewModel viewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        mainMenuViewModel = new ViewModelProvider(this).get(MainMenuViewModel.class);

        replaceFragment(RequestCode.REQUEST_INIT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mainMenuViewModel.setFirebaseUser(mUser);
        if (mUser != null){
            replaceFragment(RequestCode.REQUEST_END);
        }
    }

    public void replaceFragment(int request_code){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (request_code){
            case RequestCode.REQUEST_INIT:
                fragmentTransaction.replace(R.id.sign_in_container, new SignInFragment());
                break;
            case RequestCode.REQUEST_EMAIL_SIGN_UP:
                fragmentTransaction.replace(R.id.sign_in_container, new SignUpFragment());
                break;
            case RequestCode.REQUEST_OTHERS:
                fragmentTransaction.replace(R.id.sign_in_container, new SignUpOthersFragment());
                break;
            case RequestCode.REQUEST_GET_EMAIL:
                fragmentTransaction.replace(R.id.sign_in_container, new SignUpGetEmailFragment());
                break;
            case RequestCode.REQUEST_MENU:
                fragmentTransaction.replace(R.id.sign_in_container, new MainMenuFragment());
                break;
            case RequestCode.REQUEST_EMAIL_VERIFY:
                fragmentTransaction.replace(R.id.sign_in_container, new SignInEmailVerifyFragment());
                break;
            case RequestCode.REQUEST_END_SIGN_UP:
                fragmentTransaction.replace(R.id.sign_in_container, new SignInWelcomeFragment());
                break;
            case RequestCode.REQUEST_END:
                finish();
                break;
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void userInfo(User users) {

    }

    @Override
    public void getAllUser(List<String> list) {

    }
}