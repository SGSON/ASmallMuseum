package sg.asmallmuseum.persistence;

import java.util.List;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.logic.UserDBListener;

public interface UserDBInterface {
    void addUser(User user);
    void getUser(String email, int request_code);
    void getTempUser(String email);
    void getAllUser(List<String> list);
    void updateUser(User user);
    void deleteUser(String email);
    void setDBListener(UserDBListener userDbListener);

}
