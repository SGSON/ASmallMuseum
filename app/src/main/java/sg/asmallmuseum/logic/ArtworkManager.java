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
    public void addArtwork(String path, String id, String author, String date, String desc){
        String uuID = createUniqueID(path, id, author);

        switch (path){
            case "Paint":
                artwork = new Paint(id, author, date, desc);
                break;
            case "Book":
                artwork = new Book(id, author, date, desc);
                break;
            case "Music":
                artwork = new Music(id, author, date, desc);
                break;
            case "Picture":
                artwork = new Picture(id, author, date, desc);
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
