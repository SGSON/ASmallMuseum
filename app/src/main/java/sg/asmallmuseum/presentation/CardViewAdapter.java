package sg.asmallmuseum.presentation;

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

public class CardViewAdapter extends RecyclerView.Adapter {
    private List<Artwork> mArtList;

    public CardViewAdapter(List<Artwork> mArtList){
        this.mArtList = mArtList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).setCard(mArtList.get(position));
    }

    @Override
    public int getItemCount() {
        return mArtList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView title;
        private TextView author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_image);
            title = itemView.findViewById(R.id.card_title);
            author = itemView.findViewById(R.id.card_author);
        }

        public void setCard(Artwork artwork){
            title.setText(artwork.getaDesc());
            author.setText(artwork.getaAuthor());
        }
    }
}
