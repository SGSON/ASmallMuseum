package sg.asmallmuseum.presentation.UserProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserLoadListener;
import sg.asmallmuseum.presentation.General.MainMenuFragment;
import sg.asmallmuseum.presentation.General.MainMenuViewModel;

public class UserProfileActivity extends AppCompatActivity implements UserLoadListener {
    protected static final int REQUEST_PASSWORD = 3601;
    protected static final int REQUEST_INFO = 3602;
    protected static final int REQUEST_END = 3603;
    protected static final int REQUEST_PROFILE = 3604;

    private UserProfileFragment mUserProfileFragment;
    private MainMenuViewModel mMainMenuViewModel;
    private UserProfileViewModel viewModel;
    private UserProfileViewHistoryFragment mUserHistoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mMainMenuViewModel = new ViewModelProvider(this).get(MainMenuViewModel.class);
        mMainMenuViewModel.setFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());

        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        mUserProfileFragment = new UserProfileFragment();
        mUserHistoryFragment = new UserProfileViewHistoryFragment();

        initFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        getUserInfo(email);
    }

    private void getUserInfo(String email){
        UserManager manager = new UserManager();
        manager.setUserLoadListener(this);

        manager.getUserInfo(email, 0);
    }

    private void initFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.user_profile_container, mUserHistoryFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void replaceFragment(int request){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (request){
            case REQUEST_PROFILE:
                fragmentTransaction.replace(R.id.user_profile_container, mUserProfileFragment);
                break;
            case REQUEST_PASSWORD:
                fragmentTransaction.replace(R.id.user_profile_container, new UserProfileUpdatePasswordFragment());
                break;
            case REQUEST_INFO:
                fragmentTransaction.replace(R.id.user_profile_container, new UserProfileUpdateInfoFragment());
                break;
            case REQUEST_END:
                fragmentManager.popBackStack();
                break;
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openMainMenu(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.user_profile_container, new MainMenuFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void userInfo(User user) {
        viewModel.setUser(user);
    }

    @Override
    public void getAllUser(List<String> list) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() <= 1){
            finish();
        }
        else {
            fragmentManager.popBackStack();
        }
    }
}
