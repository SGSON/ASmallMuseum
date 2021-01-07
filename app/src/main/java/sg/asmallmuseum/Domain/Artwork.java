package sg.asmallmuseum.Domain;

public abstract class Artwork {
    private String aID;
    private String aType;
    private String aGenre;
    private String aTitle;
    private String aAuthor;
    private String aDate;
    private String aDesc;
    private String aFileLoc;

    public Artwork(){

    }

    public Artwork(String aType, String aGenre, String aTitle, String aAuthor, String aDate, String aDesc, String aFileLoc){
        this.aType = aType;
        this.aGenre = aGenre;
        this.aTitle = aTitle;
        this.aAuthor = aAuthor;
        this.aDate = aDate;
        this.aDesc = aDesc;
        this.aFileLoc = aFileLoc;
    }

    public String getaID() {
        return aID;
    }

    public void setaID(String aID) {
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

    public void setaFileLoc(String aFileName) {
        this.aFileLoc = aFileLoc;
    }
}
