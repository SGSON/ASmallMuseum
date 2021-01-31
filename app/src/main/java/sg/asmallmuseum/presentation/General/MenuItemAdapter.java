package sg.asmallmuseum.presentation.General;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.asmallmuseum.R;

public class MenuItemAdapter extends BaseAdapter {
    private List<String> mList;

    public MenuItemAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MenuAdapter.ViewHolder viewHolder;
        if (view == null){
            viewHolder = new MenuAdapter.ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu_item, viewGroup, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.item_menu_item_image);
            TextView textView = (TextView) view.findViewById(R.id.item_menu_item_text);

            viewHolder.item_img = imageView;
            viewHolder.item_txt = textView;

            view.setTag(viewHolder);
        }
        else{
            viewHolder = (MenuAdapter.ViewHolder)view.getTag();
        }
        String item = mList.get(i);
        viewHolder.item_txt.setText(item);
        viewHolder.item_img.setImageResource(R.drawable.item_pointer);


        return view;
    }

    public void updateData(List<String> newData){
        newData.remove(0);
        mList = newData;
        notifyDataSetChanged();
    }

    public String getClickedData(int i){
        return mList.get(i);
    }

    static class MenuItemViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
