package xyz.asmallmuseum.android.presentation.CustomListenerInterfaces;

import java.util.List;

import xyz.asmallmuseum.android.domain.Artwork;

public interface ArtWorkLoadCompleteListener {
    void onArtworkLoadComplete(List<Artwork> artworks, int request_code);
}
