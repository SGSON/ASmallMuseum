package xyz.asmallmuseum.android.presentation.ArtView;

import android.net.Uri;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import xyz.asmallmuseum.android.Domain.Artwork;

public class ArtViewViewModel extends ViewModel {
    private final MutableLiveData<List<Uri>> mUriList = new MutableLiveData<>();
    private final MutableLiveData<Integer> mCurrentPage = new MutableLiveData<>();
    private final MutableLiveData<String> mPath = new MutableLiveData<>();
    private final MutableLiveData<Artwork> mArtwork = new MutableLiveData<>();

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

    public LiveData<Artwork> getArtwork(){
        return mArtwork;
    }

    public void setArtwork(Artwork artwork){
        mArtwork.setValue(artwork);
    }
}
