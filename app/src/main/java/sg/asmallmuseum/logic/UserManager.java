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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.asmallmuseum.Domain.CustomException;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.persistence.FacebookUserDB;
import sg.asmallmuseum.persistence.GoogleUserDB;
import sg.asmallmuseum.persistence.EmailUserDB;
import sg.asmallmuseum.persistence.UserDBInterface;
import sg.asmallmuseum.presentation.UserInformInterface;

public class UserManager implements DBListener {
    private UserDBInterface db;
    private UserInformInterface userInformInterface;

    public void setListener(UserInformInterface userInformInterface){
        this.userInformInterface = userInformInterface;
    }

    /***Set a Db***/
    public UserManager(String path){
        switch (path){
            case "email":
                db = new EmailUserDB();
                db.setDBListener(this);
                break;
            case "google":
                db = new GoogleUserDB();
                db.setDBListener(this);
                break;
            case "facebook":
                db = new FacebookUserDB();
                db.setDBListener(this);
                break;
        }
    }

    /***this is for a email Sign-up method.***/
    public void addNewUser(String uNick, String lastname, String firstname, String email, String birth) throws CustomException {

        User user = new User(uNick, lastname, firstname, email, birth);

        db.addUser(user);

    }

    public void getTempUserInfo(String email){

       db.getTempUser(email);

    }

    public void getUserInfo(String email){

        db.getUser(email);

    }

    /*public void getAllUser(){
        db.getAllUser();

    }*/

    public void deleteUser(String email){
        db.deleteUser(email);
    }

    @Override
    public void setUserListener(List<String> list) {

        userInformInterface.userInfo(list);
    }

    @Override
    public void setAllUserListener(List<String> list) {
        String fType = list.get(0);
        String sType = list.get(1);
        String loop = list.get(2);

        if(fType.equals("email") && sType.equals("sType")){
            db = new GoogleUserDB();
            db.setDBListener(this);
            db.getAllUser(list);
        }else if(fType.equals("google") && sType.equals("sType")){
            db = new EmailUserDB();
            db.setDBListener(this);
            db.getAllUser(list);
        }else if(fType.equals("facebook") && sType.equals("sType")){
            db = new EmailUserDB();
            db.setDBListener(this);
            db.getAllUser(list);
        }

        if(fType.equals("email") && sType.equals("googleDB")){
            list.set(2,"end");
            db = new FacebookUserDB();
            db.setDBListener(this);
            db.getAllUser(list);
        }else if(fType.equals("google") && sType.equals("emailDB")){
            list.set(2,"end");
            db = new FacebookUserDB();
            db.setDBListener(this);
            db.getAllUser(list);
        }else if(fType.equals("facebook") && sType.equals("emailDB")){
            list.set(2,"end");
            db = new GoogleUserDB();
            db.setDBListener(this);
            db.getAllUser(list);
        }

        if(loop.equals("end")){
            userInformInterface.getAllUser(list);
        }

    }

    /***this method is for other sign-up methods. It does not get a password.***/
    /*public User addNewUser(FirebaseAuth mAuth, String uNick, String lastName,
                           String firstName, String eMail, @Nullable String birth) throws CustomException{
        //Create user object.
        User user = new User(uNick, lastName, firstName, eMail, birth);

        //check validation
        ValidateUser validateUser = new ValidateUser();
        validateUser.validUser(user);

        //send user to firebase db.
        //database.addUser(mAuth, user);
        return user;
    }*/






}
