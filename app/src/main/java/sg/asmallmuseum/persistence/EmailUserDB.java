package sg.asmallmuseum.persistence;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sg.asmallmuseum.Domain.User;

public class EmailUserDB implements UserDBInterface {

    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    public EmailUserDB(){
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
    }

    @Override
    public User addUser(FirebaseAuth mAuth, User user, @Nullable String password) {
        insertToDB(mAuth, user.getuEmail(), password);
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

    private void insertToDB(FirebaseAuth mAuth, String eMail, String password) {

        mAuth.createUserWithEmailAndPassword(eMail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if (task.isSuccessful()){
                    Log.d("EmailPAssword", "LoginSuccess");
                    FirebaseUser user = mAuth.getCurrentUser();

                }
                else{
                    Log.w("EmailPassword", "Fail to log-in");
                }
            }
        });
    }


}
