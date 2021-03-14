package sg.asmallmuseum.Domain.Messages;

public class UserBirthError extends CustomException {
    public UserBirthError(String errorMsg) {
        super(errorMsg);
    }
}
