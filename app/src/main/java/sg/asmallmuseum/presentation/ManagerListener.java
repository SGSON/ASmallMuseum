package sg.asmallmuseum.presentation;

import java.util.List;

import sg.asmallmuseum.Domain.Artwork;

public interface ManagerListener {
    void onLoadCompleteListener(List<Artwork> artworks);
}
