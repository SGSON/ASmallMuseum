package sg.asmallmuseum.presentation.General;

import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainMenuViewModel extends ViewModel {
    private final MutableLiveData<FirebaseUser> mUser = new MutableLiveData<>();

    public void setUser(FirebaseUser user){
        mUser.setValue(user);
    }

    public LiveData<FirebaseUser> getUser(){
        return mUser;
    }

}
