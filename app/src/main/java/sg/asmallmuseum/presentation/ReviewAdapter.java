package sg.asmallmuseum.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private List<String[]> review;
    public ReviewAdapter(List<String[]> review){
        this.review = review;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        holder.setDetail(review.get(position));
    }

    @Override
    public int getItemCount() {
        return review.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder{
        private TextView uNick;
        private TextView uDate;
        private TextView uReview;
        private RatingBar uRating;

        public ReviewHolder(@NonNull View itemView) {
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
