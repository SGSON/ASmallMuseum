package sg.asmallmuseum.Domain;

public class UserEmailError extends CustomException {
    public UserEmailError(String errorMsg) {
        super(errorMsg);
    }
}
