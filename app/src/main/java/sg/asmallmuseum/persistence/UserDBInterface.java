package sg.asmallmuseum.persistence;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import sg.asmallmuseum.Domain.User;

public interface UserDBInterface {
    User addUser(FirebaseAuth mAuth, User user, @Nullable String password);
    User getUser(String eMail);
    void updateUser();
    void deleteUser();
    User signIn(String eMail, String password);
}
