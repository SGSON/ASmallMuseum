package sg.asmallmuseum.Domain;

import com.google.firebase.storage.StorageReference;

public class Paint extends Artwork {
    public Paint(String aType, String aGenre, String aTitle, String aAuthor, String aDate, String aDesc) {
        super(aType, aGenre, aTitle, aAuthor, aDate, aDesc);
    }

    /*public Paint(String aFileName) {
        super(aFileName);
    }*/
}
