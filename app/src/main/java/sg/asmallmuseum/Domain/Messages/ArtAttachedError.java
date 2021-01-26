package sg.asmallmuseum.Domain.Messages;

public class ArtAttachedError extends CustomException {
    public ArtAttachedError(String errorMsg) {
        super(errorMsg);
    }
}
