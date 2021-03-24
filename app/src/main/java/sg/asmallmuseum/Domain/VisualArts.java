package sg.asmallmuseum.Domain;

import com.google.firebase.storage.StorageReference;

public class VisualArts extends Artwork {
    public VisualArts(String aType, String aGenre, String aTitle, String aAuthor, String aDate, String aDesc, String aUserID) {
        super(aType, aGenre, aTitle, aAuthor, aDate, aDesc, aUserID);
    }

    public VisualArts() {
        super();
    }
}
