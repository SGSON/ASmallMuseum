package xyz.asmallmuseum.android.domain.Messages;

public class ArtCategoryError extends CustomException {
    public ArtCategoryError(String errorMsg) {
        super(errorMsg);
    }
}
