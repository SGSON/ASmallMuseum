package sg.asmallmuseum.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.Messages.UserBirthError;
import sg.asmallmuseum.Domain.Messages.UserNameError;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.Domain.Messages.UserEmailError;
import sg.asmallmuseum.Domain.Messages.UserPasswordError;

public class ValidateUser {
    private static final int MAX_LENGTH = 20;
    private static final int MIN_LENGTH = 7;


    public static boolean validEmailUser(User user, String password, String checkPassword) throws CustomException{
        validEmail(user.getuEmail());
        validPassword(password, checkPassword);
        validName(user.getuFirstName());
        validName(user.getuLastName());
        validName(user.getuNick());
        validBirth(user.getuBirth());

        return true;
    }

    public static void validUser(String uNick, String uLastName, String uFirstName,String uBirth) throws CustomException{
        validName(uFirstName);
        validName(uLastName);
        validName(uNick);
        validBirth(uBirth);
    }

    public static boolean validEmail(String email) throws CustomException {
        if (email == null || email.equals("")){
            throw new UserEmailError("Enter your email.");
        }
        if (!email.contains("@") || !email.contains(".") || email.lastIndexOf("@") > email.lastIndexOf(".")){
            throw new UserEmailError("Enter the right email.");
        }
        return true;
    }

    public static boolean validPassword(String password, String checkPassword) throws CustomException{
        if (!password.equals(checkPassword)){
            throw new UserPasswordError("Does not match");
        }
        else if (password.length() <= MIN_LENGTH || password.length() >= MAX_LENGTH){
            throw new UserPasswordError("Password requires at least "+MIN_LENGTH+" and less than "+MAX_LENGTH);
        }
        else if(!validForm(password)){
            throw new UserPasswordError("Pass requires at least 1 upper and lower case, number and special character.");
        }
        return true;
    }

    public static boolean validPassword(String password) throws CustomException{
        if (password == null || password.equals("")){
            throw new UserPasswordError("Enter the password");
        }
        return true;
    }

    private static boolean validForm(String pwd){
        Pattern pattern = Pattern.compile("[A-Za-z0-9]");
        if (pwd == null || pwd.trim().isEmpty()){
            return false;
        }

        Matcher matcher = pattern.matcher(pwd);
        return matcher.find();
    }

    private static boolean validName(String name) throws CustomException{
        if (name == null || name.equals("")){
            throw new UserNameError("Please Enter your name");
        }
        return true;
    }

    private static boolean validBirth(String birth) throws CustomException{
        if (birth == null){
            throw new UserBirthError("Please Enter your birth");
        }
        return true;
    }
}
