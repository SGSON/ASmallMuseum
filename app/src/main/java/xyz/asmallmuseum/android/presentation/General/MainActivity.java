package xyz.asmallmuseum.android.presentation.General;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import xyz.asmallmuseum.android.domain.User;
import xyz.asmallmuseum.android.domain.Values;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.logic.UserManager;
import xyz.asmallmuseum.android.persistence.UserPreference;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.UserLoadListener;
import xyz.asmallmuseum.android.presentation.SignIn.SignInEmailVerifyFragment;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements UserLoadListener {
    private FirebaseAuth mAuth;
    private final int REQUEST_CODE = 1;
    private MainMenuViewModel viewModel;
    private Fragment mMainFragment;
    private Fragment mMainMenuFragment;

    private FirebaseUser mFirebaseUser;
    private boolean mInit = true;

    public static UserPreference userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userPreference = new UserPreference(getSharedPreferences("USER", Context.MODE_PRIVATE));

        //will be move to the upload activity
        //networkConnection();
        //requestPermission();
        mAuth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this).get(MainMenuViewModel.class);

        mMainFragment = new MainFragment();
        mMainMenuFragment = new MainMenuFragment();

        replaceFragment(null);
    }

    /***Verify a signed-in user***/
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseUser = mAuth.getCurrentUser();
        viewModel.setFirebaseUser(mFirebaseUser);

        if (mFirebaseUser != null){
            UserManager userManager = new UserManager();
            userManager.setUserLoadListener(this);
            userManager.getUserInfo(mFirebaseUser.getEmail(), 0);
        }

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_main_container, mMainFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void openMenuFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_main_container, mMainMenuFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void openVerifyFragment(){
        //replaceFragment(null);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_main_container, new SignInEmailVerifyFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /***Get a storage access permission***/
    /*private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

        } *//*else if (shouldShowRequestPermissionRationale(...)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            showInContextUI(...);*//*
        else {
            // You can directly ask for the permission.
            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_CODE);
        }
    }*/
    /***End***/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() <= 0){
            finish();
        }
    }

/*
    private void networkConnection(){
        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connManager.getActiveNetworkInfo();
        if (network == null && !network.isConnectedOrConnecting()){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(R.string.internet_connection_fail)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
        }
    }*/

    @Override
    public void userInfo(User user) {
        if (user.getuType().equals(Values.USER_TYPE_EMAIL) && !mFirebaseUser.isEmailVerified()){
            getSupportFragmentManager().popBackStack();
            openVerifyFragment();
        }
    }

    @Override
    public void getAllUser(List<String> list) {

    }
}