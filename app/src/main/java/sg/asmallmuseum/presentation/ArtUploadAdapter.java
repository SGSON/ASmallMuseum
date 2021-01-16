package sg.asmallmuseum.presentation;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;

public class ArtUploadAdapter extends RecyclerView.Adapter<ArtUploadAdapter.UploadViewHolder> {
    private List<Uri> mList;
    private List<String> mNameList;

    public ArtUploadAdapter(List<Uri> mList, List<String> mNameList){
        this.mList = mList;
        this.mNameList = mNameList;
    }

    @NonNull
    @Override
    public UploadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_view, parent, false);
        return new UploadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadViewHolder holder, int position) {
        holder.setCard(mList.get(position), mNameList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(){
        notifyItemInserted(mList.size()-1);
        notifyItemRangeChanged(0, mList.size());
    }

    class UploadViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImage;
        private TextView mText;
        private Button mButton;

        public UploadViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.simple_image);
            mText = (TextView) itemView.findViewById(R.id.simple_text);
            mButton = (Button) itemView.findViewById(R.id.simple_delete);

            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.equals(mButton)){
                        removeItem(getAdapterPosition());
                    }
                }
            });
        }

        public void setCard(Uri uri, String name){
            mImage.setImageURI(uri);
            mText.setText(name);
        }

        private void removeItem(int pos){
            mNameList.remove(pos);
            mList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(0, mList.size());
        }

    }

}
