package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import sg.asmallmuseum.Domain.CustomException;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;

public class GoogleUserDBActivity extends AppCompatActivity {
    EditText nick;
    EditText lastName;
    EditText firstName;
    EditText birth;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_user_db);

        nick = findViewById(R.id.user_nickname);
        lastName = findViewById(R.id.user_lastname);
        firstName = findViewById(R.id.user_firstname);
        birth = findViewById(R.id.user_birth);
        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserManager userManager = new UserManager("google");
                String uNick = nick.getText().toString();
                String uLastName = lastName.getText().toString();
                String uFirstName = firstName.getText().toString();
                String uEmail= user.getEmail();
                String uBirth = birth.getText().toString();

                try {
                    userManager.addNewUser(uNick,uLastName,uFirstName,uEmail,uBirth);
                } catch (CustomException e) {
                    e.printStackTrace();
                }

                Intent i = getIntent();
                Intent intent = new Intent(getApplicationContext(),SignInWelcomeActivity.class);
                intent.putExtra("type","google");
                intent.putExtra("nickname",uNick);
                startActivity(intent);

            }
        });


    }
}