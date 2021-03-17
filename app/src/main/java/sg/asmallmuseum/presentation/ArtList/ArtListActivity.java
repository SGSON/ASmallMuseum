package sg.asmallmuseum.presentation.ArtList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.General.MainMenuFragment;
import sg.asmallmuseum.presentation.General.MainMenuViewModel;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ArtListActivity extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    private MainMenuViewModel menuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_list);

        mAuth = FirebaseAuth.getInstance();
        menuViewModel = new ViewModelProvider(this).get(MainMenuViewModel.class);
        initFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        menuViewModel.setFirebaseUser(user);
    }

    public void initFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.art_list_container, new ArtListFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openMenuFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.art_list_container, new MainMenuFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
