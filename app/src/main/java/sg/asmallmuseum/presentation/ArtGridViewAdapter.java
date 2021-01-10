package sg.asmallmuseum.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;

public class ArtGridViewAdapter extends RecyclerView.Adapter<ArtViewHolder> {
    private List<Artwork> mArtList;
    private RecyclerViewOnClickListener mListener;
    private ArtworkManager manager;

    public ArtGridViewAdapter(List<Artwork> mArtwork, ArtworkManager manager){
        this.mArtList = mArtwork;
        this.manager = manager;
    }

    @NonNull
    @Override
    public ArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_card, parent, false);
        return new ArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtViewHolder holder, int position) {
        StorageReference ref = manager.getArtImages(mArtList.get(position).getaType(), mArtList.get(position).getaFileLoc());
        holder.setCard(mArtList.get(position), ref);

        Artwork artwork = mArtList.get(position);
        if (mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mArtList.size();
    }

    public void setOnClickListener(RecyclerViewOnClickListener listener){
        mListener = listener;
    }

}
