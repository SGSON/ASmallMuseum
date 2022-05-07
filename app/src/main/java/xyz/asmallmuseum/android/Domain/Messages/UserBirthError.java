package xyz.asmallmuseum.android.Domain.Messages;

public class UserBirthError extends CustomException {
    public UserBirthError(String errorMsg) {
        super(errorMsg);
    }
}
