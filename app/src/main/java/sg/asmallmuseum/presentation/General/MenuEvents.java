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
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.firebase.auth.FirebaseAuth;

import androidx.constraintlayout.widget.ConstraintLayout;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.LoginActivity;
import sg.asmallmuseum.presentation.SignUpActivity;

public class MenuEvents implements View.OnClickListener {
    private PopupWindow popupWindow;
    private final FirebaseAuth mAuth;
    private final Activity mActivity;

    public MenuEvents(FirebaseAuth mAuth, Activity act){
        this.mAuth = mAuth;
        this.mActivity = act;
    }

    public void openMenu(boolean signedIn){
        LayoutInflater layoutInflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menu = layoutInflater.inflate(R.layout.layout_main_menu, null);

        popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
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
        MenuAdapter menuAdapter = new MenuAdapter();
        ExpandableListView list_menu = view.findViewById(R.id.list_menu);
        
        list_menu.setAdapter(menuAdapter);
        list_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i){
                    case 0:
                        //move to user profile
                        //intent = new Intent(mActivity, );
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
                //Log.d("POS:", ""+i);
                //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
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
