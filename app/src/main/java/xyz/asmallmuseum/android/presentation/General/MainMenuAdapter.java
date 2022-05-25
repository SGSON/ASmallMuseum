package xyz.asmallmuseum.android.presentation.General;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import xyz.asmallmuseum.android.domain.Values;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.MainMenuOnClickListener;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.MainMenuViewHolder> {

    private final int MENU_LIST = 0;
    private final int MENU_ITEM = 1;

    private List<String> mList;
    private int mType;
    private MainMenuOnClickListener mListener;
    private List<MainMenuViewHolder> mViewHolderList;
    private Activity mActivity;

    public MainMenuAdapter(int type_list, Activity mActivity) {
        if (type_list == MENU_LIST){
            mList = new ArrayList<>(Arrays.asList(mActivity.getResources().getStringArray(R.array.arts_categories)));
        }
        else{
            mList = new ArrayList<>(Arrays.asList(mActivity.getResources().getStringArray(R.array.type_fine)));
        }
        mList.remove(0);
        mType = type_list;
        mViewHolderList = new ArrayList<>();
        this.mActivity = mActivity;
    }

    public void setRecyclerViewOnClickListener(int request_code, MainMenuOnClickListener mListener){
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
        MainMenuViewHolder viewHolder = new MainMenuViewHolder(view);
        mViewHolderList.add(viewHolder);
        if (mViewHolderList.size() == 1 && mType == MENU_LIST){
            setActiveColor(0);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuViewHolder holder, int position) {
        switch (mType){
            case MENU_LIST:
                holder.setMenuListCard(mList.get(position));
                break;
            case MENU_ITEM:
                holder.setMenuItemCard(mList.get(position));
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mType == MENU_LIST){
                    setInactiveColor();
                    setActiveColor(position);
                }
                mListener.onItemClick(mList.get(position), mType);
            }
        });
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

    private void setInactiveColor(){
        for (int i = 0 ; i < mViewHolderList.size() ; i++){
            mViewHolderList.get(i).mImage.setBackgroundColor(mActivity.getResources().getColor(R.color.white, mActivity.getTheme()));
            mViewHolderList.get(i).itemView.setBackgroundColor(mActivity.getResources().getColor(R.color.white, mActivity.getTheme()));
        }
    }

    private void setActiveColor(int position){
        mViewHolderList.get(position).itemView.setBackgroundColor(mActivity.getResources().getColor(R.color.boarder, mActivity.getTheme()));
        mViewHolderList.get(position).mImage.setBackgroundColor(mActivity.getResources().getColor(R.color.boarder, mActivity.getTheme()));
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
                case Values.ART_FINE:
                    mImage.setImageResource(R.drawable.image_paint);
                    break;
                case Values.ART_VISUAL:
                    mImage.setImageResource(R.drawable.image_picture);
                    break;
                case Values.ART_APPLIED:
                    mImage.setImageResource(R.drawable.image_applied);
                    break;
                case Values.ART_OTHERS:
                    mImage.setImageResource(R.drawable.image_etc);
                    break;
                case Values.ART_MUSEUM:
                    mImage.setImageResource(R.drawable.image_museum);
                    break;
            }
        }
        public void setMenuItemCard(String name){
            mText.setText(name);
        }
    }
}
