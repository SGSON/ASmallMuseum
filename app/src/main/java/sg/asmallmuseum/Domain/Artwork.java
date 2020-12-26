package sg.asmallmuseum.Domain;

public abstract class Artwork {
    private String aID;
    private String aAuthor;
    private String aDate;
    private String aDesc;
    private String aFileName;

    public Artwork(){

    }

    public Artwork(String aID, String aAuthor, String aDate, String aDesc, String aFileName){
        this.aID = aID;
        this.aAuthor = aAuthor;
        this.aDate = aDate;
        this.aDesc = aDesc;
        this.aFileName = aFileName;
    }

    public String getaID() {
        return aID;
    }

    public void setaID(String aID) {
        this.aID = aID;
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

    public String getaFileName() {
        return aFileName;
    }

    public void setaFileName(String aFileName) {
        this.aFileName = aFileName;
    }
}
