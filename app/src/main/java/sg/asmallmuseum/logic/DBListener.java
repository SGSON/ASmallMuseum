package sg.asmallmuseum.logic;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import sg.asmallmuseum.Domain.Artwork;

public interface DBListener {
    void onFileLoadCompleteListener(List<Artwork> list);
}
