package sg.asmallmuseum.presentation;

import java.util.List;

import sg.asmallmuseum.Domain.Artwork;

public interface ManagerListener {
    void onDownloadCompleteListener(List<Artwork> artworks);
    void onUploadCompleteListener(boolean status);
}
