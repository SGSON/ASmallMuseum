package sg.asmallmuseum.presentation.UserProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView nickname = (TextView) findViewById(R.id.user_nickname);
        Button updatePass = (Button) findViewById(R.id.update_password);
        TextView firstName = (TextView) findViewById(R.id.user_firstname);
        TextView lastName = (TextView) findViewById(R.id.user_lastname);
        TextView birth = (TextView) findViewById(R.id.user_birth);
        Button home = (Button) findViewById(R.id.home_button);
        Button updateInfo = (Button) findViewById(R.id.update_user_information);

        Intent i = getIntent();
        User user = (User)i.getSerializableExtra("getUser");
        String type = i.getStringExtra("type");

        nickname.setText(user.getuNick());
        firstName.setText(user.getuFirstName());
        lastName.setText(user.getuLastName());
        birth.setText(user.getuBirth());

        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateUserInfoActivity.class);
                intent.putExtra("getUser",user);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });


    }

}