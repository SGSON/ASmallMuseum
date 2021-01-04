package sg.asmallmuseum.logic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sg.asmallmuseum.Domain.CustomException;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.persistence.FacebookUserDB;
import sg.asmallmuseum.persistence.GoogleUserDB;
import sg.asmallmuseum.persistence.EmailUserDB;
import sg.asmallmuseum.persistence.UserDBInterface;

public class UserManager {
    private UserDBInterface db;

    /***Set a Db***/
    public UserManager(String path){
        switch (path){
            case "eMail":
                db = new EmailUserDB();
                break;
            case "Google":
                db = new GoogleUserDB();
                break;
            case "Facebook":
                db = new FacebookUserDB();
                break;
        }
    }

    /***this is for a email Sign-up method.***/
    public User addNewUser(FirebaseAuth mAuth, String uNick, String password, String lastname,
                                  String firstname, String eMail, String birth) throws CustomException {

        User user = new User(uNick, lastname, firstname, eMail, birth);

        //check validate form.
        ValidateUser validateUser = new ValidateUser();
        validateUser.validUser(user);
        validateUser.validPassword(password);

        //if it is validate, send to DBConnection to update user.
        //((DBConnect)database).insertToFirebase(mAuth,eMail,password);
        user = db.addUser(mAuth, user, password);

        return user;
    }

    /***this method is for other sign-up methods. It does not get a password.***/
    public User addNewUser(FirebaseAuth mAuth, String uNick, String lastName,
                           String firstName, String eMail, @Nullable String birth) throws CustomException{
        //Create user object.
        User user = new User(uNick, lastName, firstName, eMail, birth);

        //check validation
        ValidateUser validateUser = new ValidateUser();
        validateUser.validUser(user);

        //send user to firebase db.
        //database.addUser(mAuth, user);
        return user;
    }

    public User getUser(FirebaseAuth mAuth, String email, String password){
        return db.getUser(email);
    }

    public User signIn(FirebaseAuth mAuth, String eMail, String password){
        return db.signIn(eMail, password);
    }

    /***Google Sign-Up methods***/
    /*
    * signUPWithGoogle: get a google sign-up page
    * firebaseSignWithGoogle: Authentication with Google*/
    public Intent signUPWithGoogle(Context context, FirebaseAuth mAuth){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSignInClient mClient = GoogleSignIn.getClient(context, gso);
        Intent intent = mClient.getSignInIntent();
        return intent;
    }

    private void firebaseSignInWithGoogle(String idToken, FirebaseAuth mAuth, Context context){
        AuthCredential mAuthCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(mAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("LOGIN: ","LOGIN SUCCESS!");
                }
                else {
                    Log.w("LOGIN: ", "FAIL TO LOGIN");
                }
            }
        });
    }
    /***End***/
}
