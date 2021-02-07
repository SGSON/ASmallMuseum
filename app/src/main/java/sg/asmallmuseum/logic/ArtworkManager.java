package sg.asmallmuseum.logic;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.util.List;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Book;
import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.Music;
import sg.asmallmuseum.Domain.Paint;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.persistence.ArtworkDB;
import sg.asmallmuseum.persistence.ArtworkDBInterface;
import sg.asmallmuseum.presentation.General.ManagerListener;

public class ArtworkManager implements DBListener {
    private final ArtworkDBInterface db;
    private ManagerListener mListener;
    private final int REQUEST_SINGLE = 2;
    private final int REQUEST_MULTIPLE = 8;

    public ArtworkManager() {
        this.db = new ArtworkDB();
        db.setListener(this);
    }

    public void setListener(ManagerListener mListener){
        this.mListener = mListener;
    }

    /***Manager to upload a image and image info to the Firestore and the storage***/
    public void upLoadArt(List<Uri> paths, List<String> ext, String type, String genre, String title, String author, String date, String desc) {
        upLoadArtworkInfo(paths, ext, type, genre, title, author, date, desc);
    }

    public void validateArt(List<Uri> paths, List<String> ext, String type, String genre, String title, String author, String date, String desc) throws CustomException{
        ValidateArt.validateAll(paths, ext, type, genre, title, author, date, desc);
    }

    //Private Methods
    private void upLoadArtworkInfo(List<Uri> paths, List<String> ext, String type, String genre, String title, String author, String date, String desc) {
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
        db.addArt(art, paths, ext);
    }

    private void uploadAttachedFile(List<Uri> paths, List<String> refs, String id, Artwork art) {
        StorageReference storageRef = null;
        try{
            db.uploadFile(paths, refs, id, art);
            //Log.d("ASD: ", "sd"+storageRef.toString());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    //End
    /***End***/

    /***Get a image and image info from the Firestore and the storage***/
    public void getArtInfoList(String type, String genre){
        db.getArtInfoList(type, genre);
    }

    public List<StorageReference> getArtImages(String type, List<String> loc){
        return db.getArtImages(type, loc);
    }

    public void getSingleArtInfoByPath(String id){
        db.getArtInfoByPath(id, REQUEST_SINGLE);
    }

    public void getMultipleArtInfoByPath(List<String> paths){
        db.getMultipleArtInfoByPath(paths, REQUEST_MULTIPLE);
    }

    public void getRecent(){
        db.getRecent();
    }
    /***End***/

    @Override
    public void onFileDownloadCompleteListener(List<Artwork> list, int request_code) {
        if (request_code == REQUEST_MULTIPLE){
            mListener.onDownloadCompleteListener(list);
        }
        else{
            mListener.onDownloadCompleteListener(list);
        }

    }

    @Override
    public void onFileUploadCompleteListener(boolean complete) {
        mListener.onUploadCompleteListener(complete);
    }

    @Override
    public void onInfoUploadCompleteListener(boolean complete, List<Uri> paths, List<String> ext, String id, Artwork art) {
        if (complete){
            uploadAttachedFile(paths, ext, id, art);
        }
        else{
            mListener.onUploadCompleteListener(false);
        }
    }

    @Override
    public void onRecentFileDownloadCompleteListener(List<String> list) {
        getMultipleArtInfoByPath(list);
    }
}
