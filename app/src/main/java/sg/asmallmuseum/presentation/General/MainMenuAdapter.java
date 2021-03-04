package sg.asmallmuseum.presentation.General;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.R;

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.MainMenuViewHolder> {

    private final int MENU_LIST = 0;
    private final int MENU_ITEM = 1;
    private List<String> mList;
    private int mType;

    public MainMenuAdapter(int type_list) {
        mList = new ArrayList<>();
        mType = type_list;
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
                holder.setMenuListCard();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
            case MENU_ITEM:
                holder.setMenuItemCard();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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
                    mText = (TextView) itemView.findViewById(R.id.item_menu_item_text);
                    break;
            }
        }

        public void setMenuListCard(){

        }
        public void setMenuItemCard(){

        }
    }
}
