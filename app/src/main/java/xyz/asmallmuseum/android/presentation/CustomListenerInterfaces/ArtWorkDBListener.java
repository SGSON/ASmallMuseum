package xyz.asmallmuseum.android.presentation.CustomListenerInterfaces;

import android.net.Uri;

import java.util.List;

import xyz.asmallmuseum.android.domain.Artwork;

public interface ArtWorkDBListener {
    void onRecentFileDownloadComplete(List<String> list);
    void onFileDownloadComplete(List<Artwork> list, int request_code);
    void onFileUploadComplete(boolean complete);
    void onInfoUploadComplete(boolean complete, List<Uri> paths, List<String> refs, String path, Artwork art);
    void onNumPostDownloadComplete(int numPost, int request_id, String category, String type);
    void onDeleteComplete(boolean result, int result_code);
}
