package sg.asmallmuseum.logic;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import sg.asmallmuseum.Domain.Artwork;

public interface DBListener {
    void onFileDownloadCompleteListener(List<Artwork> list);
    void onFileUploadCompleteListener(boolean complete);
    void onInfoUploadCompleteListener(boolean complete, List<Uri> paths, List<String> refs, String id, Artwork art);
}
