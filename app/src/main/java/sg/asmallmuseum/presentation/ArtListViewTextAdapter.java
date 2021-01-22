package sg.asmallmuseum.presentation;

import android.content.Context;
import android.content.Intent;
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

public class ArtListViewTextAdapter extends RecyclerView.Adapter<ArtViewHolder> {
    private final List<Artwork> mArtList;
    private RecyclerViewOnClickListener mListener;
    private final ArtworkManager manager;
    private Context context;

    public ArtListViewTextAdapter(List<Artwork> mArtwork, ArtworkManager manager){
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
        List<StorageReference> refs = manager.getArtImages(mArtList.get(position).getaType(), mArtList.get(position).getaFileLoc());
        holder.setCard(mArtList.get(position), refs.get(0));

        Artwork artwork = mArtList.get(position);
        if (mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ArtViewActivity.class);
                    intent.putExtra("DocPath", mArtList.get(position).getaID().getPath());
                    mListener.onItemClick(position, intent);
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
