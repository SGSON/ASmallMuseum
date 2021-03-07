package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.List;

import sg.asmallmuseum.Domain.CustomException;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;

public class ConfirmUpdataActivity extends AppCompatActivity implements UserInformInterface {
    private UserManager userManager;
    private FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_updata);

        TextView nickname = (TextView) findViewById(R.id.user_nickname);
        Button updatePass = (Button) findViewById(R.id.update_password);
        TextView firstName = (TextView) findViewById(R.id.user_firstname);
        TextView lastName = (TextView) findViewById(R.id.user_lastname);
        TextView birth = (TextView) findViewById(R.id.user_birth);
        Button home = (Button) findViewById(R.id.home_button);
        Button updateInfo = (Button) findViewById(R.id.update_user_information);

        Intent i = getIntent();
        User user = (User)i.getSerializableExtra("getUserT");
        String type = i.getStringExtra("type");

        nickname.setText(user.getuNick());
        firstName.setText(user.getuFirstName());
        lastName.setText(user.getuLastName());
        birth.setText(user.getuBirth());

        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fUser = FirebaseAuth.getInstance().getCurrentUser();
                userManager.getUserInfo(fUser.getEmail());
            }
        });

        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UpdateUserInfoActivity.class);
                intent.putExtra("getUser",user);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        String type = i.getStringExtra("type");

        if(type.equals("email")){
            userManager = new UserManager("email");
            userManager.setListener(this);
        }else if(type.equals("facebook")){
            userManager = new UserManager("facebook");
            userManager.setListener(this);
        }else if(type.equals("google")){
            userManager = new UserManager("google");
            userManager.setListener(this);
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void userInfo(List<String> list) {

    }

    @Override
    public void getAllUser(List<String> list) {
        Intent i = getIntent();
        String type = i.getStringExtra("type");
        StringBuilder sb = new StringBuilder();

        User user = new User();

        if(!list.get(3).equals("null")){
            user.setuEmail(list.get(3));
            user.setuNick(list.get(4));
            user.setuLastName(list.get(5));
            user.setuFirstName(list.get(6));
            user.setuBirth(list.get(7));
        }else if(list.get(3).equals("null")){
            ;
        }

        list.set(3,"");

        for(String s : list){
            sb.append(s+"/");
        }

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("getUser",user);
        intent.putExtra("getAllUser", (Serializable) sb);
        intent.putExtra("code","1");

        startActivity(intent);
    }
}