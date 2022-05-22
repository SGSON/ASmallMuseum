package xyz.asmallmuseum.android.presentation.UserProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import xyz.asmallmuseum.android.domain.User;

public class UserProfileViewModel extends ViewModel {
    private final MutableLiveData<User> mUser = new MutableLiveData<>();
    private final MutableLiveData<String> mType = new MutableLiveData<>();

    public void setUser(User user){
        mUser.setValue(user);
    }

    public LiveData<User> getUser(){
        return mUser;
    }

    public void setType(String type){
        mType.setValue(type);
    }

    public LiveData<String> getType(){
        return mType;
    }
}
