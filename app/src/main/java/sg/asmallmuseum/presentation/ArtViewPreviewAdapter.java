package sg.asmallmuseum.presentation;

import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;

public class ArtViewPreviewAdapter extends RecyclerView.Adapter<ArtViewPreviewAdapter.PreviewHolder> {
    private List<Uri> mList;

    public ArtViewPreviewAdapter(List<Uri> mList){
        this.mList = mList;
    }

    @NonNull
    @Override
    public PreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preview_image, parent, false);
        return new PreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewHolder holder, int position) {
        holder.setImage(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class PreviewHolder extends RecyclerView.ViewHolder{
        private ImageView mImage;

        public PreviewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.preview_image);
            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public void setImage(Uri uri){
            Glide.with(itemView.getContext()).load(uri).into(mImage);
        }
    }
}
