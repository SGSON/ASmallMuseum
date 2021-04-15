package sg.asmallmuseum.logic;

import android.content.Intent;
import android.util.Log;

import java.util.List;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Comment;
import sg.asmallmuseum.persistence.CommentDB;
import sg.asmallmuseum.persistence.CommentDBInterface;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.CommentDBListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.CommentLoadListener;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UserLoadListener;

public class CommentManager implements CommentDBListener {
    private CommentDBInterface db;
    private CommentLoadListener commentLoadListener;

    public CommentManager(){
        db = new CommentDB();
        db.setDBListener(this);
    }

    public void setCommentListener(CommentLoadListener commentLoadListener){
        this.commentLoadListener = commentLoadListener;
    }

    public void addComment(Comment comment, String category, String type, String path){
        db.addComment(comment, category, type, path);
    }

    public void getComment(String category, String type, String path){
        db.getComment(category, type, path);
    }

    public void getRefreshComment(String category, String type, String path){
        db.getRefreshComment(category, type, path);
        Log.d("refresh", "getRefreshComment: "+ category+"/"+type+"/"+path);
    }

    public void getCommentByUser(String email){
        db.getCommentByUser(email);
    }

    public void updateComment(Comment comment){
        db.updateComment(comment);
    }

    public void deleteComment(String category, String type, String path, String commIdx, String uEmail){
        db.deleteComment(category, type, path, commIdx, uEmail);
    }

    @Override
    public void onCommentLoadComplete(List<Comment> comments) {
        Log.d("commentDB", "onCommentLoadComplete: " + comments);
        commentLoadListener.commentLoadListener(comments);
    }

    @Override
    public void onCommentByUserLoadComplete(List<Comment> comments) {

    }
}
