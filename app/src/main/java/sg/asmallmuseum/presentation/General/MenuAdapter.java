package sg.asmallmuseum.presentation.General;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import sg.asmallmuseum.R;

public class MenuAdapter extends BaseAdapter {
    private List<String> menu_list;

    public MenuAdapter(List<String> categories) {
        categories.remove(0);
        menu_list = categories;
    }

    @Override
    public int getCount() {
        return menu_list.size();
    }

    @Override
    public Object getItem(int i) {
        return menu_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu, viewGroup, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.menu_item_image);
            TextView textView = (TextView) view.findViewById(R.id.menu_item_text);

            viewHolder.item_img = imageView;
            viewHolder.item_txt = textView;

            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)view.getTag();
        }
        String item = menu_list.get(i);
        viewHolder.item_txt.setText(item);

        switch (item){
            case "Books":
                viewHolder.item_img.setImageResource(R.drawable.image_book);
                break;
            case "Pictures":
                viewHolder.item_img.setImageResource(R.drawable.image_picture);
                break;
            case "Paints":
                viewHolder.item_img.setImageResource(R.drawable.image_paint);
                break;
            case "Music":
                viewHolder.item_img.setImageResource(R.drawable.image_music);
                break;
            case "Etc..":
                viewHolder.item_img.setImageResource(R.drawable.image_etc);
                break;
        }
        return view;
    }

    public void updateData(List<String> newData){
        newData.remove(0);
        menu_list = newData;
        notifyDataSetChanged();
    }

    public String getClickedData(int i){
        return menu_list.get(i);
    }

    static class ViewHolder{
        ImageView item_img;
        TextView item_txt;
    }
}
