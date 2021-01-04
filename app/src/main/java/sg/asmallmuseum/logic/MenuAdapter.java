package sg.asmallmuseum.logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.asmallmuseum.R;

public class MenuAdapter extends BaseAdapter {
    List<String> menu_list;
    String temp[];
    public MenuAdapter() {
        String temp[] = {"Profile","Pictures","Music","Paints","Books","Upload","Support"};
        menu_list = Arrays.asList(temp);
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item, viewGroup, false);
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
        viewHolder.item_img.setImageResource(R.drawable.arrow_back);

        return view;
    }

    static class ViewHolder{
        ImageView item_img;
        TextView item_txt;
    }
}
