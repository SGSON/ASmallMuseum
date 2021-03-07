package sg.asmallmuseum.presentation.CustomListenerInterfaces;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import sg.asmallmuseum.Domain.Artwork;

public interface ArtWorkDBListener {
    void onRecentFileDownloadComplete(List<String> list);
    void onFileDownloadComplete(List<Artwork> list, int request_code);
    void onFileUploadComplete(boolean complete);
    void onInfoUploadComplete(boolean complete, List<Uri> paths, List<String> refs, String id, Artwork art);
    void onNumPostDownloadComplete(int numPost, int request_id);
}
