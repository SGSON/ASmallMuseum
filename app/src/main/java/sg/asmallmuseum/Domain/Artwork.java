package sg.asmallmuseum.Domain;

public abstract class Artwork {
    private String id;
    private String author;
    private String date;
    private String desc;

    public Artwork(String id, String author, String date, String desc){
        this.id = id;
        this.author = author;
        this.date = date;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
