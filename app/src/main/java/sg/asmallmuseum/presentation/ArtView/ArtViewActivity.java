package sg.asmallmuseum.presentation.ArtView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.R;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ArtViewActivity extends AppCompatActivity {
    private Fragment mArtViewFragment;
    private Fragment mArtViewExpandFragment;
    private ArtViewViewModel viewModel;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_view);

        mAuth = FirebaseAuth.getInstance();

        mArtViewFragment = new ArtViewFragment();
        mArtViewExpandFragment = new ArtViewExpandFragment();
        viewModel = new ViewModelProvider(this).get(ArtViewViewModel.class);

        replaceFragment(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment == null || fragment instanceof ArtViewExpandFragment){
            fragmentTransaction.replace(R.id.fragment_art_view_container, mArtViewFragment);
        }
        else if (fragment instanceof ArtViewFragment){
            fragmentTransaction.replace(R.id.fragment_art_view_container, mArtViewExpandFragment);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}