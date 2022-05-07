package xyz.asmallmuseum.android.Domain.Messages;

public class CustomException extends Exception {
    private String errorMsg;

    public CustomException(String errorMsg){
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg(){
        return errorMsg;
    }
}
