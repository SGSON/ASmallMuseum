package sg.asmallmuseum.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.RequestCode;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.Domain.Values;
import sg.asmallmuseum.persistence.EmailUserDB;
import sg.asmallmuseum.persistence.UserDBInterface;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserLoadListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserPathDeleteListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserPostExistsListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserPostLoadCompleteListener;

public class UserManager implements UserDBListener {
    private UserDBInterface db;
    private UserLoadListener userLoadListener;
    private UserPostLoadCompleteListener userPostLoadListener;
    private UserPathDeleteListener userPathDeleteListener;
    private UserPostExistsListener userPostExistsListener;

    public void setUserLoadListener(UserLoadListener userLoadListener){
        this.userLoadListener = userLoadListener;
    }

    public void setUserPostLoadListener(UserPostLoadCompleteListener userPostLoadListener){
        this.userPostLoadListener = userPostLoadListener;
    }

    public void setUserPathDeleteListener(UserPathDeleteListener mListener){
        this.userPathDeleteListener = mListener;
    }

    public void setUserPostExistsListener(UserPostExistsListener mListener){
        this.userPostExistsListener = mListener;
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
    public void addNewUser(User user) {

        db.addUser(user);

    }

    public void getTempUserInfo(String email){

       db.getTempUser(email);

    }

    public void getUserInfo(String email, int request_code){

        db.getUser(email, request_code);

    }

    public boolean exists(String email){
        getUserInfo(email, RequestCode.REQUEST_EXIST);
        return true;
    }

    public void existsIn(String field, String email, Artwork artwork){
        db.exists(email, field, artwork.getaID().getId());
    }

    public void getUserPosting(String uEmail, String field){
        db.getUserPosting(uEmail, field);
    }

    public void updateUser(String uNick, String lastname, String firstname, String email, String birth){
        User user = new User(uNick, lastname, firstname, email, birth);
        db.updateUser(user);
    }

    public void updateUserPost(String uEmail, String field, String path){
        String[] paths = path.split("/");
        Map<String, String> map = new HashMap<>();
        map.put(Values.PATH, path);
        db.addUserPosting(uEmail, field, paths[paths.length-1], map);
    }

    public void deletePath(User user, Artwork artwork, String field){
        db.deletePath(user.getuEmail(), field, artwork.getaID().getId());
    }

    public void deletePath(String email, Artwork artwork, String field){
        db.deletePath(email, field, artwork.getaID().getId());
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

    }

    @Override
    public void onUserPostLoadComplete(List<String> posts, int request_code) {
        userPostLoadListener.onUserPostLoadComplete(posts);
    }

    @Override
    public void onPathDeleteComplete(boolean result) {
        userPathDeleteListener.onUserPathDeleteComplete(result);
    }

    @Override
    public void onPostExists(boolean result) {
        userPostExistsListener.onUserPostExists(result);
    }

}
