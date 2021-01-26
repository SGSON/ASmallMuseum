package sg.asmallmuseum.Domain.Messages;

public class UserPasswordError extends CustomException {
    public UserPasswordError(String errorMsg) {
        super(errorMsg);
    }
}
