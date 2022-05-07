package xyz.asmallmuseum.android.presentation.ArtView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import xyz.asmallmuseum.R;

public class ArtViewRecyclerViewAdapter extends RecyclerView.Adapter<ArtViewRecyclerViewAdapter.ArtView2ViewHolder> {
    private List<String[]> mList;

    public ArtViewRecyclerViewAdapter(){
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ArtView2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ArtView2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtView2ViewHolder holder, int position) {
        holder.setDetail(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(List<String[]> newList){
        this.mList = newList;
        notifyDataSetChanged();
    }

    static class ArtView2ViewHolder extends RecyclerView.ViewHolder{
        private TextView uNick;
        private TextView uDate;
        private TextView uReview;
        private RatingBar uRating;

        public ArtView2ViewHolder(@NonNull View itemView) {
            super(itemView);
            uNick = (TextView)itemView.findViewById(R.id.review_id);
            uDate = (TextView)itemView.findViewById(R.id.review_time);
            uRating = (RatingBar)itemView.findViewById(R.id.review_rating);
            uReview = (TextView)itemView.findViewById(R.id.review_review);
        }

        public void setDetail(String[] review){
            uNick.setText(review[0]);
            uDate.setText(review[1]);
            uRating.setRating(Integer.parseInt(review[2]));
            uReview.setText(review[3]);
        }
    }
}
