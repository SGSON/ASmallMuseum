package xyz.asmallmuseum.android.Domain.Messages;

public class ArtTitleError extends CustomException {
    public ArtTitleError(String errorMsg) {
        super(errorMsg);
    }
}
