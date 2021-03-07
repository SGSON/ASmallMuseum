package sg.asmallmuseum.persistence;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.logic.DBListener;

public interface UserDBInterface {
    void addUser(User user);
    void getUser(String email);
    void getTempUser(String email);
    void getAllUser(List<String> list);
    void updateUser();
    void deleteUser(String email);
    void setDBListener(DBListener dbListener);

}
