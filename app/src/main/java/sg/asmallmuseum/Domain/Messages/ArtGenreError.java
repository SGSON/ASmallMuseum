package sg.asmallmuseum.Domain.Messages;

public class ArtGenreError extends CustomException {
    public ArtGenreError(String errorMsg) {
        super(errorMsg);
    }
}
