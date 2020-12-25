package sg.asmallmuseum.persistence;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.User;

public class DBConnect {
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    public DBConnect(){
        //do not insert anything..
    }

    public void connection(){
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }

    public void getDB(){

    }

    public void addUser(String path, String uuID, User user){
        dbRef.child(path).child(uuID).setValue(user);
    }

    public void addArtwork(String path, String uuID, Artwork artwork){
        dbRef.child(path).child(uuID).setValue(artwork);
    }
}
