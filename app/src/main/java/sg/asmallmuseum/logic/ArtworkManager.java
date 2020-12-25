package sg.asmallmuseum.logic;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Book;
import sg.asmallmuseum.Domain.Music;
import sg.asmallmuseum.Domain.Paint;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.persistence.DBConnect;

public class ArtworkManager {
    private DBConnect database;
    private Artwork artwork;

    public ArtworkManager() {
        this.database = new DBConnect();
        database.connection();
    }
    public void addArtwork(String path, String aID, String aAuthor, String aDate, String aDesc){
        String uuID = createUniqueID(path, aID, aAuthor);

        switch (path){
            case "Paints":
                artwork = new Paint(aID, aAuthor, aDate, aDesc);
                break;
            case "Books":
                artwork = new Book(aID, aAuthor, aDate, aDesc);
                break;
            case "Music":
                artwork = new Music(aID, aAuthor, aDate, aDesc);
                break;
            case "Pictures":
                artwork = new Picture(aID, aAuthor, aDate, aDesc);
                break;
        }

        addToDB(path, uuID, artwork);
    }

    private String createUniqueID(String path, String id, String author){
        return path + id + author;
    }

    private void addToDB(String path, String uuID, Artwork artwork){
        database.addArtwork(path, uuID, artwork);
    }
}
