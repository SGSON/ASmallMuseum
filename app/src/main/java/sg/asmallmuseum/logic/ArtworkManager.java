package sg.asmallmuseum.logic;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Book;
import sg.asmallmuseum.Domain.Music;
import sg.asmallmuseum.Domain.Paint;
import sg.asmallmuseum.Domain.Picture;
import sg.asmallmuseum.persistence.GoogleUserDB;

public class ArtworkManager {
    private GoogleUserDB database;

    public ArtworkManager() {
        this.database = new GoogleUserDB();
        //database.connection();
    }

    public void addArtwork(String path, String aID, String aAuthor, String aDate, String aDesc, String aFileName){
        String uaID = createUniqueID(path, aID, aAuthor);
        Artwork artwork;

        switch (path){
            case "Paints":
                artwork = new Paint(aID, aAuthor, aDate, aDesc, aFileName);
                break;
            case "Books":
                artwork = new Book(aID, aAuthor, aDate, aDesc, aFileName);
                break;
            case "Music":
                artwork = new Music(aID, aAuthor, aDate, aDesc, aFileName);
                break;
            case "Pictures":
                artwork = new Picture(aID, aAuthor, aDate, aDesc, aFileName);
                break;
            default:
                artwork = new Paint(aID, aAuthor, aDate, aDesc, aFileName);
                break;
        }

        addToDB(path, uaID, artwork);
    }
    /*public void addArtwork(String path, String aFileName){
        String uaID = createUniqueID(path, "asd", "ASDSADASD");
        Artwork artwork = null;

        switch (path){
            case "Paints":
                artwork = new Paint(aFileName);
                break;
            case "Books":
                artwork = new Book(aFileName);
                break;
            case "Music":
                artwork = new Music(aFileName);
                break;
            case "Pictures":
                artwork = new Picture(aFileName);
                break;
        }

        addToDB(path, uaID, artwork);
    }*/

    private String createUniqueID(String path, String id, String author){
        return path + id + author;
    }

    private void addToDB(String path, String uaID, Artwork artwork){
        //database.addArtwork(path, uaID, artwork);
    }
}
