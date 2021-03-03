package sg.asmallmuseum.persistence;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Book;
import sg.asmallmuseum.Domain.Museum;
import sg.asmallmuseum.Domain.Music;
import sg.asmallmuseum.Domain.Paint;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.logic.DBListener;

public class ArtworkDB implements ArtworkDBInterface {
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    private DBListener mListener;
    private final int MAX_LIST_SIZE = 10;

    private final int REQUEST_USER = 2010;
    private final int REQUEST_UPLOAD = 2011;

    public ArtworkDB(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void setListener(DBListener mListener){
        this.mListener = mListener;
    }

    /***Upload a image and info to the firestore and the storage***/
    @Override
    public void uploadArtInfo(Artwork art, List<Uri> paths, List<String> ext) {

        DocumentReference ref = db.collection("Art").document(art.getaType()).collection(art.getaGenre()).document();
        DocumentReference recentRef = db.collection("Art").document("Recent");
        Map<String, String> map = new HashMap<>();
        ref.set(art)
            .addOnSuccessListener(aVoid -> {
                //Log.d(TAG, "SUCCESS!");
                List<String> refs = setReferences(ext, ref.getId(), art);
                ref.update("aID", ref);
                ref.update("aFileLoc", refs);

                updateRecentList(ref.getPath());

                mListener.onInfoUploadComplete(true, paths, refs, ref.getId(), art);
            }).addOnFailureListener(e -> {
                //Log.w(TAG, "FAILED");
                mListener.onInfoUploadComplete(false, null, null, null, null);
            });
    }

    @Override
    public void uploadAttachedImage(List<Uri> paths, List<String> refs, String id, Artwork art) throws FileNotFoundException {
        for (int i = 0 ; i < paths.size() ; i++){
            StorageReference ref = storage.getReference().child(refs.get(i));
            UploadTask uploadTask = ref.putFile(paths.get(i));

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                //Log.d(TAG2, "SUCCESS!");
                mListener.onFileUploadComplete(true);
            }).addOnFailureListener(e -> {
                //Log.w(TAG2, "FAILED!");
                mListener.onFileUploadComplete(false);
            });
        }
    }

    private void updateRecentList(String path){
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

    @Override
    public void updatePostingNumber(Map<String, String> map, int numPost){
        DocumentReference ref = db.collection("Art").document(map.get("type")).collection(map.get("genre")).document(map.get("id"));
        ref.update("aPostNum", numPost);
    }

    /***Get a image and a info***/
    public void getArtInfoList(String type, String genre, int currPost){
        List<Artwork> list = new ArrayList<>();

        int numPost = MAX_LIST_SIZE;
        if (currPost < 10){
            numPost = currPost;
        }

        CollectionReference colRef = db.collection("Art").document(type).collection(genre);
        colRef.whereLessThanOrEqualTo("aPostNum", currPost).orderBy("aPostNum", Query.Direction.DESCENDING).limit(numPost).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot collection : Objects.requireNonNull(task.getResult())){
                    Artwork artwork = getArtObject(collection, type);
                    list.add(artwork);
                }
                mListener.onFileDownloadComplete(list, 0);
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
            Artwork art = getArtObject(documentSnapshot, info[1]);
                    //documentSnapshot.toObject(Book.class);
            artworks.add(art);
            mListener.onFileDownloadComplete(artworks, requestCode);
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
                    mListener.onFileDownloadComplete(artworks, requestCode);
            });

        }
    }

    @Override
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
                        mListener.onRecentFileDownloadComplete(result);
                    }
                }
            }
        });
    }

    @Override
    public void getNumPost(String type, String genre, int request_id){
        CollectionReference colRef = db.collection("Art").document(type).collection(genre);
        colRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if(task.getResult() != null){
                    mListener.onNumPostDownloadComplete(task.getResult().size(), request_id);
                }
                else {
                    mListener.onNumPostDownloadComplete(0, request_id);
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

    private void getCollection(CollectionReference ref, String type, String genre){
        List<Artwork> list = new ArrayList<>();

        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot collection : Objects.requireNonNull(task.getResult())){
                    Artwork artwork = getArtObject(collection, type);
                    list.add(artwork);
                }
                mListener.onFileDownloadComplete(list, 0);
            }
        });
    }

    private Artwork getArtObject(DocumentSnapshot collection, String type){
        Artwork artwork = null;
        switch (type){
            case "Books":
                artwork = collection.toObject(Book.class);
                break;
            case "Music":
                artwork = collection.toObject(Music.class);
                break;
            case "Paints":
                artwork = collection.toObject(Paint.class);
                break;
            case "Museums":
                artwork = collection.toObject(Museum.class);
                break;
            default:
                artwork = collection.toObject(Picture.class);
                break;
        }
        return artwork;
    }



}
