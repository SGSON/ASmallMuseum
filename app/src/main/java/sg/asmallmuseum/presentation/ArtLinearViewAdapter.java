package sg.asmallmuseum.presentation;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;

public class ArtLinearViewAdapter extends RecyclerView.Adapter<ArtViewHolder> {
    private List<Artwork> mArtList;
    private RecyclerViewOnClickListener mListener;

    public ArtLinearViewAdapter(List<Artwork> mArtList){
        this.mArtList = mArtList;
    }

    @NonNull
    @Override
    public ArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_card, parent, false);
        return new ArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtViewHolder holder, int position) {
        holder.setCard(mArtList.get(position));

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

    public void setOnClickListener(RecyclerViewOnClickListener mListener){
        this.mListener = mListener;
    }
}
