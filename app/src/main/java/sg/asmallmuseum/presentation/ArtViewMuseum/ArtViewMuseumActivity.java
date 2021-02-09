package sg.asmallmuseum.presentation.ArtViewMuseum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.General.MenuEvents;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ArtViewMuseumActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Fragment mArtView;
    private Fragment mExpandView;

    private boolean signedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_view_museum);

        mAuth = FirebaseAuth.getInstance();

        mArtView = new ArtViewMuseumFragment();
        mExpandView = new ArtViewMuseumExpandFragment();

        addFragment(mArtView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            signedIn = true;
        }
        else{
            signedIn = false;
        }
    }

    /***
     * Top-bar events
     * ***/
    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /***Move to the main activity***/
    public void onMainButtonPressed(View view) {
        makeText("Pressed Main Button");
    }

    /***Open the menu window***/
    public void onMenuButtonPressed(View view) {
        makeText("Pressed Menu Button");

        //Configure the main menu
        MenuEvents menuEvents = new MenuEvents(mAuth, this);
        menuEvents.openMenu(signedIn);
    }

    private void addFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.art_view_museum_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}