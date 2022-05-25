package xyz.asmallmuseum.android.presentation.SignIn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import xyz.asmallmuseum.android.domain.Messages.CustomException;
import xyz.asmallmuseum.android.domain.RequestCode;
import xyz.asmallmuseum.android.domain.User;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.logic.UserManager;
import xyz.asmallmuseum.android.logic.ValidateUser;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.UserLoadListener;

public class SignUpGetEmailFragment extends Fragment implements UserLoadListener, View.OnClickListener {
    private View view;
    private UserManager manager;
    private SignInViewModel viewModel;

    public SignUpGetEmailFragment() {
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
        view = inflater.inflate(R.layout.fragment_sign_up_get_email, container, false);

        manager = new UserManager();
        manager.setUserLoadListener(this);

        Button mButton = (Button) view.findViewById(R.id.fragment_sign_up_get_email_button);
        mButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SignInViewModel.class);
        viewModel.getManager().observe(getViewLifecycleOwner(), new Observer<UserManager>() {
            @Override
            public void onChanged(UserManager userManager) {
                setManager(userManager);
            }
        });
    }

    @Override
    public void userInfo(User user) {
        if (user == null){
            EditText mEdit = (EditText) view.findViewById(R.id.fragment_sign_up_get_email);
            viewModel.setEmail(mEdit.getText().toString());
            if (getActivity() instanceof SignInActivity){
                ((SignInActivity) getActivity()).replaceFragment(RequestCode.REQUEST_EMAIL_SIGN_UP);
            }
        }
        else {
            //alert.
        }
    }

    @Override
    public void getAllUser(List<String> list) {

    }

    @Override
    public void onClick(View view) {
        EditText mEdit = (EditText) this.view.findViewById(R.id.fragment_sign_up_get_email);
        String text = mEdit.getText().toString();
        try {
            ValidateUser.validEmail(text);
            manager.getUserInfo(text, 0);
        }
        catch (CustomException e){
            mEdit.setError("Invalid Email.");
        }

    }

    private void setManager(UserManager manager){
        this.manager = manager;
        this.manager.setUserLoadListener(this);
    }
}