package sg.asmallmuseum.presentation.CustomListenerInterfaces;

import android.content.Intent;

import java.util.List;

public interface RecyclerViewOnClickListener {
    void onItemClick(int position, Intent intent);
    void onItemClick(int position, List<String> mList);
}