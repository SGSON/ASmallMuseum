package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.presentation.SignUp.SignInWelcomeActivity;

/**
 * never used
 * */

public class FacebookUserDBActivity extends AppCompatActivity{
    private FirebaseUser user;
    private UserManager userManager;
    EditText nick;
    EditText birth;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_user_db);

        nick = findViewById(R.id.user_nickname);
        birth = findViewById(R.id.user_birth);
        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                userManager = new UserManager("facebook");

                String uNick = nick.getText().toString();
                String uLastName = Profile.getCurrentProfile().getLastName();
                String uFirstName = Profile.getCurrentProfile().getFirstName();
                String uEmail= user.getEmail()+"";
                String uBirth = birth.getText().toString();
                Toast.makeText(getApplicationContext(),uEmail+"",Toast.LENGTH_SHORT).show();

                try {
                    userManager.addNewUser(uNick, uLastName, uFirstName, uEmail, uBirth);
                } catch (CustomException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), SignInWelcomeActivity.class);
                intent.putExtra("type","facebook");
                intent.putExtra("nickname",uNick);
                startActivity(intent);



            }
        });


    }




}