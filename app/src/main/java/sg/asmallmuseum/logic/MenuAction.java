package sg.asmallmuseum.logic;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;

import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.MenuAdapter;
import sg.asmallmuseum.presentation.Login;
import sg.asmallmuseum.presentation.SignUp;

public class MenuAction implements View.OnClickListener {
    private Context context;
    public MenuAction(){}

    public void openMenu(Context context){
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menu = layoutInflater.inflate(R.layout.layout_main_menu, null);

        ImageButton loginBtn = (ImageButton) menu.findViewById(R.id.logInBtn);
        ImageButton signUpBtn = (ImageButton) menu.findViewById(R.id.signUpBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Login.class);
                context.startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SignUp.class);
                context.startActivity(intent);
            }
        });

        PopupWindow popupWindow = new PopupWindow(menu, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(menu, Gravity.RIGHT,0,0);

        changeBackGroundColor(context, popupWindow);
        setMenuAdapter(context, menu);
        closePopupWindow(menu, popupWindow);
    }

    private void setMenuAdapter(Context context, View view){
        MenuAdapter menuAdapter = new MenuAdapter();
        ListView list_menu = view.findViewById(R.id.list_menu);

        list_menu.setAdapter(menuAdapter);
        list_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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

    private void closePopupWindow(View view, PopupWindow popupWindow){
        ImageButton close = (ImageButton) view.findViewById(R.id.menu_close);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
