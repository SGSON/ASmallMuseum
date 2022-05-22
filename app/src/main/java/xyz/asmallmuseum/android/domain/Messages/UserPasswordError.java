package xyz.asmallmuseum.android.domain.Messages;

public class UserPasswordError extends CustomException {
    public UserPasswordError(String errorMsg) {
        super(errorMsg);
    }
}
