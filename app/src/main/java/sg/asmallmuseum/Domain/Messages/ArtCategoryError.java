package sg.asmallmuseum.Domain.Messages;

public class ArtCategoryError extends CustomException {
    public ArtCategoryError(String errorMsg) {
        super(errorMsg);
    }
}
