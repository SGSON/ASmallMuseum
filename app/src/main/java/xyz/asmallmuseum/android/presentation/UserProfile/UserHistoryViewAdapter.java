package xyz.asmallmuseum.android.presentation.UserProfile;

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
import xyz.asmallmuseum.android.presentation.ArtView.ArtViewActivity;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;

public class UserHistoryViewAdapter extends RecyclerView.Adapter<UserHistoryViewAdapter.UserHistoryViewHolder> {

    private List<Artwork> mList;
    private ArtworkManager artworkManager;
    private RecyclerViewOnClickListener mListener;

    public UserHistoryViewAdapter(ArtworkManager manager){
        mList = new ArrayList<>();
        artworkManager = manager;
    }

    public void setOnItemClickListener(RecyclerViewOnClickListener mListener){
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public UserHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_text_card, parent, false);
        return new UserHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHistoryViewHolder holder, int position) {
        List<StorageReference> ref = artworkManager.getArtImages(mList.get(position).getaCategory(), mList.get(position).getaFileLoc());
        holder.setCard(mList.get(position), ref.get(0));

        //holder.setCard(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ArtViewActivity.class);
                intent.putExtra(Values.DOCUMENT_PATH, mList.get(position).getaID().getPath());
                mListener.onItemClick(position, intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(List<Artwork> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    static class UserHistoryViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImage;
        private TextView mTitle;
        private TextView mAuthor;
        private TextView mDate;

        public UserHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.card_text_image);
            mTitle = (TextView) itemView.findViewById(R.id.card_text_title);
            mAuthor = (TextView) itemView.findViewById(R.id.card_text_author);
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
