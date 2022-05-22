package xyz.asmallmuseum.android.presentation.ArtList;

import java.util.List;

import xyz.asmallmuseum.android.domain.Artwork;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.OnBottomReachedListener;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;

public interface ArtListViewAdapterInterface {
    void setOnClickListener(RecyclerViewOnClickListener listener);
    void setOnBottomReachedListener(OnBottomReachedListener listener);
    void updateList(List<Artwork> artworks);
    void resetList();
}
