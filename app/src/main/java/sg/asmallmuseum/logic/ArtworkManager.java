package sg.asmallmuseum.logic;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Book;
import sg.asmallmuseum.Domain.Music;
import sg.asmallmuseum.Domain.Paint;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.persistence.ArtworkDB;

public class ArtworkManager implements ArtOnSuccessListener {
    private ArtworkDB db;
    private FirebaseAuth mAuth;
    private DocumentReference ref;

    public ArtworkManager() {
        this.db = new ArtworkDB();
        db.setOnSuccessListener(this);
    }

    public void addArtwork(String type, String genre, String title, String author, String date, String desc, String file){
        Artwork art = null;
        switch (type){
            case "Books":
                art = new Book(type, genre, title, author, date, desc, file);
                break;
            case "Music":
                art = new Music(type, genre, title, author, date, desc, file);
                break;
            case "Paints":
                art = new Paint(type, genre, title, author, date, desc, file);
                break;
            default:
                art = new Picture(type, genre, title, author, date, desc, file);
                break;
        }
        db.addArt(art);
    }

    @Override
    public void onSuccessListener(DocumentReference ref) {
        this.ref = ref;
    }
}
