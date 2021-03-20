package sg.asmallmuseum.presentation.UserProfile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;

public class UserProfileViewHistoryFragment extends Fragment implements View.OnClickListener{

    private View view;
    private UserProfileViewModel viewModel;

    private UserHistoryViewAdapter adapter;

    public UserProfileViewHistoryFragment() {
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
        view = inflater.inflate(R.layout.fragment_user_profile_view_history, container, false);

        Button mSettings = (Button) view.findViewById(R.id.fragment_user_history_profile_setting);
        Button mBack = (Button) view.findViewById(R.id.back_button);
        Button mMenu = (Button) view.findViewById(R.id.top_menu_button);

        mSettings.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mMenu.setOnClickListener(this);

        setRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    setUser(user);
                }
                else {
                    Toast.makeText(getContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_user_history_recycler_view);
        adapter = new UserHistoryViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void setUser(User user){
        TextView mNick = (TextView) view.findViewById(R.id.fragment_user_history_user_nick);
        TextView mEmail = (TextView) view.findViewById(R.id.fragment_user_history_user_email);
        ImageView mImage = (ImageView) view.findViewById(R.id.fragment_user_history_user_image);

        mNick.setText(user.getuNick());
        mEmail.setText(user.getuEmail());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.top_menu_button){
            if (getActivity() instanceof UserProfileActivity){
                ((UserProfileActivity) getActivity()).openMainMenu();
            }
        }
        else if (id == R.id.back_button){
            getActivity().finish();
        }
        else if (id == R.id.fragment_user_history_profile_setting){
            if (getActivity() instanceof UserProfileActivity){
                ((UserProfileActivity) getActivity()).replaceFragment(UserProfileActivity.REQUEST_PROFILE);
            }
        }
    }
}