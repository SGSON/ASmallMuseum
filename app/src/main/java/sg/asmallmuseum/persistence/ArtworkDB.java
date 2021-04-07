package sg.asmallmuseum.persistence;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import sg.asmallmuseum.Domain.NumPosts;
import sg.asmallmuseum.Domain.RequestCode;
import sg.asmallmuseum.Domain.Values;
import sg.asmallmuseum.Domain.VisualArts;
import sg.asmallmuseum.Domain.Museum;
import sg.asmallmuseum.Domain.AppliedArts;
import sg.asmallmuseum.Domain.Others;
import sg.asmallmuseum.Domain.FineArts;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.ArtWorkDBListener;

public class ArtworkDB implements ArtworkDBInterface {
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    private ArtWorkDBListener mListener;
    private final int MAX_LIST_SIZE = 10;

    public ArtworkDB(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void setListener(ArtWorkDBListener mListener){
        this.mListener = mListener;
    }

    /***Upload a image and info to the firestore and the storage***/
    @Override
    public void uploadArtInfo(Artwork art, List<Uri> paths, List<String> ext) {

        DocumentReference ref = db.collection(Values.ART).document(art.getaCategory()).collection(art.getaType()).document();
        Map<String, String> map = new HashMap<>();
        ref.set(art)
            .addOnSuccessListener(aVoid -> {
                List<String> refs = setReferences(ext, ref.getId(), art);
                ref.update(Values.ART_VAL_ID, ref);
                ref.update(Values.ART_VAL_FILE_LOC, refs);

                updateRecentList(ref.getPath(), ref.getId(), art.getaDate());
                increaseNumPost(art.getaCategory(), art.getaType());

                mListener.onInfoUploadComplete(true, paths, refs, ref.getPath(), art);
            }).addOnFailureListener(e -> {
                mListener.onInfoUploadComplete(false, null, null, null, null);
            });
    }

    @Override
    public void uploadAttachedImage(List<Uri> paths, List<String> refs, String id, Artwork art) throws FileNotFoundException {
        for (int i = 0 ; i < paths.size() ; i++){
            StorageReference ref = storage.getReference().child(refs.get(i));
            UploadTask uploadTask = ref.putFile(paths.get(i));

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                mListener.onFileUploadComplete(true);
            }).addOnFailureListener(e -> {
                mListener.onFileUploadComplete(false);
            });
        }
    }

    private void updateRecentList(String path, String id, String date){
        DocumentReference ref = db.collection(Values.ART).document(Values.ART_RECENT).collection(Values.ART_RECENT).document(id);
        Map<String, String> map = new HashMap<>();
        map.put(Values.PATH, path);
        map.put(Values.DATE, date);
        ref.set(map);
    }

    @Override
    public void updatePostingNumber(Map<String, String> map, int numPost){
        DocumentReference ref = db.collection(Values.ART).document(map.get(Values.ART_CATEGORY)).collection(map.get(Values.ART_TYPE)).document(map.get(Values.ART_ID));
        ref.update(Values.ART_VAL_POST_NUM, numPost);
    }

    /***Get a image and a info***/
    public void getArtInfoList(String category, String type, int currPost){
        List<Artwork> list = new ArrayList<>();

        int numPost = MAX_LIST_SIZE;
        if (currPost < 10){
            numPost = currPost;
        }

        CollectionReference colRef = db.collection(Values.ART).document(category).collection(type);
        colRef.whereLessThanOrEqualTo(Values.ART_VAL_POST_NUM, currPost).orderBy(Values.ART_VAL_POST_NUM, Query.Direction.DESCENDING).limit(numPost).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot collection : Objects.requireNonNull(task.getResult())){
                    Artwork artwork = getArtObject(collection, category);
                    if (artwork != null){
                        list.add(artwork);
                    }
                }
                mListener.onFileDownloadComplete(list, 0);
            }
        });
    }

    @Override
    public List<StorageReference> getArtImages(String category, List<String> loc){
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

            if (art != null){
                artworks.add(art);
                mListener.onFileDownloadComplete(artworks, requestCode);
            }

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
                if (documentSnapshot.getData() != null){
                    Artwork art = documentSnapshot.toObject(VisualArts.class);
                    artworks.add(art);
                }
                if (artworks.size() == paths.size())
                    mListener.onFileDownloadComplete(artworks, requestCode);
            });
        }
    }

    @Override
    public void getRecent(){
        CollectionReference ref = db.collection(Values.ART).document(Values.ART_RECENT).collection(Values.ART_RECENT);
        ref.orderBy(Values.DATE, Query.Direction.DESCENDING).limit(MAX_LIST_SIZE).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> paths = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> map = document.getData();
                        if ((map.containsKey(Values.PATH)) && (map.get(Values.PATH) instanceof String)){
                            paths.add((String) map.get(Values.PATH));
                        }
                    }
                    getMultipleArtInfoByPath(paths, 0);
                }
            }
        });
    }

    @Override
    public void getNumPost(String category, String type, int request_id){
        DocumentReference docRef = db.collection(Values.ART).document(category).collection(type).document(Values.ART_NUM_POSTS);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                NumPosts posts = documentSnapshot.toObject(NumPosts.class);
                if (posts != null)
                    mListener.onNumPostDownloadComplete(posts.getnumPosts(), request_id, category, type);
                else
                    mListener.onNumPostDownloadComplete(0, request_id, null, null);
            }
        });
    }

    @Override
    public void getArtInfoByPostNum(String category, String type, int postNum, int request_code){
        CollectionReference colRef = db.collection(Values.ART).document(category).collection(type);
        colRef.whereEqualTo(Values.ART_VAL_POST_NUM, postNum).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Artwork artwork = null;
                    for (DocumentSnapshot snapshot : task.getResult()){
                        artwork = getArtObject(snapshot, category);
                    }

                    if (artwork != null){
                        List<Artwork> list = new ArrayList<>();
                        list.add(artwork);
                        mListener.onFileDownloadComplete(list, request_code);
                    }
                }
            }
        });
    }

    private void increaseNumPost(String category, String type){
        DocumentReference docRef = db.collection(Values.ART).document(category).collection(type).document(Values.ART_NUM_POSTS);
        docRef.update(Values.ART_NUM_POSTS_VAL, FieldValue.increment(1));
    }


    private List<String> setReferences(List<String> ext, String id, Artwork art){
        List<String> files = new ArrayList<>();
        for (int i = 0 ; i < ext.size() ; i++){
            files.add(art.getaCategory()+"/"+art.getaType()+"/"+id+"("+(i+1)+")"+"."+ext.get(i));
        }
        return files;
    }

    private void getCollection(CollectionReference ref, String category, String type){
        List<Artwork> list = new ArrayList<>();

        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot collection : Objects.requireNonNull(task.getResult())){
                    Artwork artwork = getArtObject(collection, category);
                    list.add(artwork);
                }
                mListener.onFileDownloadComplete(list, 0);
            }
        });
    }

    private Artwork getArtObject(DocumentSnapshot collection, String category){
        Artwork artwork = null;
        if (collection.getData() != null){
            switch (category){
                case Values.ART_VISUAL:
                    artwork = collection.toObject(VisualArts.class);
                    break;
                case Values.ART_APPLIED:
                    artwork = collection.toObject(AppliedArts.class);
                    break;
                case Values.ART_OTHERS:
                    artwork = collection.toObject(Others.class);
                    break;
                case Values.ART_MUSEUM:
                    artwork = collection.toObject(Museum.class);
                    break;
                default:
                    artwork = collection.toObject(FineArts.class);
                    break;
            }
        }
        return artwork;
    }

    public void deleteArtwork(String category, String type, String id){
        DocumentReference docRef = db.collection(Values.ART).document(category).collection(type).document(id);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.collection(Values.ART).document(category).collection(type).document(Values.ART_NUM_POSTS).update(Values.ART_NUM_POSTS_VAL, FieldValue.increment(-1));
                mListener.onDeleteComplete(true, RequestCode.RESULT_ART_DELETE_OK);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mListener.onDeleteComplete(false, RequestCode.RESULT_DELETE_FAIL);
            }
        });
    }

    public void deleteRecentPath(String id){
        DocumentReference docRef = db.collection(Values.ART).document(Values.ART_RECENT).collection(Values.ART_RECENT).document(id);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mListener.onDeleteComplete(true, RequestCode.RESULT_PATH_DELETE_OK);
            }
        });
    }

    @Override
    public void updateLike(String category, String type, String id, int value) {
        DocumentReference docRef = db.collection(Values.ART).document(category).collection(type).document(id);
        docRef.update(Values.ART_VAL_LIKE, FieldValue.increment(value));
    }

}
