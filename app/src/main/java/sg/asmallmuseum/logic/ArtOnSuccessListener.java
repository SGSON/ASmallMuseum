package sg.asmallmuseum.logic;

import com.google.firebase.firestore.DocumentReference;

import sg.asmallmuseum.Domain.Artwork;

public interface ArtOnSuccessListener {
    void onSuccessListener(DocumentReference ref);
}
