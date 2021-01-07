package sg.asmallmuseum.persistence;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import androidx.annotation.NonNull;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.logic.ArtOnSuccessListener;

public class ArtworkDB implements ArtworkDBInterface {
    private FirebaseFirestore db;
    private ArtOnSuccessListener mListener;
    private final String TAG = "ART DB Connection: ";

    public ArtworkDB(){
        db = FirebaseFirestore.getInstance();
    }

    public void setOnSuccessListener(ArtOnSuccessListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void addArt(Artwork art) {
        DocumentReference ref = db.collection("Art").document(art.getaType()).collection(art.getaGenre()).document();
        ref.set(art)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "SUCCESS!");
                    ref.update("aID", ref);
                    mListener.onSuccessListener(ref);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "FAILED");
                }
            });

    }
}
