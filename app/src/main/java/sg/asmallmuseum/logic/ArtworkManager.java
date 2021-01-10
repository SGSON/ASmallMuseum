package sg.asmallmuseum.logic;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.util.List;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Book;
import sg.asmallmuseum.Domain.Music;
import sg.asmallmuseum.Domain.Paint;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.persistence.ArtworkDB;
import sg.asmallmuseum.persistence.ArtworkDBInterface;
import sg.asmallmuseum.presentation.ManagerListener;

public class ArtworkManager implements DBListener {
    private final ArtworkDBInterface db;
    private ManagerListener mListener;

    public ArtworkManager() {
        this.db = new ArtworkDB();
        db.setOnSuccessListener(this);
    }

    public void setListener(ManagerListener mListener){
        this.mListener = mListener;
    }

    /***Manager to upload a image and image info to the Firestore and the storage***/
    public void upLoadArt(String path, String type, String genre, String title, String author, String date, String desc){
        DocumentReference docRef
                = upLoadArtworkInfo(type, genre, title, author, date, desc);
        StorageReference storageRef
                = uploadAttachedFile(path, docRef.getId());

        db.setArtInfo(docRef, "aID");
        db.setFileLoc(docRef, storageRef);
    }

    //Private Methods
    private DocumentReference upLoadArtworkInfo(String type, String genre, String title, String author, String date, String desc){
        Artwork art = null;
        switch (type){
            case "Books":
                art = new Book(type, genre, title, author, date, desc);
                break;
            case "Music":
                art = new Music(type, genre, title, author, date, desc);
                break;
            case "Paints":
                art = new Paint(type, genre, title, author, date, desc);
                break;
            default:
                art = new Picture(type, genre, title, author, date, desc);
                break;
        }
        return db.addArt(art);
    }

    private StorageReference uploadAttachedFile(String path, String id) {
        StorageReference storageRef = null;
        try{
            storageRef = db.uploadFile(path, id);
            Log.d("ASD: ", "sd"+storageRef.toString());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return storageRef;
    }
    //End
    /***End***/

    /***Get a image and image info from the Firestore and the storage***/
    public void getArtInfo(String type, String genre){
        db.getArtInfo(type, genre);
    }

    public void getArtImages(){

    }
    /***End***/

    @Override
    public void onFileLoadCompleteListener(List<Artwork> list) {
        mListener.onLoadCompleteListener(list);
    }
}
