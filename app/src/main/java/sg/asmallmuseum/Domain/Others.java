package sg.asmallmuseum.Domain;

import com.google.firebase.storage.StorageReference;

public class Others extends Artwork {
    public Others(String aCategory, String aType, String aTitle, String aAuthor, String aDate, String aDesc, String aUserID) {
        super(aCategory, aType, aTitle, aAuthor, aDate, aDesc, aUserID);
    }

    public Others() {
    }
}
