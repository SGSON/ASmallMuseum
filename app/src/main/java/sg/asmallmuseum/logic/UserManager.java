package sg.asmallmuseum.logic;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.persistence.DBConnect;

public class UserManager {
    private DBConnect database;

    public UserManager(){
        database = new DBConnect();
        database.connection();
    }
    public void addNewUser(String uID, String password, String lastname, String firstname, String email, String birth){
        String newUniqueID = createUniqueID(uID, password);
        User newUser = new User(uID, password, lastname, firstname, email, birth);
        database.addUser("Users", newUniqueID, newUser);
    }

    private String createUniqueID(String uID, String password){
        //should be encrypt ID and password
        // it would be uuID.
        return uID+password;
    }
}
