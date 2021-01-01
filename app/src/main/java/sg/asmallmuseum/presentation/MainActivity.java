package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;
import sg.asmallmuseum.Domain.CustomException;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.logic.MenuAction;
import sg.asmallmuseum.logic.UserManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserManager userManager = new UserManager("eMail");
        ArtworkManager artworkManager = new ArtworkManager();
        mAuth = FirebaseAuth.getInstance();

        Button button = (Button) findViewById(R.id.textas);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    userManager.addNewUser(mAuth, "asdasd", "ASDasd123!@#", "asd", "asd", "asd@asd.com", "123");
                } catch (CustomException e) {
                    e.printStackTrace();
                }
                //artworkManager.addArtwork("Pictures", "asdqweqes", "qwe", "1-1--", "Wow", "WOWOWO");
                makeText("Success!!");
            }
        });
    }
    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void onMainButtonPressed(View view) {
        Toast.makeText(this, "Pressed Main Button", Toast.LENGTH_LONG).show();
    }

    public void onMenuButtonPressed(View view) {
        Toast.makeText(this, "Pressed Menu Button", Toast.LENGTH_LONG).show();
        //showPopup(view);
        MenuAction menuAction = new MenuAction();
        menuAction.openMenu(this);
    }

    public void onBackButtonPressed(View view) {
        Toast.makeText(this, "Pressed Back Button", Toast.LENGTH_LONG).show();
    }

}