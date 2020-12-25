package sg.asmallmuseum.Domain;

public class User {
    private String uuID;
    private String uID;
    private String uPassword;
    private String uLastName;
    private String uFirstName;
    private String uEmail;
    private String uBirth;

    public User(String uID, String password, String lastname, String firstname, String email, String birth){
        uuID = "ASDASDASDA";
        this.uID = uID;
        this.uPassword = password;
        this.uFirstName = firstname;
        this.uLastName = lastname;
        this.uEmail = email;
        this.uBirth = birth;
    }
}
