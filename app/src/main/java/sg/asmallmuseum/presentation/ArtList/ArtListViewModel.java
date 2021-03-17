package sg.asmallmuseum.presentation.ArtList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArtListViewModel extends ViewModel {
    private final MutableLiveData<String> mCategory = new MutableLiveData<>();
    private final MutableLiveData<String> mType = new MutableLiveData<>();
    private final MutableLiveData<String[]> mItems = new MutableLiveData<>();

    public void setCategory(String category){
        mCategory.setValue(category);
    }

    public LiveData<String> getCategory(){
        return mCategory;
    }

    public void setType(String type){
        mType.setValue(type);
    }

    public LiveData<String> getType(){
        return mType;
    }

    public void setItems(String[] items){
        this.mItems.setValue(items);
    }

    public LiveData<String[]> getItems(){
        return mItems;
    }
}
