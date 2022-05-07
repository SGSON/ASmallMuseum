package xyz.asmallmuseum.android.presentation.CustomListenerInterfaces;

import java.util.List;

import xyz.asmallmuseum.android.Domain.Artwork;

public interface ArtWorkLoadCompleteListener {
    void onArtworkLoadComplete(List<Artwork> artworks, int request_code);
}
