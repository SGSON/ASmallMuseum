package sg.asmallmuseum.Domain.Messages;

public class UserEmailError extends CustomException {
    public UserEmailError(String errorMsg) {
        super(errorMsg);
    }
}
