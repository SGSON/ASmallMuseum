package xyz.asmallmuseum.android.presentation.UserProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import xyz.asmallmuseum.android.domain.RequestCode;
import xyz.asmallmuseum.android.domain.User;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.logic.UserManager;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.UserLoadListener;
import xyz.asmallmuseum.android.presentation.General.MainMenuFragment;
import xyz.asmallmuseum.android.presentation.General.MainMenuViewModel;

public class UserProfileActivity extends AppCompatActivity implements UserLoadListener {
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
            case RequestCode.REQUEST_PROFILE:
                fragmentTransaction.replace(R.id.user_profile_container, mUserProfileFragment);
                break;
            case RequestCode.REQUEST_PASSWORD:
                fragmentTransaction.replace(R.id.user_profile_container, new UserProfileUpdatePasswordFragment());
                break;
            case RequestCode.REQUEST_INFO:
                fragmentTransaction.replace(R.id.user_profile_container, new UserProfileUpdateInfoFragment());
                break;
            case RequestCode.REQUEST_END:
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
