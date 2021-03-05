package sg.asmallmuseum.presentation.General;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.RecyclerViewOnClickListener;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.MainMenuViewHolder> {

    private final int MENU_LIST = 0;
    private final int MENU_ITEM = 1;
    private List<String> mList;
    private int mType;
    private RecyclerViewOnClickListener mListener;

    public MainMenuAdapter(int type_list, Activity mActivity) {
        if (type_list == MENU_LIST){
            mList = new ArrayList<>(Arrays.asList(mActivity.getResources().getStringArray(R.array.arts_categories)));
        }
        else{
            mList = new ArrayList<>(Arrays.asList(mActivity.getResources().getStringArray(R.array.type_fine)));
        }
        mList.remove(0);
        mType = type_list;
    }

    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener mListener){
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MainMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (mType){
            case MENU_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_item, parent, false);
                break;
        }
        return new MainMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuViewHolder holder, int position) {
        switch (mType){
            case MENU_LIST:
                holder.setMenuListCard(mList.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onItemClick(position, (List<String>) null);
                    }
                });
                break;
            case MENU_ITEM:
                holder.setMenuItemCard(mList.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        mListener.onItemClick(0, null);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateList(List<String> mList){
        this.mList = mList;
        mList.remove(0);
        notifyDataSetChanged();
    }

    class MainMenuViewHolder extends RecyclerView.ViewHolder{

        private ImageView mImage;
        private TextView mText;

        public MainMenuViewHolder(@NonNull View itemView) {
            super(itemView);

            switch(mType){
                case MENU_LIST:
                    mImage = (ImageView) itemView.findViewById(R.id.menu_item_image);
                    mText = (TextView) itemView.findViewById(R.id.menu_item_text);
                    break;
                default:
                    mImage = (ImageView) itemView.findViewById(R.id.item_menu_item_image);
                    mImage.setImageResource(R.drawable.item_pointer);
                    mText = (TextView) itemView.findViewById(R.id.item_menu_item_text);
                    break;
            }
        }

        public void setMenuListCard(String name){
            mText.setText(name);
            switch (name){
                case "Fine Arts":
                    mImage.setImageResource(R.drawable.image_paint);
                    break;
                case "Visual Arts":
                    mImage.setImageResource(R.drawable.image_picture);
                    break;
                case "Applied Arts":
                    mImage.setImageResource(R.drawable.image_applied);
                    break;
                case "Others":
                    mImage.setImageResource(R.drawable.image_etc);
                    break;
                case "Museums":
                    mImage.setImageResource(R.drawable.image_museum);
                    break;
            }
        }
        public void setMenuItemCard(String name){
            mText.setText(name);
        }
    }
}
