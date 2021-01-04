package sg.asmallmuseum.logic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.persistence.DBConnect;
import sg.asmallmuseum.presentation.MainActivity;
import sg.asmallmuseum.presentation.SignUp;

public class UserManager{
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
