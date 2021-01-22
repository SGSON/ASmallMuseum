package sg.asmallmuseum.logic;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import sg.asmallmuseum.Domain.Artwork;

public interface DBListener {
    void onRecentFileDownloadCompleteListener(List<String> list);
    void onFileDownloadCompleteListener(List<Artwork> list, int request_code);
    void onFileUploadCompleteListener(boolean complete);
    void onInfoUploadCompleteListener(boolean complete, List<Uri> paths, List<String> refs, String id, Artwork art);
}
