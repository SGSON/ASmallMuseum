package xyz.asmallmuseum.android.presentation.ArtList;

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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import xyz.asmallmuseum.android.Domain.Artwork;
import xyz.asmallmuseum.android.Domain.Values;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.logic.ArtworkManager;
import xyz.asmallmuseum.android.logic.CommentManager;
import xyz.asmallmuseum.android.presentation.ArtView.ArtViewActivity;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.OnBottomReachedListener;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;

public class ArtListImageViewAdapter extends RecyclerView.Adapter<ArtListImageViewAdapter.ArtListImageViewHolder> implements ArtListViewAdapterInterface{
    private List<Artwork> mArtList;
    private RecyclerViewOnClickListener mListener;
    private ArtworkManager manager;
    private OnBottomReachedListener mBottomReachedListener;
    private CommentManager commentManager;

    public ArtListImageViewAdapter(List<Artwork> mArtList, ArtworkManager manager){
        this.mArtList = mArtList;
        this.manager = manager;
    }

    @Override
    public void setOnClickListener(RecyclerViewOnClickListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void setOnBottomReachedListener(OnBottomReachedListener listener) {
        mBottomReachedListener = listener;
    }

    @NonNull
    @Override
    public ArtListImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_image_card, parent, false);
        return new ArtListImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtListImageViewHolder holder, int position) {

        List<StorageReference> ref = manager.getArtImages(mArtList.get(position).getaCategory(), mArtList.get(position).getaFileLoc());
        holder.setCard(mArtList.get(position), ref.get(0));

        if (position == mArtList.size()-1){
            mBottomReachedListener.onBottomReached();
        }

        if (mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ArtViewActivity.class);
                    intent.putExtra(Values.DOCUMENT_PATH, mArtList.get(position).getaID().getPath());
                    mListener.onItemClick(position, intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mArtList.size();
    }

    public void updateList(List<Artwork> artworks){
        int start = mArtList.size();
        this.mArtList.addAll(artworks);
        notifyItemRangeChanged(start, artworks.size());
        //notifyDataSetChanged();
    }

    @Override
    public void resetList() {
        mArtList = new ArrayList<>();
    }

    static class ArtListImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImage;
        private TextView mTitle;
        private TextView mAuthor;
        private TextView mDate;

        public ArtListImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.card_image_image);
            mAuthor = (TextView) itemView.findViewById(R.id.card_image_author);
            mTitle = (TextView) itemView.findViewById(R.id.card_image_title);
            mDate = (TextView) itemView.findViewById(R.id.card_image_date);
        }

        public void setCard(Artwork artwork, StorageReference ref){
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(itemView.getContext()).load(uri).into(mImage);
                }
            });

            mTitle.setText(artwork.getaTitle());
            mAuthor.setText(artwork.getaAuthor());
            mDate.setText(artwork.getaDate());
        }
    }
}
