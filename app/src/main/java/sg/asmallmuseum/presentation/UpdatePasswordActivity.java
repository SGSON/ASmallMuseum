package sg.asmallmuseum.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sg.asmallmuseum.R;

public class UpdatePasswordActivity extends AppCompatActivity {
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        EditText newPass = (EditText) findViewById(R.id.user_new_password);
        EditText checkPass = (EditText) findViewById(R.id.check_user_password);
        Button updatePassword = (Button) findViewById(R.id.update_password);
        Button backBtn = (Button) findViewById(R.id.back_button);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String newPassword = newPass.getText().toString();
                String checkPassword = checkPass.getText().toString();
                if(newPassword.equals(checkPassword)){
                    updatePassword(newPassword);
                }else if(!newPassword.equals(checkPassword)){
                   alertMessage();
                }

            }
        });

    }

    public void updatePassword(String newPassword) {

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                           finish();
                           Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END update_password]
    }

    public void alertMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("not same");
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

}