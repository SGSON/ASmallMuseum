package sg.asmallmuseum.persistence;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final int MAX_LIST_SIZE = 16;

    public ArtworkDB(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void setListener(DBListener mListener){
        this.mListener = mListener;
    }

    /***Upload a image and info to the firestore and the storage***/
    @Override
    public void addArt(Artwork art, List<Uri> paths, List<String> ext) {

        DocumentReference ref = db.collection("Art").document(art.getaType()).collection(art.getaGenre()).document();
        DocumentReference recentRef = db.collection("Art").document("Recent");
        Map<String, String> map = new HashMap<>();
        ref.set(art)
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "SUCCESS!");
                List<String> refs = setReferences(ext, ref.getId(), art);
                ref.update("aID", ref);
                ref.update("aFileLoc", refs);

                updateRecentFile(ref.getPath());

                mListener.onInfoUploadCompleteListener(true, paths, refs, ref.getId(), art);
            }).addOnFailureListener(e -> {
                Log.w(TAG, "FAILED");
                mListener.onInfoUploadCompleteListener(false, null, null, null, null);
            });
    }

    @Override
    public void uploadFile(List<Uri> paths, List<String> refs, String id, Artwork art) throws FileNotFoundException {
        for (int i = 0 ; i < paths.size() ; i++){
            StorageReference ref = storage.getReference().child(refs.get(i));
            UploadTask uploadTask = ref.putFile(paths.get(i));

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Log.d(TAG2, "SUCCESS!");
                mListener.onFileUploadCompleteListener(true);
            }).addOnFailureListener(e -> {
                Log.w(TAG2, "FAILED!");
                mListener.onFileUploadCompleteListener(false);
            });
        }
    }

    private void updateRecentFile(String path){
        DocumentReference ref = db.collection("Art").document("Recent");
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot != null){
                        Map<String, Object> map = snapshot.getData();
                        List<String> result = (List<String>)map.get("Locs");
                        result.add(0, path);
                        if (result.size() >= MAX_LIST_SIZE)
                            result.remove(result.size());

                        map.put("Locs", result);
                        ref.set(map);
                    }
                }
            }
        });
    }

    /***Get a image and a info***/
    public void getArtInfoList(String type, String genre){
        List<Artwork> list = new ArrayList<>();

        //need to checknull pointer
        CollectionReference colRef = db.collection("Art").document(type).collection(genre);
        colRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot collection : Objects.requireNonNull(task.getResult())){
                    Artwork artwork = collection.toObject(Book.class);
                    list.add(artwork);
                }
                mListener.onFileDownloadCompleteListener(list, 0);
            }
        });
    }

    @Override
    public List<StorageReference> getArtImages(String type, List<String> loc){
        List<StorageReference> refs = new ArrayList<>();
        for (int i = 0 ; i < loc.size() ; i++){
            refs.add(storage.getReference().child(loc.get(i)));
        }
        return refs;
    }

    @Override
    public void getArtInfoByPath(String path, int requestCode){
        String[] info = path.split("/");
        DocumentReference ref = db.collection(info[0]).document(info[1]).collection(info[2]).document(info[3]);
        ref.get().addOnSuccessListener(documentSnapshot -> {
            List<Artwork> artworks = new ArrayList<>();
            Artwork art = documentSnapshot.toObject(Book.class);
            artworks.add(art);
            mListener.onFileDownloadCompleteListener(artworks, requestCode);
        });
    }

    @Override
    public void getMultipleArtInfoByPath(List<String> paths, int requestCode){
        List<Artwork> artworks = new ArrayList<>();
        for (int i = 0 ; i < paths.size() ; i++){
            String path = paths.get(i);
            String[] info = path.split("/");

            DocumentReference ref = db.collection(info[0]).document(info[1]).collection(info[2]).document(info[3]);
            ref.get().addOnSuccessListener(documentSnapshot -> {
                Artwork art = documentSnapshot.toObject(Book.class);
                artworks.add(art);
                if (artworks.size() == paths.size())
                    mListener.onFileDownloadCompleteListener(artworks, requestCode);
            });

        }
    }

    public void getRecent(){
        DocumentReference ref = db.collection("Art").document("Recent");
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot != null){
                        Map<String, Object> map = snapshot.getData();
                        List<String> result = (List<String>) map.get("Locs");
                        mListener.onRecentFileDownloadCompleteListener(result);
                    }
                }
            }
        });
    }

    private List<String> setReferences(List<String> ext, String id, Artwork art){
        List<String> files = new ArrayList<>();
        for (int i = 0 ; i < ext.size() ; i++){
            files.add(art.getaType()+"/"+art.getaGenre()+"/"+id+"("+(i+1)+")"+"."+ext.get(i));
        }
        return files;
    }

}
