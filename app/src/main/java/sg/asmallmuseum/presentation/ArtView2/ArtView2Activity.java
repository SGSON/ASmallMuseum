package sg.asmallmuseum.presentation.ArtView2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.ArtViewMuseum.ArtViewMuseumExpandFragment;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ArtView2Activity extends AppCompatActivity {
    private Fragment mArtViewFragment;
    private Fragment mArtViewExpandFragment;
    private ArtView2ViewModel viewModel;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_view2);

        mAuth = FirebaseAuth.getInstance();


        mArtViewFragment = new ArtView2Fragment();
        mArtViewExpandFragment = new ArtViewMuseumExpandFragment();
        viewModel = new ViewModelProvider(this).get(ArtView2ViewModel.class);

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
        else if (fragment instanceof ArtView2Fragment){
            fragmentTransaction.replace(R.id.fragment_art_view_container, mArtViewExpandFragment);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}