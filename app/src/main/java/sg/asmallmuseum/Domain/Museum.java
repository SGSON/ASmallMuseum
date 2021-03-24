package sg.asmallmuseum.Domain;

public class Museum extends Artwork {
    private String aThumbnail;
    private String aMainImage;
    private String aMaterial;

    public Museum(){}

    public Museum(String aCategory, String aType, String aTitle, String aAuthor, String aDate, String aDesc, String aThumbnail, String aMainImage, String aMaterial){
        super(aCategory, aType, aTitle, aAuthor, aDate, aDesc, null);
        this.aThumbnail = aThumbnail;
        this.aMainImage = aMainImage;
        this.aMaterial = aMaterial;
    }

    public String getaThumbnail() {
        return aThumbnail;
    }

    public void setaThumbnail(String aThumbnail) {
        this.aThumbnail = aThumbnail;
    }

    public String getaMainImage() {
        return aMainImage;
    }

    public void setaMainImage(String aMainImage) {
        this.aMainImage = aMainImage;
    }

    public String getaMaterial() {
        return aMaterial;
    }

    public void setaMaterial(String aMaterial) {
        this.aMaterial = aMaterial;
    }
}
