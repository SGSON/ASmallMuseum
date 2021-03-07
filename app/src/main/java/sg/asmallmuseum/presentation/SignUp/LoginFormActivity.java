package sg.asmallmuseum.presentation.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.List;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.presentation.UserInformInterface;

public class LoginFormActivity extends AppCompatActivity implements UserInformInterface {
    private UserManager eUserManager;
    private UserManager fUserManager;
    private UserManager gUserManager;
    private FirebaseUser user;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;

    Button emailLoginBtn;
    Button facebookLoginBtn;
    Button googleLoginBtn;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        mAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();

        eUserManager = new UserManager("email");
        eUserManager.setListener(this);

        fUserManager = new UserManager("facebook");
        fUserManager.setListener(this);

        gUserManager = new UserManager("google");
        gUserManager.setListener(this);

        emailLoginBtn = (Button)findViewById(R.id.email_login_button);
        facebookLoginBtn = (Button)findViewById(R.id.facebook_login_button);
        googleLoginBtn = (Button)findViewById(R.id.google_login_button);
        signUpBtn = (Button)findViewById(R.id.signUp);

        emailLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        facebookLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FacebookSignInActivity.class);
                startActivity(intent);
            }
        });

        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GoogleSignInActivity.class);
                startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eUserManager.getUserInfo("nullvgvgv");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void userInfo(List<String> users) {

    }

    @Override
    public void getAllUser(List<String> list) {
        StringBuilder sb = new StringBuilder();
        User user = new User();

        if(!list.get(3).equals("null")){
            user.setuEmail(list.get(3));
            user.setuNick(list.get(4));
            user.setuLastName(list.get(5));
            user.setuFirstName(list.get(6));
            user.setuBirth(list.get(7));
        }else if(list.get(3).equals("null")){
            ;
        }
        list.set(3,"");

        for(String s : list){
            sb.append(s+"/");
        }

        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        intent.putExtra("getAllUser", (Serializable) sb);
        intent.putExtra("type","email");
        startActivity(intent);
    }

}