package sg.asmallmuseum.Domain;

import com.google.firebase.storage.StorageReference;

public class Picture extends Artwork {

    public Picture(String aType, String aGenre, String aTitle, String aAuthor, String aDate, String aDesc) {
        super(aType, aGenre, aTitle, aAuthor, aDate, aDesc);
    }

   /*public Picture(String aFileName) {
        super(aFileName);
    }*/
}
