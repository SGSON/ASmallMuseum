package sg.asmallmuseum.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.List;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.persistence.EmailUserDB;

public class Login extends AppCompatActivity implements UserInformInterface {
    private UserManager userManager;
    private FirebaseAuth mAuth;

    EditText emailID;
    EditText password;
    Button loginBtn;
    Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userManager = new UserManager("email");
        userManager.setListener(this);

        emailID = (EditText)findViewById(R.id.emailID);
        password= (EditText)findViewById(R.id.password);
        loginBtn = (Button)findViewById(R.id.logInBtn);
        homeBtn = (Button)findViewById(R.id.homeBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                String uEmailID = emailID.getText().toString();
                String uPassword = password.getText().toString();
                signIn(uEmailID,uPassword);

            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);//<< MainActivity
                startActivity(intent);
            }
        });
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userManager.getUserInfo(email);

                        } else {

                        }

                        if (!task.isSuccessful()) {
                           Toast.makeText(getApplication(),"failed",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }


    @Override
    public void userInfo(List<String> list) {

    }

    @Override
    public void getAllUser(List<String> list) {
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
        intent.putExtra("getUser",user);
        intent.putExtra("getAllUser", (Serializable) sb);
        intent.putExtra("type","email");
        intent.putExtra("code","1");
        startActivity(intent);

    }
}