package sg.asmallmuseum.presentation.ArtView;


import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;

public class ArtViewHolder extends RecyclerView.ViewHolder {
    private TextView mTitle;
    private TextView mAuthor;
    private ImageView mImage;

    public ArtViewHolder(@NonNull View itemView) {
        super(itemView);
        mImage = (ImageView) itemView.findViewById(R.id.card_image);
        mTitle = (TextView) itemView.findViewById(R.id.card_title);
        mAuthor = (TextView) itemView.findViewById(R.id.card_author);
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
    }
}
