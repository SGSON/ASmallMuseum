package sg.asmallmuseum.presentation.General;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    private final int REQUEST_CODE = 1;
    private MainMenuViewModel viewModel;
    private Fragment mMainFragment;
    private Fragment mMainMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //will be move to the upload activity
        requestPermission();
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
        FirebaseUser user = mAuth.getCurrentUser();
        viewModel.setFirebaseUser(user);
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

    /***Get a storage access permission***/
    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

        } /*else if (shouldShowRequestPermissionRationale(...)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            showInContextUI(...);*/
        else {
            // You can directly ask for the permission.
            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_CODE);
        }
    }
    /***End***/
}