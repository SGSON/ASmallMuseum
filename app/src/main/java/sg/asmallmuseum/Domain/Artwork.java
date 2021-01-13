package sg.asmallmuseum.Domain;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

public abstract class Artwork {
    private DocumentReference aID;
    private String aType;
    private String aGenre;
    private String aTitle;
    private String aAuthor;
    private String aDate;
    private String aDesc;
    private String aFileLoc;
    private int aRating;

    public Artwork(){

    }

    public Artwork(String aType, String aGenre, String aTitle, String aAuthor, String aDate, String aDesc){
        this.aType = aType;
        this.aGenre = aGenre;
        this.aTitle = aTitle;
        this.aAuthor = aAuthor;
        this.aDate = aDate;
        this.aDesc = aDesc;
        this.aRating = 0;
    }

    public DocumentReference getaID() {
        return aID;
    }

    public void setaID(DocumentReference aID) {
        this.aID = aID;
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }

    public String getaGenre() {
        return aGenre;
    }

    public void setaGenre(String aGenre) {
        this.aGenre = aGenre;
    }

    public String getaTitle() {
        return aTitle;
    }

    public void setaTitle(String aTitle) {
        this.aTitle = aTitle;
    }

    public String getaAuthor() {
        return aAuthor;
    }

    public void setaAuthor(String aAuthor) {
        this.aAuthor = aAuthor;
    }

    public String getaDate() {
        return aDate;
    }

    public void setaDate(String aDate) {
        this.aDate = aDate;
    }

    public String getaDesc() {
        return aDesc;
    }

    public void setaDesc(String aDesc) {
        this.aDesc = aDesc;
    }

    public String getaFileLoc() {
        return aFileLoc;
    }

    public void setaFileLoc(String aFileLoc) {
        this.aFileLoc = aFileLoc;
    }

    public int getaRating() {
        return aRating;
    }

    public void setaRating(int aRating) {
        this.aRating = aRating;
    }

}
