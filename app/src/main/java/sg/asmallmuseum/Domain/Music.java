package sg.asmallmuseum.Domain;

import com.google.firebase.storage.StorageReference;

public class Music extends Artwork {
    public Music(String aType, String aGenre, String aTitle, String aAuthor, String aDate, String aDesc) {
        super(aType, aGenre, aTitle, aAuthor, aDate, aDesc);
    }

    /*public Music(String aFileName) {
        super(aFileName);
    }*/
}
