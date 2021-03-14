package sg.asmallmuseum.presentation.SignIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.General.MainMenuFragment;
import sg.asmallmuseum.presentation.General.MainMenuViewModel;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserLoadListener;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class SignInActivity extends AppCompatActivity implements UserLoadListener {

    protected static final int REQUEST_CODE_INIT = 5500;
    protected static final int REQUEST_CODE_EMAIL_SIGN_UP = 5501;
    protected static final int REQUEST_CODE_OTHERS = 5502;
    protected static final int REQUEST_CODE_GET_EMAIL = 5503;
    protected static final int REQUEST_CODE_END_SIGN_UP = 5504;
    protected static final int REQUEST_CODE_MENU = 5505;
    protected static final int REQUEST_CODE_EMAIL_VERIFY = 5506;
    protected static final int REQUEST_CODE_END = 5507;

    private MainMenuViewModel mainMenuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SignInViewModel viewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        mainMenuViewModel = new ViewModelProvider(this).get(MainMenuViewModel.class);

        replaceFragment(REQUEST_CODE_INIT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mainMenuViewModel.setFirebaseUser(mUser);
        if (mUser != null){
            replaceFragment(REQUEST_CODE_END);
        }
    }

    public void replaceFragment(int request_code){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (request_code){
            case REQUEST_CODE_INIT:
                fragmentTransaction.replace(R.id.sign_in_container, new SignInFragment());
                break;
            case REQUEST_CODE_EMAIL_SIGN_UP:
                fragmentTransaction.replace(R.id.sign_in_container, new SignUpFragment());
                break;
            case REQUEST_CODE_OTHERS:
                fragmentTransaction.replace(R.id.sign_in_container, new SignUpOthersFragment());
                break;
            case REQUEST_CODE_GET_EMAIL:
                fragmentTransaction.replace(R.id.sign_in_container, new SignUpGetEmailFragment());
                break;
            case REQUEST_CODE_MENU:
                fragmentTransaction.replace(R.id.sign_in_container, new MainMenuFragment());
                break;
            case REQUEST_CODE_EMAIL_VERIFY:
                fragmentTransaction.replace(R.id.sign_in_container, new SignInEmailVerifyFragment());
                break;
            case REQUEST_CODE_END_SIGN_UP:
                fragmentTransaction.replace(R.id.sign_in_container, new SignInWelcomeFragment());
                break;
            case REQUEST_CODE_END:
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