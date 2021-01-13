package sg.asmallmuseum.Domain;

import com.google.firebase.storage.StorageReference;

public class Book extends Artwork {
    public Book(String aType, String aGenre, String aTitle, String aAuthor, String aDate, String aDesc) {
        super(aType, aGenre, aTitle, aAuthor, aDate, aDesc);
    }

    public Book() {
        super();
    }
}
