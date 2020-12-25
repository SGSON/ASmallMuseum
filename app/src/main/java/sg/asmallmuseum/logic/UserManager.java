package sg.asmallmuseum.logic;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.persistence.DBConnect;

public class UserManager {
    private DBConnect database;

    public UserManager(){
        database = new DBConnect();
        database.connection("user");
    }
    public void addNewUser(User user){
        
    }
}
