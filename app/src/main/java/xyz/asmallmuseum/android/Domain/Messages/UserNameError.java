package xyz.asmallmuseum.android.Domain.Messages;

public class UserNameError extends CustomException {
    public UserNameError(String errorMsg) {
        super(errorMsg);
    }
}
