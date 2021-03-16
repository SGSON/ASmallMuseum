package sg.asmallmuseum.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.persistence.EmailUserDB;
import sg.asmallmuseum.persistence.UserDBInterface;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserLoadListener;

public class UserManager implements UserDBListener {
    private UserDBInterface db;
    private UserLoadListener userLoadListener;

    private static final int REQUEST_EXIST = 3301;
    private static final int REQUEST_USER = 3302;
    private static final int REQUEST_USER_LIST = 3303;

    public void setListener(UserLoadListener userLoadListener){
        this.userLoadListener = userLoadListener;
    }

    /***Set a Db***/
    public UserManager(){
        db = new EmailUserDB();
        db.setDBListener(this);
    }

    /***this is for a email Sign-up method.***/
    public void addNewUser(String uNick, String lastname, String firstname, String email, String birth, String method) {

        User user = new User(uNick, lastname, firstname, email, birth, method);

        db.addUser(user);

    }

    public void getTempUserInfo(String email){

       db.getTempUser(email);

    }

    public void getUserInfo(String email, int request_code){

        db.getUser(email, request_code);

    }

    public boolean hasExisted(String email){
        getUserInfo(email, REQUEST_EXIST);
        return true;
    }

    /*public void getAllUser(){
        db.getAllUser();

    }*/

    public void updateUser(String uNick, String lastname, String firstname, String email, String birth){
        User user = new User(uNick, lastname, firstname, email, birth);
        db.updateUser(user);
    }

    public void updateUserPost(String uEmail, String field, String path){
        String[] paths = path.split("/");
        Map<String, String> map = new HashMap<>();
        map.put("path", path);
        db.addUserPosting(uEmail, field, paths[paths.length-1], map);
    }

    public void deleteUser(String email){
        db.deleteUser(email);
    }

    @Override
    public void onUserLoadComplete(User user, int request_code) {
        userLoadListener.userInfo(user);
    }

    @Override
    public void onAllUserLoadComplete(List<String> list) {
//        String fType = list.get(0);
//        String sType = list.get(1);
//        String loop = list.get(2);
//
//        if(fType.equals("email") && sType.equals("sType")){
//            db = new GoogleUserDB();
//            db.setDBListener(this);
//            db.getAllUser(list);
//        }else if(fType.equals("google") && sType.equals("sType")){
//            db = new EmailUserDB();
//            db.setDBListener(this);
//            db.getAllUser(list);
//        }else if(fType.equals("facebook") && sType.equals("sType")){
//            db = new EmailUserDB();
//            db.setDBListener(this);
//            db.getAllUser(list);
//        }
//
//        if(fType.equals("email") && sType.equals("googleDB")){
//            list.set(2,"end");
//            db = new FacebookUserDB();
//            db.setDBListener(this);
//            db.getAllUser(list);
//        }else if(fType.equals("google") && sType.equals("emailDB")){
//            list.set(2,"end");
//            db = new FacebookUserDB();
//            db.setDBListener(this);
//            db.getAllUser(list);
//        }else if(fType.equals("facebook") && sType.equals("emailDB")){
//            list.set(2,"end");
//            db = new GoogleUserDB();
//            db.setDBListener(this);
//            db.getAllUser(list);
//        }
//
//        if(loop.equals("end")){
//            userLoadListener.getAllUser(list);
//        }

    }
    /***this method is for other sign-up methods. It does not get a password.***/
    /*public User addNewUser(FirebaseAuth mAuth, String uNick, String lastName,
                           String firstName, String eMail, @Nullable String birth) throws CustomException{
        //Create user object.
        User user = new User(uNick, lastName, firstName, eMail, birth);

        //check validation
        ValidateUser validateUser = new ValidateUser();
        validateUser.validUser(user);

        //send user to firebase db.
        //database.addUser(mAuth, user);
        return user;
    }*/






}
