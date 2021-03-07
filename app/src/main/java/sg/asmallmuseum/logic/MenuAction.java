package sg.asmallmuseum.logic;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.presentation.LoginForm;
import sg.asmallmuseum.presentation.MenuAdapter;
import sg.asmallmuseum.presentation.SignUp;
import sg.asmallmuseum.presentation.UserProfileActivity;

public class MenuAction implements View.OnClickListener {
    private User getUser;
    private String type;
    private Context context;
    private FirebaseUser user;
    public MenuAction(){}

    public void getUserConnection(User getUser){
        this.getUser = getUser;
    }
    public void getUserType(String type){this.type=type; };
    public void openMenu(Context context){
        this.context = context;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menu = layoutInflater.inflate(R.layout.layout_main_menu, null);

        ImageButton loginBtn = (ImageButton) menu.findViewById(R.id.logInBtn);
        ImageButton signUpBtn = (ImageButton) menu.findViewById(R.id.signUpBtn);
        Button logOutBtn = (Button) menu.findViewById(R.id.logout_button);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginForm.class);
                context.startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SignUp.class);
                intent.putExtra("sign","null");
                context.startActivity(intent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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
                String name = menuAdapter.getItem(i).toString();
                if(name.equals("Profile")){
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user != null){
                        Intent intent = new Intent(context.getApplicationContext(), UserProfileActivity.class);
                        intent.putExtra("getUser",getUser);
                        intent.putExtra("type",type);
                        context.startActivity(intent);
                    }else if( user == null){
                        Toast.makeText(context,"Please sign in",Toast.LENGTH_SHORT).show();
                    }

                }
                Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
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
