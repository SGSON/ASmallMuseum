package sg.asmallmuseum.presentation.ArtView;

import android.net.Uri;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import sg.asmallmuseum.Domain.Comment;

public class ArtViewViewModel extends ViewModel {
    private final MutableLiveData<List<Uri>> mUriList = new MutableLiveData<>();
    private final MutableLiveData<Integer> mCurrentPage = new MutableLiveData<>();
    private final MutableLiveData<String> mPath = new MutableLiveData<>();

    public void setmPath(String path){this.mPath.setValue(path);}

    public MutableLiveData<String> getmPath() {
        return mPath;
    }

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
