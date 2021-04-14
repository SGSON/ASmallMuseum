package sg.asmallmuseum.persistence;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkDBListener;

public interface ArtworkDBInterface {
    void uploadArtInfo(Artwork art, List<Uri> paths, List<String> ext);
    void uploadAttachedImage(List<Uri> paths, List<String> refs, String id, Artwork art) throws FileNotFoundException;
    void setListener(ArtWorkDBListener ArtWorkDBListener);
    void getArtInfoList(String type, String genre, int currPost, String date);
    List<StorageReference> getArtImages(String type, List<String> loc);
    void getArtInfoByPath(String path, int requestCode);
    void getRecent();
    void getMultipleArtInfoByPath(List<String> paths, int requestCode);
    void getNumPost(String type, String genre, int request_id);
    void updatePostingNumber(Map<String, String> map, int numPost);
    void getArtInfoByPostNum(String category, String type, int postNum, int request_code);
    void deleteArtwork(String category, String type, String id);
    void updateLike(String category, String type, String id, int value);
    void deleteRecentPath(String id);
    void deleteImages(List<String> list);
    void uploadReport(Map<String, String> map, String id, String email);
}
