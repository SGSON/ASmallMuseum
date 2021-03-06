package sg.asmallmuseum.Domain;

import com.google.firebase.storage.StorageReference;

public class AppliedArts extends Artwork {
    public AppliedArts(String aCategory, String aType, String aTitle, String aAuthor, String aDate, String aDesc) {
        super(aCategory, aType, aTitle, aAuthor, aDate, aDesc);
    }

    public AppliedArts() {
    }

    /*public Music(String aFileName) {
        super(aFileName);
    }*/
}
