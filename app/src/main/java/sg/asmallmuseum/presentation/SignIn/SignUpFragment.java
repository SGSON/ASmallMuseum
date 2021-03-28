package sg.asmallmuseum.presentation.SignIn;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.Messages.UserBirthError;
import sg.asmallmuseum.Domain.Messages.UserEmailError;
import sg.asmallmuseum.Domain.Messages.UserNameError;
import sg.asmallmuseum.Domain.Messages.UserPasswordError;
import sg.asmallmuseum.Domain.RequestCode;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.UserManager;
import sg.asmallmuseum.logic.ValidateUser;

public class SignUpFragment extends Fragment implements View.OnClickListener{

    private View view;
    private SignInViewModel viewModel;

    private FirebaseAuth mAuth;


    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth = FirebaseAuth.getInstance();

        Button backBtn = (Button) view.findViewById(R.id.fragment_sign_up_back_button);
        Button submitBtn = (Button) view.findViewById(R.id.fragment_sign_up_submit_button);

        backBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        //setSpinner();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SignInViewModel.class);

        viewModel.getEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                setText(s);
            }
        });
    }

    private void setText(String eMail){
        ((EditText)view.findViewById(R.id.fragment_sign_up_email)).setText(eMail);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_sign_up_back_button){
            getActivity().finish();
        }
        else if (id == R.id.fragment_sign_up_submit_button){
            getUserInput();
        }
    }

//    private void setSpinner(){
//        String arrayList[] = "input, google, naver, ??".split(",");
//        String arrayList2[] ="input,gmail.com,naver.com,fdf.com".split(",");
//        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_spinner_dropdown_item,
//                arrayList);
//
//
//        Spinner spinner =(Spinner)view.findViewById(R.id.fragment_sign_up_spinner);
//        spinner.setAdapter(arrayAdapter);
//
//        EditText spinnerEditText = (EditText) view.findViewById(R.id.fragment_sign_up_spinner_edit);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position == 0){
//                    spinnerEditText.setText(null);
//                    spinnerEditText.setBackgroundResource(android.R.drawable.edit_text);
//                    spinnerEditText.setFocusableInTouchMode(true);
//                    spinnerEditText.setClickable(true);
//                    spinnerEditText.setFocusable(true);
//
//                }else if(position != 0){
//                    spinnerEditText.setBackground(null);
//                    spinnerEditText.setClickable(false);
//                    spinnerEditText.setFocusable(false);
//                    spinnerEditText.setText(arrayList2[position]);
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }

    private void getUserInput(){
        EditText emailID = (EditText) view.findViewById(R.id.fragment_sign_up_email);
        EditText ID = (EditText) view.findViewById(R.id.fragment_sign_up_nick);
        EditText password = (EditText) view.findViewById(R.id.fragment_sign_up_password);
        EditText checkPass = (EditText) view.findViewById(R.id.fragment_sign_up_check_pass);
        EditText birth = (EditText) view.findViewById(R.id.fragment_sign_up_birth);
        EditText firstName = (EditText) view.findViewById(R.id.fragment_sign_up_first_name);
        EditText lastName = (EditText) view.findViewById(R.id.fragment_sign_up_last_name);
        //EditText spinnerEditText = (EditText) view.findViewById(R.id.fragment_sign_up_spinner_edit);

        //String eForm = spinnerEditText.getText().toString();
        String uEmailID = emailID.getText().toString();//+"@"+spinnerEditText.getText().toString();
        String uPassword = password.getText().toString();
        String checkPassword = checkPass.getText().toString();
        String uNick = ID.getText().toString();
        String uBirth = birth.getText().toString();
        String uFirstName = firstName.getText().toString();
        String uLastName = lastName.getText().toString();


        User user = new User(uNick, uLastName, uFirstName, uEmailID, uBirth, "eMail");
        try {
            ValidateUser.validEmailUser(user, uPassword, checkPassword);
            createAccount(user, user.getuEmail(), uPassword);
        }
        catch (CustomException e){
            if (e instanceof UserEmailError){
                emailID.setError(e.getErrorMsg());
            }
            else if (e instanceof UserPasswordError){
                password.setError(e.getErrorMsg());
            }
            else if (e instanceof UserNameError){

            }
            else if (e instanceof UserBirthError){
                birth.setError("Invalid Date of Birth");
            }
        }
    }

    private void createAccount(User user, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification(user);

                            UserManager userManager = new UserManager();
                            userManager.addNewUser(user);

                            Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void sendEmailVerification(User mUser) {

        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mFirebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),"Confirm your email",Toast.LENGTH_LONG).show();

                            viewModel.setUser(mUser);
                            if(getActivity() instanceof SignInActivity){
                                ((SignInActivity) getActivity()).replaceFragment(RequestCode.REQUEST_EMAIL_VERIFY);
                            }
                        }
                    }
                });
    }

    public void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
}