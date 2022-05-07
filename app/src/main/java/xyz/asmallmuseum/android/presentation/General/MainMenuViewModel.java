package xyz.asmallmuseum.android.presentation.General;

import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import xyz.asmallmuseum.android.Domain.User;

public class MainMenuViewModel extends ViewModel {
    private final MutableLiveData<FirebaseUser> mFirebaseUser = new MutableLiveData<>();
    private final MutableLiveData<User> mUser = new MutableLiveData<>();
    private final MutableLiveData<String> mType = new MutableLiveData<>();

    public void setFirebaseUser(FirebaseUser user){
        mFirebaseUser.setValue(user);
    }

    public LiveData<FirebaseUser> getFirebaseUser(){
        return mFirebaseUser;
    }

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
