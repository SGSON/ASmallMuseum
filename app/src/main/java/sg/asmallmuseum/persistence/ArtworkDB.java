package sg.asmallmuseum.persistence;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.List;
import java.util.Objects;

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
    public void addArt(Artwork art, List<Uri> paths, List<String> ext) {

        DocumentReference ref = db.collection("Art").document(art.getaType()).collection(art.getaGenre()).document();
        ref.set(art)
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "SUCCESS!");
                List<String> refs = setReferences(ext, ref.getId(), art);
                ref.update("aID", ref);
                ref.update("aFileLoc", refs);
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

    /***Get a image and a info***/
    public void getArtInfo(String type, String genre){
        List<Artwork> list = new ArrayList<>();
        CollectionReference colRef = db.collection("Art").document(type).collection(genre);
        colRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot collection : Objects.requireNonNull(task.getResult())){
                    Artwork artwork = collection.toObject(Book.class);
                    list.add(artwork);
                }
                mListener.onFileDownloadCompleteListener(list);
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
    public void getArtInfoById(String id){
        String[] info = id.split("/");
        //Log.d("Element", id.toString()+" "+(info.length));
        DocumentReference ref = db.collection(info[0]).document(info[1]).collection(info[2]).document(info[3]);
        ref.get().addOnSuccessListener(documentSnapshot -> {
            List<Artwork> artworks = new ArrayList<>();
            Artwork art = documentSnapshot.toObject(Book.class);
            artworks.add(art);
            mListener.onFileDownloadCompleteListener(artworks);
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
