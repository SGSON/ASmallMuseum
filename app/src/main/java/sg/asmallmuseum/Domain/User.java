package sg.asmallmuseum.Domain;

import androidx.loader.content.Loader;

import java.io.Serializable;

public class User implements Serializable {
    private String uNick;
    private String uLastName;
    private String uFirstName;
    private String uEmail;
    private String uBirth;
    private String uType;

    public User(){
        //default constructor
        //do not delete this constructor
    }

    public User(String uNick, String lastname, String firstname, String email, String birth){

        this.uNick = uNick;
        this.uFirstName = firstname;
        this.uLastName = lastname;
        this.uEmail = email;
        this.uBirth = birth;
    }

    public User(String uNick, String lastname, String firstname, String email, String birth, String method){
        this.uNick = uNick;
        this.uFirstName = firstname;
        this.uLastName = lastname;
        this.uEmail = email;
        this.uBirth = birth;
        this.uType = method;
    }

    public String getuNick() {
        return uNick;
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

    public void setuNick(String uNick) {
        this.uNick = uNick;
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

    public String getuType() {
        return uType;
    }

    public void setuType(String uType) {
        this.uType = uType;
    }
}
