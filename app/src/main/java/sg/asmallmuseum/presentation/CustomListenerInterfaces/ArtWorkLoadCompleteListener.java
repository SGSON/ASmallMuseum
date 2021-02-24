package sg.asmallmuseum.presentation.CustomListenerInterfaces;

import java.util.List;

import sg.asmallmuseum.Domain.Artwork;

public interface ArtWorkLoadCompleteListener {
    void onArtworkLoadComplete(List<Artwork> artworks);
}
