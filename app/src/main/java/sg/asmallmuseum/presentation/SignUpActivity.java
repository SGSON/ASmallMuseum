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

import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.presentation.General.MainActivity;
import sg.asmallmuseum.presentation.General.MenuEvents;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;

    EditText emailID;
    EditText ID;
    EditText password;
    EditText birth;
    EditText firstName;
    EditText lastName;
    Button backBtn;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        UserManager userManager = new UserManager("eMail");

        emailID = (EditText)findViewById(R.id.email_id);
        ID = (EditText)findViewById(R.id.id);
        password = (EditText)findViewById(R.id.password);
        birth = (EditText)findViewById(R.id.birth);
        firstName = (EditText)findViewById(R.id.first_name);
        lastName = (EditText)findViewById(R.id.last_name);
        backBtn = (Button)findViewById(R.id.back_btn);
        submitBtn = (Button)findViewById(R.id.submit_btn);


        submitBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onMainButtonPressed(View view) {
        Toast.makeText(this, "Pressed Main Button", Toast.LENGTH_SHORT).show();
    }

    public void onMenuButtonPressed(View view) {
        Toast.makeText(this, "Pressed Menu Button", Toast.LENGTH_SHORT).show();
        MenuEvents menuEvents = new MenuEvents(mAuth, this);
        menuEvents.openMenu(false);
    }

    public void onBackButtonPressed(View view) {
        Toast.makeText(this, "Pressed Back Button", Toast.LENGTH_SHORT).show();
    }



    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }

    public void sendData(){

    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.submit_btn){
            mAuth = FirebaseAuth.getInstance();

            String uEmailID = emailID.getText().toString();
            String uID = ID.getText().toString();
            String uPassword = password.getText().toString();
            String uBirth = birth.getText().toString();
            String uFirstName = firstName.getText().toString();
            String uLastName = lastName.getText().toString();
            createAccount(uEmailID,uPassword);
        }
        else if (viewId == R.id.back_btn){
            Toast.makeText(getApplicationContext(),"MainActivity",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }
}