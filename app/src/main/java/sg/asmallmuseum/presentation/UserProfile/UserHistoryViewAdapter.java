package sg.asmallmuseum.presentation.UserProfile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;

public class UserHistoryViewAdapter extends RecyclerView.Adapter<UserHistoryViewAdapter.UserHistoryViewHolder> {

    private List<Artwork> mList;

    public UserHistoryViewAdapter(){
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_text_card, parent, false);
        return new UserHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHistoryViewHolder holder, int position) {
        holder.setCard(mList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    class UserHistoryViewHolder extends RecyclerView.ViewHolder{
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

        public void setCard(Artwork artwork){

        }
    }
}
