package xyz.asmallmuseum.android.domain.Messages;

public class ArtTitleError extends CustomException {
    public ArtTitleError(String errorMsg) {
        super(errorMsg);
    }
}
