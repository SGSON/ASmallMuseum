package xyz.asmallmuseum.android.persistence;

import java.util.List;
import java.util.Map;

import xyz.asmallmuseum.android.Domain.User;
import xyz.asmallmuseum.android.logic.UserDBListener;

public interface UserDBInterface {
    void addUser(User user);
    void getUser(String email, int request_code);
    void getTempUser(String email);
    void getAllUser(List<String> list);
    void updateUser(User user);
    void deleteUser(String email);
    void setDBListener(UserDBListener userDbListener);
    void addUserPosting(String uEmail, String field, String id, Map<String, String> map);
    void getUserPosting(String uEmail, String field);
    void deletePath(String uEmail, String field, String id);
    void exists(String email, String field, String artId);
}
