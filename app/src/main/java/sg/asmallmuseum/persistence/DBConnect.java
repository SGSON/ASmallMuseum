package sg.asmallmuseum.persistence;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBConnect {
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    public DBConnect(){
        //do not insert anything..
    }

    public void connection(String path){
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference(path);
    }

    public void getDB(){

    }

    public void addUser(){

    }
}
