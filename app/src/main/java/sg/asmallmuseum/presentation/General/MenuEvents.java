package sg.asmallmuseum.presentation.General;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.ArtList.ArtListActivity;
import sg.asmallmuseum.presentation.LoginActivity;
import sg.asmallmuseum.presentation.SignUpActivity;

public class MenuEvents implements View.OnClickListener {
    private PopupWindow popupWindow;
    private final FirebaseAuth mAuth;
    private final Activity mActivity;
    private Map<String, String> map;

    public MenuEvents(FirebaseAuth mAuth, Activity act){
        this.mAuth = mAuth;
        this.mActivity = act;
        map = new HashMap<>();
    }

    public void openMenu(boolean signedIn){
        LayoutInflater layoutInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menu = layoutInflater.inflate(R.layout.layout_main_menu, null);

        popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(menu, Gravity.RIGHT,0,0);

        menu.findViewById(R.id.menu_close).setOnClickListener(this);

        setMenuForm(menu, signedIn);
        changeBackGroundColor(mActivity, popupWindow);
        setMenuAdapter(mActivity, menu);
    }

    @Override
    public void onClick(View view) {
        int btn_id = view.getId();

        if (btn_id == R.id.log_in_btn){
            Intent intent = new Intent(mActivity, LoginActivity.class);
            mActivity.startActivity(intent);
        }
        else if (btn_id == R.id.sign_up_btn){
            Intent intent = new Intent(mActivity, SignUpActivity.class);
            mActivity.startActivity(intent);
        }
        else if (btn_id == R.id.menu_close){
            popupWindow.dismiss();
        }
        else if (btn_id == R.id.sign_out_btn){
            mAuth.signOut();
            mActivity.finish();
            mActivity.startActivity(mActivity.getIntent());
        }
    }

    private void setMenuForm(View view, boolean signedIn){
        ConstraintLayout userInfoForm = (ConstraintLayout)view.findViewById(R.id.user_info);
        ConstraintLayout signInForm = (ConstraintLayout)view.findViewById(R.id.sign_in);
        ImageButton sign_out_btn = (ImageButton)view.findViewById(R.id.sign_out_btn);

        if (signedIn){
            view.findViewById(R.id.sign_out_btn).setOnClickListener(this);

            userInfoForm.setVisibility(View.VISIBLE);
            sign_out_btn.setVisibility(View.VISIBLE);
            signInForm.setVisibility(View.GONE);
        }
        else{
            view.findViewById(R.id.sign_up_btn).setOnClickListener(this);
            view.findViewById(R.id.log_in_btn).setOnClickListener(this);

            signInForm.setVisibility(View.VISIBLE);
            userInfoForm.setVisibility(View.GONE);
            sign_out_btn.setVisibility(View.GONE);
        }
    }

    private void setMenuAdapter(Context context, View view){
        List<String> types = new ArrayList<>(Arrays.asList(view.getResources().getStringArray(R.array.types)));
        List<String> genres = new ArrayList<>(Arrays.asList(view.getResources().getStringArray(R.array.genre_book)));

        //shows genres of book first
        map.put("Type", "Books");

        MenuAdapter menuAdapter = new MenuAdapter(types);
        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(genres);

        ListView list_menu = view.findViewById(R.id.list_menu);
        ListView list_menu_item = view.findViewById(R.id.menu_item_list);
        
        list_menu.setAdapter(menuAdapter);
        list_menu_item.setAdapter(menuItemAdapter);

        list_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("Type", menuAdapter.getClickedData(i));
                switch (types.get(i)){
                    case "Books":
                        //move to user profile
                        menuItemAdapter.updateData(new ArrayList<>(Arrays.asList(view.getResources().getStringArray(R.array.genre_book))));
                        break;
                    case "Pictures":
                        menuItemAdapter.updateData(new ArrayList<>(Arrays.asList(view.getResources().getStringArray(R.array.genre_picture))));
                        break;
                    case "Paints":
                        menuItemAdapter.updateData(new ArrayList<>(Arrays.asList(view.getResources().getStringArray(R.array.genre_paints))));
                        break;
                    case "Music":
                        menuItemAdapter.updateData(new ArrayList<>(Arrays.asList(view.getResources().getStringArray(R.array.genre_music))));
                        break;
                    case "Etc..":
                        menuItemAdapter.updateData(new ArrayList<>(Arrays.asList(view.getResources().getStringArray(R.array.etc))));
                        break;
                }
            }
        });

        list_menu_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("Genre", menuItemAdapter.getClickedData(i));
                Intent intent = new Intent(mActivity, ArtListActivity.class);
                intent.putExtra("Type", map.get("Type"));
                intent.putExtra("Genre", map.get("Genre"));
                mActivity.startActivity(intent);
                //Log.d("CLICKED:", map.get("Type"));
                //Log.d("CLICKED:", map.get("Genre"));
            }
        });
    }

    private void changeBackGroundColor(Context context, PopupWindow mPopupWindow){
        View container = (View)mPopupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }
}
