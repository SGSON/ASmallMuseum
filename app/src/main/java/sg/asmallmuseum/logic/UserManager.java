package sg.asmallmuseum.logic;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.User;
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


}
