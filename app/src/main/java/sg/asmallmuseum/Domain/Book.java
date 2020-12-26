package sg.asmallmuseum.Domain;

public class Book extends Artwork {
    public Book(String aID, String aAuthor, String aDate, String aDesc, String aFileName) {
        super(aID, aAuthor, aDate, aDesc, aFileName);
    }

    /*public Book(String aFileName) {
        super(aFileName);
    }*/
}
