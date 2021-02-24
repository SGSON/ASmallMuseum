package sg.asmallmuseum.logic;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Book;
import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.Music;
import sg.asmallmuseum.Domain.Paint;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.persistence.ArtworkDB;
import sg.asmallmuseum.persistence.ArtworkDBInterface;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.NumPostLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UploadCompleteListener;

public class ArtworkManager implements DBListener {
    private final ArtworkDBInterface db;

    private UploadCompleteListener upListener;
    private ArtWorkLoadCompleteListener downListener;
    private NumPostLoadCompleteListener numListener;

    private final int REQUEST_SINGLE = 2;
    private final int REQUEST_MULTIPLE = 8;

    private final int REQUEST_USER = 2010;
    private final int REQUEST_UPLOAD = 2011;

    private final Map<String, String> map;

    public ArtworkManager() {
        this.db = new ArtworkDB();
        db.setListener(this);
        map = new HashMap<>();
    }

    public void setUpLoadCompleteListener(UploadCompleteListener mListener){
        this.upListener = mListener;
    }

    public void setArtworkLoadCompleteListener(ArtWorkLoadCompleteListener mListener){
        this.downListener = mListener;
    }

    public void setNumPostLoadCompleteListener(NumPostLoadCompleteListener mListener){
        this.numListener = mListener;
    }

    /***Manager to upload a image and image info to the Firestore and the storage***/
    public void upLoadArt(List<Uri> paths, List<String> ext, String type, String genre, String title, String author, String date, String desc) {
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
        map.put("type", type);
        map.put("genre", genre);

        db.uploadArtInfo(art, paths, ext);
    }

    public void validateArt(List<Uri> paths, List<String> ext, String type, String genre, String title, String author, String date, String desc) throws CustomException{
        ValidateArt.validateAll(paths, ext, type, genre, title, author, date, desc);
    }

    //Private Methods
    private void uploadAttachedFile(List<Uri> paths, List<String> refs, String id, Artwork art) {
        StorageReference storageRef = null;
        try{
            db.uploadAttachedImage(paths, refs, id, art);
            //Log.d("ASD: ", "sd"+storageRef.toString());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    //End
    /***End***/

    /***Get a image and image info from the Firestore and the storage***/
    public void getArtInfoList(String type, String genre, int currPost){
        db.getArtInfoList(type, genre, currPost);
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

    public void getNumPost(String type, String genre, int request_id){
        if (request_id == REQUEST_USER){
            db.getNumPost(type, genre, REQUEST_USER);
        }
        else {
            db.getNumPost(type, genre, REQUEST_UPLOAD);
        }

    }
    /***End***/

    @Override
    public void onFileDownloadComplete(List<Artwork> list, int request_code) {
            list.sort(new Comparator<Artwork>() {
                @Override
                public int compare(Artwork artwork, Artwork t1) {
                    if (artwork.getaPostNum() >= t1.getaPostNum()){
                        return -1;
                    }
                    return 1;
                }
            });
            downListener.onArtworkLoadComplete(list);
    }

    @Override
    public void onFileUploadComplete(boolean complete) {
        upListener.onUploadComplete(complete);
    }

    @Override
    public void onInfoUploadComplete(boolean complete, List<Uri> paths, List<String> refs, String id, Artwork art) {
        if (complete){
            map.put("id", id);
            db.getNumPost(map.get("type"), map.get("genre"), REQUEST_UPLOAD);
            uploadAttachedFile(paths, refs, id, art);
        }
        else{
            upListener.onUploadComplete(false);
        }
    }

    @Override
    public void onNumPostDownloadComplete(int numPost, int request_number) {
        if (request_number == REQUEST_USER){
            numListener.onNumPostLoadComplete(numPost);

            if (numListener != null){
                numListener.onNumPostLoadComplete(numPost);
            }
        }
        else if (request_number == REQUEST_UPLOAD){
            db.updatePostingNumber(map, numPost);
        }
    }

    @Override
    public void onRecentFileDownloadComplete(List<String> list) {
        getMultipleArtInfoByPath(list);
    }
}
