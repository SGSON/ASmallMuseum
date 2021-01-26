package sg.asmallmuseum.Domain.Messages;

public class ArtTitleError extends CustomException {
    public ArtTitleError(String errorMsg) {
        super(errorMsg);
    }
}
