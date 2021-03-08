package sg.asmallmuseum.presentation.UserProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;

public class UserProfileActivity extends AppCompatActivity {
    protected static final int REQUEST_PASSWORD = 3601;
    protected static final int REQUEST_INFO = 3602;
    protected static final int REQUEST_END = 3603;

    private UserProfileFragment mUserProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        UserProfileViewModel viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        mUserProfileFragment = new UserProfileFragment();

        Intent intent = getIntent();
        User user = (User)intent.getSerializableExtra("getUser");
        String type = intent.getStringExtra("type");

        viewModel.setUser(user);
        viewModel.setType(type);

        initFragment();
    }

    private void initFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.user_profile_container, mUserProfileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void replaceFragment(int request){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (request){
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

}