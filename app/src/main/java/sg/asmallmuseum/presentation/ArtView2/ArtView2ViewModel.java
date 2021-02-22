package sg.asmallmuseum.presentation.ArtView2;

import android.net.Uri;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArtView2ViewModel extends ViewModel {
    private final MutableLiveData<List<Uri>> mUriList = new MutableLiveData<>();
    private final MutableLiveData<Integer> mCurrentPage = new MutableLiveData<>();

    public void setUriList(List<Uri> mList){
        this.mUriList.setValue(mList);
    }

    public LiveData<List<Uri>> getUriList(){
        return mUriList;
    }

    public void setmCurrentPage(int page){
        mCurrentPage.setValue(page);
    }

    public MutableLiveData<Integer> getmCurrentPage() {
        return mCurrentPage;
    }
}
