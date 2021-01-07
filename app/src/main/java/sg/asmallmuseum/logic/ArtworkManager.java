package sg.asmallmuseum.logic;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Book;
import sg.asmallmuseum.Domain.Music;
import sg.asmallmuseum.Domain.Paint;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.persistence.ArtworkDB;
import sg.asmallmuseum.persistence.GoogleUserDB;

public class ArtworkManager {
    private ArtworkDB db;
    private FirebaseAuth mAuth;

    public ArtworkManager() {
        this.db = new ArtworkDB();
    }

    public void addArtwork(String type, String Genre, String title, String author, String date, String file){
        Map<String, String> art = new HashMap<>();
        art.put("Title", title);
        art.put("Author", author);
        art.put("Date", date);
        art.put("FileLoc", file);

    }

    private String createUniqueID(String path, String id, String author){
        return path + id + author;
    }

    private void addToDB(String path, String uaID, Artwork artwork){
        //database.addArtwork(path, uaID, artwork);
    }
}
