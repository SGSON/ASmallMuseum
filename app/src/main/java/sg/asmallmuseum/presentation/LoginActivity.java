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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;

    EditText emailID;
    EditText password;
    Button loginBtn;
    Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailID = (EditText)findViewById(R.id.email_id);
        password= (EditText)findViewById(R.id.password);
        loginBtn = (Button)findViewById(R.id.log_in_btn);
        homeBtn = (Button)findViewById(R.id.home_btn);

        homeBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.log_in_btn){
            mAuth = FirebaseAuth.getInstance();

            String uEmailID = emailID.getText().toString();
            String uPassword = password.getText().toString();
            signIn(uEmailID,uPassword);
        }
        else if (viewId == R.id.home_btn){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);//<< MainActivity
            startActivity(intent);
        }
    }

    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void onMainButtonPressed(View view) {
        Toast.makeText(this, "Pressed Main Button", Toast.LENGTH_SHORT).show();
    }

    public void onMenuButtonPressed(View view) {
        Toast.makeText(this, "Pressed Menu Button", Toast.LENGTH_SHORT).show();
        MenuAction menuAction = new MenuAction();
        menuAction.openMenu(this, false);
    }

    public void onBackButtonPressed(View view) {
        Toast.makeText(this, "Pressed Back Button", Toast.LENGTH_SHORT).show();
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