package xyz.asmallmuseum.android.Domain.Messages;

public class UserPasswordError extends CustomException {
    public UserPasswordError(String errorMsg) {
        super(errorMsg);
    }
}
