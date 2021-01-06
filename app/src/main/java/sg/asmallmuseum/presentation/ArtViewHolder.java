package sg.asmallmuseum.presentation;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;

public class ArtViewHolder extends RecyclerView.ViewHolder {
    private TextView mTitle;
    private TextView mAuthor;

    public ArtViewHolder(@NonNull View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.card_title);
        mAuthor = (TextView) itemView.findViewById(R.id.card_author);
    }

    public void setCard(Artwork artwork){
        mTitle.setText(artwork.getaID());
        mAuthor.setText(artwork.getaAuthor());
    }
}
