package xyz.asmallmuseum.android.domain.Messages;

public class UserEmailError extends CustomException {
    public UserEmailError(String errorMsg) {
        super(errorMsg);
    }
}
