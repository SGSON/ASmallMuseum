package sg.asmallmuseum.Domain;

public class CustomException extends Exception {
    private String errorMsg;

    public CustomException(String errorMsg){
        this.errorMsg = errorMsg;
    }
}
