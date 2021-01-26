package sg.asmallmuseum.Domain.Messages;

public class ArtTypeError extends CustomException {
    public ArtTypeError(String errorMsg) {
        super(errorMsg);
    }
}
