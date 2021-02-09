package sg.asmallmuseum.presentation.ArtList;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Museum;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.persistence.SomaGallery;
import sg.asmallmuseum.presentation.ArtView.ArtViewActivity;
import sg.asmallmuseum.presentation.General.RecyclerViewOnClickListener;

public class ArtListMuseumViewAdapter extends RecyclerView.Adapter<ArtListMuseumViewAdapter.MuseumViewHolder> implements ArtListViewAdapterInterface {
    private List<Artwork> mArtList;
    private RecyclerViewOnClickListener mListener;

    public ArtListMuseumViewAdapter(List<Artwork> mArtList) {
        this.mArtList = mArtList;
    }

    @NonNull
    @Override
    public MuseumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_image_card, parent, false);
        return new MuseumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MuseumViewHolder holder, int position) {
        holder.setCard(mArtList.get(position));
        //Glide.with(holder.itemView).load(ref).into(holder.mImage);

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

    @Override
    public void setOnClickListener(RecyclerViewOnClickListener listener) {
        this.mListener = mListener;
    }

    @Override
    public void updateList(List<Artwork> artworks) {
        this.mArtList = artworks;
    }

    static class MuseumViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImage;
        private TextView mTitle;
        private TextView mAuthor;
        private TextView mDate;

        public MuseumViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.card_image_image);
            mAuthor = (TextView) itemView.findViewById(R.id.card_image_author);
            mTitle = (TextView) itemView.findViewById(R.id.card_image_title);
            mDate = (TextView) itemView.findViewById(R.id.card_image_date);
        }

        public void setCard(Artwork artwork){
            SomaGallery soma = new SomaGallery();
            try{
                soma.getArtInfo(artwork.getaAuthor(), artwork.getaTitle());
            }
            catch (IOException e){
                e.printStackTrace();
            }

//            if (artwork instanceof Museum){
//                Glide.with(itemView.getContext()).load(((Museum) artwork).getaThumbnail()).into(mImage);
//            }

            mTitle.setText(artwork.getaTitle());
            mAuthor.setText(artwork.getaAuthor());
            mDate.setText(artwork.getaDate());
        }
    }
}
