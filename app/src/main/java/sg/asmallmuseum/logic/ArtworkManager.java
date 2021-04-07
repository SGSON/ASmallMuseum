package sg.asmallmuseum.logic;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.RequestCode;
import sg.asmallmuseum.Domain.Values;
import sg.asmallmuseum.Domain.VisualArts;
import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.AppliedArts;
import sg.asmallmuseum.Domain.Others;
import sg.asmallmuseum.Domain.FineArts;
import sg.asmallmuseum.persistence.ArtworkDB;
import sg.asmallmuseum.persistence.ArtworkDBInterface;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkDBListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtworkDeleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.NumPostLoadCompleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UploadCompleteListener;

import static sg.asmallmuseum.Domain.RequestCode.RESULT_UPLOAD_FAIL;
import static sg.asmallmuseum.Domain.RequestCode.RESULT_UPLOAD_INFO_OK;

public class ArtworkManager implements ArtWorkDBListener {
    private final ArtworkDBInterface db;

    private UploadCompleteListener upListener;
    private ArtWorkLoadCompleteListener downListener;
    private NumPostLoadCompleteListener numListener;
    private ArtworkDeleteListener deleteListener;

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

    public void setDeleteArtworkListener(ArtworkDeleteListener mListener){
        this.deleteListener = mListener;
    }

    /***Manager to upload a image and image info to the Firestore and the storage***/
    public void upLoadArt(List<Uri> paths, List<String> ext, String category, String type, String title, String author, String date, String time, String desc, String email) {
        Artwork art = null;
        switch (type){
            case Values.ART_VISUAL:
                art = new VisualArts(category, type, title, author, date, time, desc, email);
                break;
            case Values.ART_APPLIED:
                art = new AppliedArts(category, type, title, author, date, time, desc, email);
                break;
            case Values.ART_OTHERS:
                art = new Others(category, type, title, author, date, time, desc, email);
                break;
            default:
                art = new FineArts(category, type, title, author, date, time, desc, email);
                break;
        }
        map.put(Values.ART_CATEGORY, category);
        map.put(Values.ART_TYPE, type);

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
        db.getArtInfoByPath(id, RequestCode.REQUEST_SINGLE);
    }

    public void getMultipleArtInfoByPath(List<String> paths){
        db.getMultipleArtInfoByPath(paths, RequestCode.REQUEST_MULTIPLE);
    }

    public void getRecent(){
        db.getRecent();
    }

    public void getNumPost(String category, String type, int request_id){
        db.getNumPost(category, type, request_id);
    }

    public void getArtInfoByNumPost(String category, String type, int numPost, int request_code){
        db.getArtInfoByPostNum(category, type, numPost, request_code);
    }
    /***End***/

    /***Delete Artwork***/
    public void deleteArtwork(Artwork artwork){
        db.deleteArtwork(artwork.getaCategory(), artwork.getaType(), artwork.getaID().getId());
    }

    public void deleteArtworkRecentPath(Artwork artwork){
        db.deleteRecentPath(artwork.getaID().getId());
    }
    //End

    /***Update artwork information***/
    public void updateArtwork(Artwork artwork, String field, int value){
        db.updateLike(artwork.getaCategory(), artwork.getaType(), artwork.getaID().getId(), value);
    }

    /***SORTING and Other Utils***/
    public void sortByDate(List<Artwork> list){
        list.sort(new Comparator<Artwork>() {
            @Override
            public int compare(Artwork artwork, Artwork t1) {
                if (artwork.getaTime() != null && t1.getaTime() != null){
                    return -(artwork.getaTime()).compareTo(t1.getaTime());
                }
                else {
                    return -(artwork.getaDate()).compareTo(t1.getaDate());
                }
            }
        });
    }

    public void sortByPostNum(List<Artwork> list){
        list.sort(new Comparator<Artwork>() {
            @Override
            public int compare(Artwork artwork, Artwork t1) {
                return -(artwork.getaPostNum() - t1.getaPostNum());
            }
        });
    }

    private void removeInvalidArt(List<Artwork> list){
        if(list.contains(null)){
            int size = list.size();
            for (int i = 0 ; i < size ; i++){
                if (list.get(i) == null){
                    list.remove(i);
                    size--;
                    i--;
                }
            }
        }
    }

    /***Complete Listeners from the ArtworkDB***/
    @Override
    public void onFileDownloadComplete(List<Artwork> list, int request_code) {
        removeInvalidArt(list);
        downListener.onArtworkLoadComplete(list, request_code);
    }

    @Override
    public void onFileUploadComplete(boolean complete) {
        upListener.onUploadComplete(complete, null, RequestCode.RESULT_UPLOAD_OK);
    }

    @Override
    public void onInfoUploadComplete(boolean complete, List<Uri> paths, List<String> refs, String path, Artwork art) {
        if (complete){
            String[] pathList = path.split("/");
            map.put(Values.ART_ID, pathList[pathList.length-1]);
            db.getNumPost(map.get(Values.ART_CATEGORY), map.get(Values.ART_TYPE), RequestCode.REQUEST_UPLOAD);
            upListener.onUploadComplete(false, path, RESULT_UPLOAD_INFO_OK);
            uploadAttachedFile(paths, refs, path, art);
        }
        else{
            upListener.onUploadComplete(false, null, RESULT_UPLOAD_FAIL);
        }
    }

    @Override
    public void onNumPostDownloadComplete(int numPost, int request_number, String category, String type) {
        if (request_number == RequestCode.REQUEST_USER || request_number == RequestCode.REQUEST_RANDOM){
            //numListener.onNumPostLoadComplete(numPost);
            if (numListener != null){
                numListener.onNumPostLoadComplete(numPost, request_number, category, type);
            }
        }
        else if (request_number == RequestCode.REQUEST_UPLOAD){
            db.updatePostingNumber(map, numPost);
        }
    }

    @Override
    public void onDeleteComplete(boolean result, int result_code) {
        deleteListener.onArtworkDeleteComplete(result, result_code);
    }

    @Override
    public void onRecentFileDownloadComplete(List<String> list) {
        getMultipleArtInfoByPath(list);
    }
}
