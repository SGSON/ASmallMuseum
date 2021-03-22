package sg.asmallmuseum.persistence;

import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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
import sg.asmallmuseum.Domain.NumPosts;
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

    private final int REQUEST_USER = 2010;
    private final int REQUEST_UPLOAD = 2011;

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

        DocumentReference ref = db.collection("Art").document(art.getaCategory()).collection(art.getaType()).document();
        DocumentReference recentRef = db.collection("Art").document("Recent");
        Map<String, String> map = new HashMap<>();
        ref.set(art)
            .addOnSuccessListener(aVoid -> {
                //Log.d(TAG, "SUCCESS!");
                List<String> refs = setReferences(ext, ref.getId(), art);
                ref.update("aID", ref);
                ref.update("aFileLoc", refs);

                updateRecentList(ref.getPath());
                increaseNumPost(art.getaCategory(), art.getaType());

                mListener.onInfoUploadComplete(true, paths, refs, ref.getPath(), art);
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

                        if (result.size() >= MAX_LIST_SIZE)
                            result.remove(result.size()-1);
                        result.add(0, path);

                        map.put("Locs", result);
                        ref.set(map);
                    }
                }
            }
        });
    }

    @Override
    public void updatePostingNumber(Map<String, String> map, int numPost){
        DocumentReference ref = db.collection("Art").document(map.get("Category")).collection(map.get("Type")).document(map.get("id"));
        ref.update("aPostNum", numPost);
    }

    /***Get a image and a info***/
    public void getArtInfoList(String category, String type, int currPost){
        List<Artwork> list = new ArrayList<>();

        int numPost = MAX_LIST_SIZE;
        if (currPost < 10){
            numPost = currPost;
        }

        CollectionReference colRef = db.collection("Art").document(category).collection(type);
        colRef.whereLessThanOrEqualTo("aPostNum", currPost).orderBy("aPostNum", Query.Direction.DESCENDING).limit(numPost).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot collection : Objects.requireNonNull(task.getResult())){
                    Artwork artwork = getArtObject(collection, category);
                    list.add(artwork);
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
                Artwork art = documentSnapshot.toObject(VisualArts.class);
                artworks.add(art);
                if (artworks.size() == paths.size())
                    mListener.onFileDownloadComplete(artworks, requestCode);
            });

        }
    }

    @SuppressWarnings("unchecked")
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
    public void getNumPost(String category, String type, int request_id){
//        CollectionReference colRef = db.collection("Art").document(category).collection(type);
//        colRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()){
//                if(task.getResult() != null){
//                    mListener.onNumPostDownloadComplete(task.getResult().size(), request_id);
//                }
//                else {
//                    mListener.onNumPostDownloadComplete(0, request_id);
//                }
//            }
//        });

        DocumentReference docRef = db.collection("Art").document(category).collection(type).document("NumPosts");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                NumPosts posts = documentSnapshot.toObject(NumPosts.class);
                if (posts != null)
                    mListener.onNumPostDownloadComplete(posts.getnumPosts(), request_id);
                else
                    mListener.onNumPostDownloadComplete(0, request_id);
            }
        });
    }

    private void increaseNumPost(String category, String type){
        DocumentReference docRef = db.collection("Art").document(category).collection(type).document("NumPosts");
        docRef.update("numPosts", FieldValue.increment(1));
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
        switch (category){
            case "Books":
                artwork = collection.toObject(VisualArts.class);
                break;
            case "Music":
                artwork = collection.toObject(AppliedArts.class);
                break;
            case "Paints":
                artwork = collection.toObject(Others.class);
                break;
            case "Museums":
                artwork = collection.toObject(Museum.class);
                break;
            default:
                artwork = collection.toObject(FineArts.class);
                break;
        }
        return artwork;
    }



}
