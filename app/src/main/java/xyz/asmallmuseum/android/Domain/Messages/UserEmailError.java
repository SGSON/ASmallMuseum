package xyz.asmallmuseum.android.Domain.Messages;

public class UserEmailError extends CustomException {
    public UserEmailError(String errorMsg) {
        super(errorMsg);
    }
}
