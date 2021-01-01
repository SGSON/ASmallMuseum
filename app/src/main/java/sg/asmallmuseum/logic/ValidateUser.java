package sg.asmallmuseum.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sg.asmallmuseum.Domain.CustomException;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.Domain.UserEmailError;
import sg.asmallmuseum.Domain.UserPasswordError;

public class ValidateUser {
    private final int MAX_LENGTH = 20;
    private final int MIN_LENGTH = 7;

    public boolean validUser(User user) throws CustomException{
        validEmail(user);
        //validPassword(user);
        return true;
    }

    public boolean validEmail(User user) throws CustomException {
        if (!user.getuEmail().contains("@")){
            throw new UserEmailError("Enter the right email.");
        }
        return true;
    }

    public boolean validPassword(String password) throws CustomException{
        if (password.length() >= MIN_LENGTH && password.length() <= MAX_LENGTH){
            throw new UserPasswordError("Password requires at least "+MIN_LENGTH+" and less than "+MAX_LENGTH);
        }
        else if(!validForm(password)){
            throw new UserPasswordError("Pass requires at least 1 upper and lower case, number and special character.");
        }
        return true;
    }

    private boolean validForm(String pwd){
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]]");
        if (pwd == null || pwd.trim().isEmpty()){
            return false;
        }

        Matcher matcher = pattern.matcher(pwd);
        return matcher.find();
    }

}
