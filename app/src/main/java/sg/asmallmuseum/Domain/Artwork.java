package sg.asmallmuseum.Domain;

public abstract class Artwork {
    private String aID;
    private String aAuthor;
    private String aDate;
    private String aDesc;

    public Artwork(){

    }

    public Artwork(String aID, String aAuthor, String aDate, String aDesc){
        this.aID = aID;
        this.aAuthor = aAuthor;
        this.aDate = aDate;
        this.aDesc = aDesc;
    }

    public String getId() {
        return aID;
    }

    public void setId(String id) {
        this.aID = id;
    }

    public String getAuthor() {
        return aAuthor;
    }

    public void setAuthor(String author) {
        this.aAuthor = author;
    }

    public String getDate() {
        return aDate;
    }

    public void setDate(String date) {
        this.aDate = date;
    }

    public String getDesc() {
        return aDesc;
    }

    public void setDesc(String desc) {
        this.aDesc = desc;
    }
}
