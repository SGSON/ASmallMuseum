package xyz.asmallmuseum.android.domain.Messages;

public class UserBirthError extends CustomException {
    public UserBirthError(String errorMsg) {
        super(errorMsg);
    }
}
