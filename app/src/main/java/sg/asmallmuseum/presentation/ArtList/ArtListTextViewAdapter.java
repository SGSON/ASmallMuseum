package sg.asmallmuseum.presentation.ArtList;

import android.content.Context;
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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.ArtView.ArtViewActivity;
import sg.asmallmuseum.presentation.ArtView2.ArtView2Activity;
import sg.asmallmuseum.presentation.General.RecyclerViewOnClickListener;

public class ArtListTextViewAdapter extends RecyclerView.Adapter<ArtListTextViewAdapter.ArtListTextViewHolder> implements ArtListViewAdapterInterface {
    private List<Artwork> mArtList;
    private RecyclerViewOnClickListener mListener;
    private final ArtworkManager manager;
    private Context context;

    public ArtListTextViewAdapter(List<Artwork> mArtwork, ArtworkManager manager){
        this.mArtList = mArtwork;
        this.manager = manager;
    }

    @NonNull
    @Override
    public ArtListTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_text_card, parent, false);
        return new ArtListTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtListTextViewHolder holder, int position) {
        List<StorageReference> refs = manager.getArtImages(mArtList.get(position).getaType(), mArtList.get(position).getaFileLoc());
        holder.setCard(mArtList.get(position), refs.get(0));

        Artwork artwork = mArtList.get(position);
        if (mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ArtView2Activity.class);
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
    public void setOnClickListener(RecyclerViewOnClickListener listener){
        mListener = listener;
    }

    @Override
    public void updateList(List<Artwork> artworks) {
        mArtList = artworks;
        notifyDataSetChanged();
    }

    static class ArtListTextViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImage;
        private TextView mTitle;
        private TextView mAuthor;
        private TextView mDate;

        public ArtListTextViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.card_text_image);
            mAuthor = (TextView) itemView.findViewById(R.id.card_text_author);
            mTitle = (TextView) itemView.findViewById(R.id.card_text_title);
            mDate = (TextView) itemView.findViewById(R.id.card_text_date);
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
