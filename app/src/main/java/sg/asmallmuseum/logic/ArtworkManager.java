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
        upLoadArtworkInfo(path, type, genre, title, author, date, desc);
        //uploadAttachedFile(path, docRef.getId());

        //db.setFileLoc(docRef, storageRef);
    }

    //Private Methods
    private void upLoadArtworkInfo(String path, String type, String genre, String title, String author, String date, String desc){
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
        db.addArt(art, path);
    }

    private void uploadAttachedFile(String path, String id, Artwork art) {
        StorageReference storageRef = null;
        try{
            db.uploadFile(path, id, art);
            //Log.d("ASD: ", "sd"+storageRef.toString());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    //End
    /***End***/

    /***Get a image and image info from the Firestore and the storage***/
    public void getArtInfo(String type, String genre){
        db.getArtInfo(type, genre);
    }

    public StorageReference getArtImages(String type, String loc){
        return db.getArtImage(type, loc);
    }

    public void getArtInfoById(String id){
        db.getArtInfoById(id);
    }
    /***End***/

    @Override
    public void onFileDownloadCompleteListener(List<Artwork> list) {
        mListener.onDownloadCompleteListener(list);
    }

    @Override
    public void onFileUploadCompleteListener(boolean complete) {
        mListener.onUploadCompleteListener(complete);
    }

    @Override
    public void onInfoUploadCompleteListener(boolean complete, String path, String id, Artwork art) {
        if (complete){
            uploadAttachedFile(path, id, art);
        }
        else{
            mListener.onUploadCompleteListener(false);
        }
    }
}
