package sg.asmallmuseum.Domain;

public class UserPasswordError extends CustomException {
    public UserPasswordError(String errorMsg) {
        super(errorMsg);
    }
}
