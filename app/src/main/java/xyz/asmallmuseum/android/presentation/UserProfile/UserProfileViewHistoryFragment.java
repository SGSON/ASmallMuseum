package xyz.asmallmuseum.android.presentation.UserProfile;

import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import xyz.asmallmuseum.android.Domain.RequestCode;
import xyz.asmallmuseum.android.Domain.User;
import xyz.asmallmuseum.android.Domain.Values;
import xyz.asmallmuseum.android.R;

public class UserProfileViewHistoryFragment extends Fragment implements View.OnClickListener {

    private View view;
    private UserProfileViewModel viewModel;

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

        setViewPager();
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

    private void setUser(User user){
        TextView mNick = (TextView) view.findViewById(R.id.fragment_user_history_user_nick);
        TextView mEmail = (TextView) view.findViewById(R.id.fragment_user_history_user_email);
        ImageView mImage = (ImageView) view.findViewById(R.id.fragment_user_history_user_image);

        mNick.setText(user.getuNick());
        mEmail.setText(user.getuEmail());

        Uri imageUri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        if (imageUri != null){
            Glide.with(getContext()).load(imageUri).apply(new RequestOptions().circleCrop()).into(mImage);
        }
        setTabLayout("", 0);
    }

    private void setViewPager(){
        ViewPager2 viewPager = (ViewPager2) view.findViewById(R.id.fragment_user_history_view_pager);
        viewPager.setOffscreenPageLimit(2);
        UserHistoryViewPagerAdapter viewAdapter = new UserHistoryViewPagerAdapter(this);
        viewPager.setAdapter(viewAdapter);
    }

    public void setTabLayout(String type, int numPost){
        TabLayout tab = (TabLayout) view.findViewById(R.id.fragment_user_history_tab_layout);
        ViewPager2 viewPager = (ViewPager2) view.findViewById(R.id.fragment_user_history_view_pager);
        TabLayoutMediator mediator = new TabLayoutMediator(tab, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String text = "";
                if (position == 0){
                    text = Values.USER_POST;
                }
                else if (position == 1){
                    text = Values.USER_REVIEW;
                }
                else if (position == 2){
                    text = Values.USER_LIKE;
                }
                tab.setText(text);
            }
        });
        mediator.attach();
    }

    public void setNumPosts(String type, int numPosts){
        if (type.equals(Values.USER_POST)){
            TextView mNumPost = (TextView) view.findViewById(R.id.fragment_user_history_post);
            String text = Integer.toString(numPosts);
            mNumPost.setText(text);
        }
        else if (type.equals(Values.USER_COMMENTS)){
            TextView mNumPost = (TextView) view.findViewById(R.id.fragment_user_history_review);
            String text = Integer.toString(numPosts);
            mNumPost.setText(text);
        }
        else if (type.equals(Values.USER_LIKE)){
            TextView mNumPost = (TextView) view.findViewById(R.id.fragment_user_history_like);
            String text = Integer.toString(numPosts);
            mNumPost.setText(text);
        }
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
                ((UserProfileActivity) getActivity()).replaceFragment(RequestCode.REQUEST_PROFILE);
            }
        }
    }


}