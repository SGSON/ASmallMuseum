package xyz.asmallmuseum.android.Domain.Messages;

public class ArtDescError extends CustomException {
    public ArtDescError(String errorMsg) {
        super(errorMsg);
    }
}
