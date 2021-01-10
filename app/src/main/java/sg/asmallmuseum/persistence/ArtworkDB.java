package sg.asmallmuseum.persistence;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Book;
import sg.asmallmuseum.logic.DBListener;

public class ArtworkDB implements ArtworkDBInterface {
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    private DBListener mListener;
    private final String TAG = "ART DB Connection: ";
    private final String TAG2 = "UPLOAD: ";

    public ArtworkDB(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void setOnSuccessListener(DBListener mListener){
        this.mListener = mListener;
    }

    /***Upload a image and info to the firestore and the storage***/
    @Override
    public DocumentReference addArt(Artwork art) {

        DocumentReference ref = db.collection("Art").document(art.getaType()).collection(art.getaGenre()).document();
        ref.set(art)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "SUCCESS!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "FAILED");
                }
            });

        return ref;
    }

    @Override
    public StorageReference uploadFile(String path, String id) throws FileNotFoundException {

        InputStream inputStream = new FileInputStream(new File(path));
        StorageReference storageRef = storage.getReference().child(id+".png");
        UploadTask uploadTask = storageRef.putStream(inputStream);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG2, "SUCCESS!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG2, "FAILED!");
            }
        });

        return storageRef;
    }

    @Override
    public void setArtInfo(DocumentReference ref, String field){
        ref.update(field, ref);
    }

    @Override
    public void setFileLoc(DocumentReference docRef, StorageReference storageRef){
        docRef.update("aFileLoc", storageRef.getPath());
    }
    /***End***/

    /***Get a image and a info***/
    public void getArtInfo(String type, String genre){
        List<Artwork> list = new ArrayList<>();
        CollectionReference colRef = db.collection("Art").document(type).collection(genre);
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot collection : task.getResult()){
                        Artwork artwork = collection.toObject(Book.class);
                        list.add(artwork);
                    }
                    mListener.onFileLoadCompleteListener(list);
                }
            }
        });
    }

    @Override
    public StorageReference getArtImage(String type, String loc){
        return storage.getReference().child(loc);
    }
    /***End***/
}
