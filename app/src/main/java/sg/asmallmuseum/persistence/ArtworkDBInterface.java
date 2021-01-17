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
    void setOnSuccessListener(DBListener DBListener);
    void getArtInfo(String type, String genre);
    StorageReference getArtImage(String type, String loc);
    void getArtInfoById(String id);
}
