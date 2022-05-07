package xyz.asmallmuseum.android.Domain.Messages;

public class ArtCategoryError extends CustomException {
    public ArtCategoryError(String errorMsg) {
        super(errorMsg);
    }
}
