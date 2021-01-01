package sg.asmallmuseum.persistence;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import sg.asmallmuseum.Domain.User;

public class FacebookUserDB implements UserDBInterface {
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public FacebookUserDB(){
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
    }
    @Override
    public User addUser(FirebaseAuth mAuth, User user, @Nullable String password) {
        dbRef.child("Users").child(user.getuEmail()).setValue(user);
        return user;
    }

    @Override
    public User getUser(String eMail) {
        return null;
    }

    @Override
    public void updateUser() {

    }

    @Override
    public void deleteUser() {

    }

    @Override
    public User signIn(String eMail, String password) {
        return null;
    }
}
