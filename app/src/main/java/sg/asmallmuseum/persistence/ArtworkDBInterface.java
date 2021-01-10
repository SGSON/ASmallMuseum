package sg.asmallmuseum.persistence;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.logic.DBListener;

public interface ArtworkDBInterface {
    void setArtInfo(DocumentReference ref, String field);
    void setFileLoc(DocumentReference docRef, StorageReference storageRef);
    DocumentReference addArt(Artwork art);
    StorageReference uploadFile(String path,String id) throws FileNotFoundException;
    void setOnSuccessListener(DBListener DBListener);
    void getArtInfo(String type, String genre);
}
