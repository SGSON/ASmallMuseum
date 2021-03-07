package sg.asmallmuseum.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.List;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.persistence.FacebookUserDB;

public class FacebookSignInActivity extends AppCompatActivity implements UserInformInterface {
    private UserManager userManager;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    LoginButton facebookLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_sign_in);

        userManager = new UserManager("facebook");
        userManager.setListener(this);

        mAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();

        facebookLoginBtn = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookLoginBtn.setPermissions("email","public_profile");

        // Callback registration
        facebookLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebookLogin", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("facebookLogin", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebookLogin", "facebook:onError", error);
                // ...
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    protected void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            fUser = mAuth.getCurrentUser();
                            userManager.getUserInfo(fUser.getEmail()+"");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Facebook", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplication(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @Override
    public void userInfo(List<String> list) {

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

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("getUser",user);
        intent.putExtra("getAllUser", (Serializable) sb);
        intent.putExtra("type","facebook");
        intent.putExtra("code","1");
        startActivity(intent);
    }
}