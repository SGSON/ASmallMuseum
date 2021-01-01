package sg.asmallmuseum.Domain;

public class User {
    private String uID;
    private String uPassword;
    private String uLastName;
    private String uFirstName;
    private String uEmail;
    private String uBirth;

    public User(){
        //default constructor
        //do not delete this constructor
    }

    public User(String uID, String lastname, String firstname, String email, String birth){
        this.uID = uID;
        this.uFirstName = firstname;
        this.uLastName = lastname;
        this.uEmail = email;
        this.uBirth = birth;
    }

    public String getuID() {
        return uID;
    }

    public String getuPassword() {
        return uPassword;
    }

    public String getuLastName() {
        return uLastName;
    }

    public String getuFirstName() {
        return uFirstName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public String getuBirth() {
        return uBirth;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public void setuLastName(String uLastName) {
        this.uLastName = uLastName;
    }

    public void setuFirstName(String uFirstName) {
        this.uFirstName = uFirstName;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public void setuBirth(String uBirth) {
        this.uBirth = uBirth;
    }
}
