package sg.asmallmuseum.presentation.ArtView2;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArtView2ViewModel extends ViewModel {
    private final MutableLiveData<List<Fragment>> mFragmentList = new MutableLiveData<>();

    public void setFragmentList(List<Fragment> mList){
        this.mFragmentList.setValue(mList);
    }

    public LiveData<List<Fragment>> getFragmentList(){
        return mFragmentList;
    }

}
