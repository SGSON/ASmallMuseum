package sg.asmallmuseum.persistence;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.util.List;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.logic.DBListener;

public interface ArtworkDBInterface {
    void addArt(Artwork art, List<Uri> paths, List<String> ext);
    void uploadFile(List<Uri> paths, List<String> refs, String id, Artwork art) throws FileNotFoundException;
    void setListener(DBListener DBListener);
    void getArtInfoList(String type, String genre);
    List<StorageReference> getArtImages(String type, List<String> loc);
    void getArtInfoByPath(String path, int requestCode);
    void getRecent();
    void getMultipleArtInfoByPath(List<String> paths, int requestCode);
}
