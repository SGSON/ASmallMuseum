package sg.asmallmuseum.Domain;

public class Book extends Artwork {
    public Book(String aType, String aGenre, String aTitle, String aAuthor, String aDate, String aDesc, String aFileName) {
        super(aType, aGenre, aTitle, aAuthor, aDate, aDesc, aFileName);
    }

    /*public Book(String aFileName) {
        super(aFileName);
    }*/
}
