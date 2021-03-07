package sg.asmallmuseum.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.List;

import sg.asmallmuseum.Domain.CustomException;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;

public class SignInWelcomeActivity extends AppCompatActivity implements UserInformInterface {
    private FirebaseUser user;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_welcome);

        TextView userNickname = findViewById(R.id.user_nickname);
        Button homeBtn = findViewById(R.id.home_button);

        Intent i = getIntent();
        String type = i.getStringExtra("type");
        String nickName = i.getStringExtra("nickname");
        userNickname.setText(nickName);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if(type.equals("email")){
                    if(user.isEmailVerified() == true){
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        userManager.getUserInfo(user.getEmail());

                    }else if(user.isEmailVerified() == false){
                        alert();
                    }

                }else if(!type.equals("email")){
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    userManager.getUserInfo(user.getEmail());

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        String type = i.getStringExtra("type");

        if(type.equals("email")){
            User nUser = (User) i.getSerializableExtra("nUser");
            userManager = new UserManager("email");
            userManager.setListener(this);

            user = FirebaseAuth.getInstance().getCurrentUser();
            user.reload().addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    if(user.isEmailVerified() == true){

                        try {
                            userManager.addNewUser(nUser.getuNick(), nUser.getuLastName(), nUser.getuFirstName(), user.getEmail(), nUser.getuBirth());
                        } catch (CustomException e) {
                            e.printStackTrace();
                        }

                    }else if (user.isEmailVerified() == false){
                        alert();
                    }
                }

            });
        }else if(type.equals("facebook")){
            userManager = new UserManager("facebook");
            userManager.setListener(this);
        }else if(type.equals("google")){
            userManager = new UserManager("google");
            userManager.setListener(this);
        }

    }

    public void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Click button after confirm your email");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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