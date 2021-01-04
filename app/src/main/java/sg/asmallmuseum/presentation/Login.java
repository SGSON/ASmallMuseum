package sg.asmallmuseum.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import sg.asmallmuseum.R;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText emailID;
    EditText password;
    Button loginBtn;
    Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

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
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplication(),"success",Toast.LENGTH_SHORT).show();

                        } else {

                        }

                        if (!task.isSuccessful()) {
                           Toast.makeText(getApplication(),"failed",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }



}