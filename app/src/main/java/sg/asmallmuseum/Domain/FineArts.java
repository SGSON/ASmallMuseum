package sg.asmallmuseum.Domain;

import com.google.firebase.storage.StorageReference;

public class FineArts extends Artwork {

    public FineArts(String aCategory, String aType, String aTitle, String aAuthor, String aDate, String aDesc) {
        super(aCategory, aType, aTitle, aAuthor, aDate, aDesc);
    }

   /*public Picture(String aFileName) {
        super(aFileName);
    }*/
}
