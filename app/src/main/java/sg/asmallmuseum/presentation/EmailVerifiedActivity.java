package sg.asmallmuseum.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.asmallmuseum.Domain.CustomException;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;

public class EmailVerifiedActivity extends AppCompatActivity {
    private FirebaseUser user;
    private UserManager userManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_user_verified);
        mAuth = FirebaseAuth.getInstance();

        Button resendEmail = (Button) findViewById(R.id.resend_email);
        Button next = (Button) findViewById(R.id.next_button);

        Intent i = getIntent();
        User nUser = (User) i.getSerializableExtra("nUser");

        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),SignInWelcomeActivity.class);
                intent.putExtra("type","email");
                intent.putExtra("nUser",nUser);
                intent.putExtra("nickname",nUser.getuNick());
                startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void sendEmailVerification() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),"Success, confirm your email",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Click button after confirm your email");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               ;
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}