package sg.asmallmuseum.presentation.CategoryView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.General.RecyclerViewOnClickListener;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewAdapter.CategoryViewHolder> {
    private List<String> mList;
    private int mViewId;
    private RecyclerViewOnClickListener mListener;

    public CategoryViewAdapter(RecyclerViewOnClickListener mListener, int mViewId, List<String> mList){
        this.mList = mList;
        this.mViewId = mViewId;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public CategoryViewAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewAdapter.CategoryViewHolder holder, int position) {
        holder.textView.setText(mList.get(position));
        holder.imageView.setImageResource(R.drawable.button_circle);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewId == R.id.category_type_list){
                    mListener.onItemClick(position, mList);
                }
                else if (mViewId == R.id.category_genre_list){
                    mListener.onItemClick(position, new Intent());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(List<String> genre){
        mList = genre;
        notifyDataSetChanged();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;
        private View mView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            textView = (TextView) itemView.findViewById(R.id.menu_item_text);
            imageView = (ImageView) itemView.findViewById(R.id.menu_item_image);
        }
    }
}
