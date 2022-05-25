package xyz.asmallmuseum.android.domain.Messages;

public class UserNameError extends CustomException {
    public UserNameError(String errorMsg) {
        super(errorMsg);
    }
}
