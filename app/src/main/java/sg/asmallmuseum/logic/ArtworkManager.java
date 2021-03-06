package sg.asmallmuseum.logic;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.VisualArts;
import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.AppliedArts;
import sg.asmallmuseum.Domain.Others;
import sg.asmallmuseum.Domain.FineArts;
import sg.asmallmuseum.persistence.ArtworkDB;
import sg.asmallmuseum.persistence.ArtworkDBInterface;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.DBListener;
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
    public void upLoadArt(List<Uri> paths, List<String> ext, String category, String type, String title, String author, String date, String desc) {
        Artwork art = null;
        switch (type){
            case "Visual Arts":
                art = new VisualArts(category, type, title, author, date, desc);
                break;
            case "Applied Arts":
                art = new AppliedArts(category, type, title, author, date, desc);
                break;
            case "Others":
                art = new Others(category, type, title, author, date, desc);
                break;
            default:
                art = new FineArts(category, type, title, author, date, desc);
                break;
        }
        map.put("Category", category);
        map.put("Type", type);

        db.uploadArtInfo(art, paths, ext);
    }

    public void validateArt(List<Uri> paths, List<String> ext, String category, String type, String title, String author, String date, String desc) throws CustomException{
        ValidateArt.validateAll(paths, ext, category, type, title, author, date, desc);
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
    public void getArtInfoList(String category, String type, int currPost){
        if (currPost > 0){
            db.getArtInfoList(category, type, currPost);
        }
    }

    public List<StorageReference> getArtImages(String category, List<String> loc){
        return db.getArtImages(category, loc);
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

    public void getNumPost(String category, String type, int request_id){
        if (request_id == REQUEST_USER){
            db.getNumPost(category, type, REQUEST_USER);
        }
        else {
            db.getNumPost(category, type, REQUEST_UPLOAD);
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
            db.getNumPost(map.get("Category"), map.get("Type"), REQUEST_UPLOAD);
            uploadAttachedFile(paths, refs, id, art);
        }
        else{
            upListener.onUploadComplete(false);
        }
    }

    @Override
    public void onNumPostDownloadComplete(int numPost, int request_number) {
        if (request_number == REQUEST_USER){
            //numListener.onNumPostLoadComplete(numPost);

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
