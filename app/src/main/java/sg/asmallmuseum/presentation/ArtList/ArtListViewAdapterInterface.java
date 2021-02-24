package sg.asmallmuseum.presentation.ArtList;

import java.util.List;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;

public interface ArtListViewAdapterInterface {
    void setOnClickListener(RecyclerViewOnClickListener listener);
    void updateList(List<Artwork> artworks);
}
