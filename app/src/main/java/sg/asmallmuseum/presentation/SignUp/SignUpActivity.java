package sg.asmallmuseum.presentation.SignUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.persistence.EmailUserDB;
import sg.asmallmuseum.presentation.General.MainActivity;
import sg.asmallmuseum.presentation.SignUp.EmailVerifiedActivity;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private UserManager userManager;

    EditText emailID;
    EditText ID;
    EditText password;
    EditText birth;
    EditText firstName;
    EditText lastName;
    EditText checkPass;

    Button backBtn;
    Button submitBtn;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        userManager = new UserManager("email");

        String arrayList[] = "input, google, naver, ??".split(",");
        String arrayList2[] ="input,gmail.com,naver.com,fdf.com".split(",");
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);


        Spinner spinner =(Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);

        EditText spinnerEditText = (EditText) findViewById(R.id.spinner_edit);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    spinnerEditText.setText(null);
                    spinnerEditText.setBackgroundResource(android.R.drawable.edit_text);
                    spinnerEditText.setFocusableInTouchMode(true);
                    spinnerEditText.setClickable(true);
                    spinnerEditText.setFocusable(true);

                }else if(position != 0){
                    spinnerEditText.setBackground(null);
                    spinnerEditText.setClickable(false);
                    spinnerEditText.setFocusable(false);
                    spinnerEditText.setText(arrayList2[position]);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        emailID = (EditText)findViewById(R.id.emailID);
        ID = (EditText)findViewById(R.id.ID);
        password = (EditText)findViewById(R.id.password);
        checkPass = (EditText)findViewById(R.id.check_pass);
        birth = (EditText)findViewById(R.id.birth);
        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        backBtn = (Button)findViewById(R.id.backBtn);
        submitBtn = (Button)findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eForm = spinnerEditText.getText().toString();
                String uEmailID = emailID.getText().toString()+"@"+spinnerEditText.getText().toString();
                String uPassword = password.getText().toString();
                String checkPassword = checkPass.getText().toString();
                String uNick = ID.getText().toString();
                String uBirth = birth.getText().toString();
                String uFirstName = firstName.getText().toString();
                String uLastName = lastName.getText().toString();

                if(!eForm.equals("") && !uEmailID.equals("") && !uPassword.equals("") && !checkPassword.equals("") && !uNick.equals("") && !uBirth.equals("") && !uFirstName.equals("") && !uLastName.equals("") ){
                    if(uPassword.equals(checkPassword)){
                        Intent i = getIntent();
                        String uInfo = i.getStringExtra("sign");
                        if(uInfo != null){
                            EmailUserDB emailUserDB = new EmailUserDB();

                            User user = new User(uNick, uLastName, uFirstName, uEmailID, uBirth);
                            emailUserDB.addTempUser(user);

                            createAccount(uEmailID,uPassword);
                        }else if(uInfo == null){
                            String[] getAllUser=i.getStringExtra("getAllUser").split("/");

                            if(getAllUser != null){
                                EmailUserDB emailUserDB = new EmailUserDB();

                                List<String> str = new ArrayList<>();
                                str.add("00");
                                for(String s : getAllUser) {
                                    if (s.equals(uEmailID)) {
                                        str.set(0,"1");
                                    }
                                }
                                if (str.get(0).equals("1")) {
                                    alert2();

                                }else if(str.get(0).equals("00")){
                                    User user = new User(uNick, uLastName, uFirstName, uEmailID, uBirth);
                                    emailUserDB.addTempUser(user);

                                    createAccount(uEmailID,uPassword);
                                }

                            }
                        }

                    }else if(!uPassword.equals(checkPassword)){
                        alert();
                    }

                }else {
                    alert3();
                }



            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"MainActivity",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification();
                            Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public void sendEmailVerification() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Confirm your email",Toast.LENGTH_LONG).show();

                            String uEmailID = emailID.getText().toString();
                            String uNick = ID.getText().toString();
                            String uBirth = birth.getText().toString();
                            String uFirstName = firstName.getText().toString();
                            String uLastName = lastName.getText().toString();

                            User nUser = new User(uNick, uLastName, uFirstName, uEmailID, uBirth);
                            Intent intent = new Intent(getApplicationContext(), EmailVerifiedActivity.class);
                            intent.putExtra("type","email");
                            intent.putExtra("nUser", nUser);
                            startActivity(intent);


                        }
                    }
                });

    }

    public void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("confirm your password");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alert2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("exist same email, fail");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alert3(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("null exist, fail");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {

    }

}