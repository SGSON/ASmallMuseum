package sg.asmallmuseum.logic;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import sg.asmallmuseum.Domain.Artwork;

public interface DBListener {
    void onFileDownloadCompleteListener(List<Artwork> list);
    void onFileUploadCompleteListener(boolean complete);
    void onInfoUploadCompleteListener(boolean complete, String path, String id, Artwork art);
}
