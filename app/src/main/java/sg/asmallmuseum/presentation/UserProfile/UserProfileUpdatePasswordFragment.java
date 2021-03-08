package sg.asmallmuseum.presentation.UserProfile;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sg.asmallmuseum.R;

public class UserProfileUpdatePasswordFragment extends Fragment implements View.OnClickListener{

    private View view;
    private FirebaseUser user;

    public UserProfileUpdatePasswordFragment() {
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
        view = inflater.inflate(R.layout.fragment_user_profile_update_password, container, false);

        Button updatePassword = (Button) view.findViewById(R.id.update_password);
        Button backBtn = (Button) view.findViewById(R.id.back_button);

        backBtn.setOnClickListener(this);
        updatePassword.setOnClickListener(this);

        return view;
    }

    public void alertMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setMessage("not same");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getNewPassword(){
        user = FirebaseAuth.getInstance().getCurrentUser();

        EditText newPass = (EditText) view.findViewById(R.id.user_new_password);
        EditText checkPass = (EditText) view.findViewById(R.id.check_user_password);

        String newPassword = newPass.getText().toString();
        String checkPassword = checkPass.getText().toString();

        if(newPassword.equals(checkPassword)){
            updatePassword(newPassword);
        }else if(!newPassword.equals(checkPassword)){
            alertMessage();
        }
    }

    public void updatePassword(String newPassword) {
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();
                            closeWindow();
                        }
                    }
                });
        // [END update_password]
    }

    private void closeWindow(){
        //The FragmentTransaction.remove() only removes a existing fragment from the container, the fragment still leaves in a stack.
        //The FragmentManager.popBackStack() pops a fragment from a stack and removes a existing fragment from the container.

//        FragmentManager fragmentManager = getParentFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.remove(this);
//        fragmentTransaction.commit();

        if (getActivity() instanceof UserProfileActivity){
            ((UserProfileActivity) getActivity()).replaceFragment(UserProfileActivity.REQUEST_END);
        }
    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();
        if (id == R.id.back_button){
            closeWindow();
        }
        else if (id == R.id.update_password){
            getNewPassword();
        }
    }
}