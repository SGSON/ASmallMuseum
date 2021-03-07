package sg.asmallmuseum.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.MenuAction;
import sg.asmallmuseum.logic.UserManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UserInformInterface{
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UserManager userManager;
    private GoogleSignInClient mGoogleSignInClient;
    private String code;
    private ImageButton mQuick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button confirm = (Button)findViewById(R.id.confirm);
        Button logOut = (Button)findViewById(R.id.logOut);
        Button getAllUser = (Button)findViewById(R.id.get_all_user_button);
        Button goToIntentActivity = (Button)findViewById(R.id.go_to_intent);

        Intent i = getIntent();
        User getU = (User) i.getSerializableExtra("getUser");
        String type = i.getStringExtra("type");
        String getAllUserS = i.getStringExtra("getAllUser");
        code = i.getStringExtra("code");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null ) {
                    Toast.makeText(getApplicationContext(), " type : "+type +" /userEmail : "+ user.getEmail()+" /firstName : "+getU.getuFirstName(),Toast.LENGTH_LONG).show();

                } else{
                    Toast.makeText(getApplicationContext(),"Please sign in ",Toast.LENGTH_SHORT).show();

                }
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                googleSignOut();
                startActivity(getIntent());
            }
        });

        getAllUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),getAllUserS+" ",Toast.LENGTH_SHORT).show();
            }
        });

        goToIntentActivity.setText("app exit");
        goToIntentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finishAffinity();
                //finishAndRemoveTask();
                //moveTaskToBack(true);						// 태스크를 백그라운드로 이동
                //finishAndRemoveTask();						// 액티비티 종료 + 태스크 리스트에서 지우기
                //android.os.Process.killProcess(android.os.Process.myPid());	// 앱 프로세스 종료
                System.exit(0);
            }
        });

    }


    public void googleSignOut(){
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        /*페이지 간 넘겨야할 정보
        * type
        * getUser
        **/
        if(user !=null){
            Intent i = getIntent();
            String type = i.getStringExtra("type");

            if(type != null){

                userManager = new UserManager(type);
                userManager.setListener(this);

                User getUser = (User) i.getSerializableExtra("getUser");
                String[] getAllUser=i.getStringExtra("getAllUser").split("/");

                List<String> str = new ArrayList<>();
                str.add("00");
                for(String s : getAllUser) {
                    if (s.equals(user.getEmail())) {
                        str.set(0,"1");
                    }
                }

                if (str.get(0).equals("1")) {
                    /*userManager.deleteUser(user.getEmail());
                    deleteUser();
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();*/
                    //alert();

                }else if(str.get(0).equals("00")) {
                    if(getUser.getuFirstName() == null && type.equals("email")) {
                        userManager.getTempUserInfo(user.getEmail());

                    }else if(getUser.getuFirstName() == null && type.equals("facebook")){
                        Intent intent = new Intent(getApplicationContext(),FacebookUserDBActivity.class);
                        intent.putExtra("type","facebook");
                        startActivity(intent);

                    }else if(getUser.getuFirstName() == null && type.equals("google")){
                        Intent intent = new Intent(getApplicationContext(),GoogleUserDBActivity.class);
                        intent.putExtra("type","google");
                        startActivity(intent);

                    }
                }



            }
        }
    }

    @Override
    public void onBackPressed() {
        String code2 = code;
        if(code2 != null){
            ;
        }else if(code2 == null) {
            super.onBackPressed();
        }

    }

    public void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("exist same emailID, failed");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(getIntent());
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void deleteUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onMainButtonPressed(View view) {
        Toast.makeText(this, "Pressed Main Button", Toast.LENGTH_SHORT).show();
    }

    public void onMenuButtonPressed(View view) {
        Intent i = getIntent();
        User getUser = (User)getIntent().getSerializableExtra("getUser");
        String type = i.getStringExtra("type");
        Toast.makeText(this, "Pressed Menu Button", Toast.LENGTH_SHORT).show();
        MenuAction menuAction = new MenuAction();
        menuAction.openMenu(this);
        menuAction.getUserConnection(getUser);
        menuAction.getUserType(type);
    }

    public void onBackButtonPressed(View view) {
        Toast.makeText(this, "Pressed Back Button", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void userInfo(List<String> list) {

        User user = new User(list.get(1),list.get(2),list.get(3),list.get(0),list.get(4));
        Intent intent = new Intent(getApplicationContext(), EmailVerifiedActivity.class);
        intent.putExtra("type","email");
        intent.putExtra("nUser",user);
        startActivity(intent);

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

        Intent intent = new Intent(getApplicationContext(),SignUp.class);
        intent.putExtra("getAllUser", (Serializable) sb);
        intent.putExtra("type","email");
        startActivity(intent);

    }


}