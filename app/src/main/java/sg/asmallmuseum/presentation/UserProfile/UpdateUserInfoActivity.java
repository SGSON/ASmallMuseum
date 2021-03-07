package sg.asmallmuseum.presentation.UserProfile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.presentation.SignUp.ConfirmUpdataActivity;

public class UpdateUserInfoActivity extends AppCompatActivity {
    private UserManager userManager;
    private FirebaseUser fUser;
    EditText nickname;
    EditText lastName;
    EditText firstName;
    EditText birth;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        nickname = (EditText) findViewById(R.id.user_nickname);
        lastName = (EditText) findViewById(R.id.user_lastname);
        firstName = (EditText) findViewById(R.id.user_firstname);
        birth = (EditText) findViewById(R.id.user_birth);
        Button backBtn = (Button) findViewById(R.id.home_button);
        Button updateBtn = (Button) findViewById(R.id.update_user_information);

        Intent i = getIntent();
        User user = (User)i.getSerializableExtra("getUser");
        type = i.getStringExtra("type");

        if(type.equals("email")){
            userManager = new UserManager("email");
        }else if(type.equals("facebook")){
            userManager = new UserManager("facebook");
        }else if(type.equals("google")){
            userManager = new UserManager("google");
        }

        nickname.setText(user.getuNick());
        firstName.setText(user.getuFirstName());
        lastName.setText(user.getuLastName());
        birth.setText(user.getuBirth());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertMessage();

            }
        });

    }

    public void alertMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("change your profile ?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fUser = FirebaseAuth.getInstance().getCurrentUser();
                String uEmail = fUser.getEmail();
                String uNickname = nickname.getText().toString();
                String uLastName = lastName.getText().toString();
                String uFirstName = firstName.getText().toString();
                String uBirth = birth.getText().toString();
                try {
                    userManager.addNewUser(uNickname,uLastName,uFirstName,uEmail,uBirth);
                } catch (CustomException e) {
                    e.printStackTrace();
                }
                User tempUser = new User(uNickname,uLastName,uFirstName,uEmail,uBirth);
                Intent intent = new Intent(getApplicationContext(), ConfirmUpdataActivity.class);
                intent.putExtra("getUserT",tempUser);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ;
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}