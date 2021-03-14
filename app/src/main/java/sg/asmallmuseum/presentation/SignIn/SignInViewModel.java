package sg.asmallmuseum.presentation.SignIn;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.logic.UserManager;

public class SignInViewModel extends ViewModel {
    private final MutableLiveData<UserManager> mManager = new MutableLiveData<>();
    private final MutableLiveData<User> mUser = new MutableLiveData<>();
    private final MutableLiveData<String> mType = new MutableLiveData<>();
    private final MutableLiveData<String> mEmail = new MutableLiveData<>();

    public void setManager(UserManager manager){
        this.mManager.setValue(manager);
    }

    public LiveData<UserManager> getManager(){
        return mManager;
    }

    public void setUser(User user){
        this.mUser.setValue(user);
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

    public void setEmail(String email){
        mType.setValue(email);
    }

    public LiveData<String> getEmail(){
        return mEmail;
    }
}
