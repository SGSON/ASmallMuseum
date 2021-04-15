package sg.asmallmuseum.persistence;

import android.content.Intent;

import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Comment;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.CommentDBListener;

public interface CommentDBInterface {
    void addComment(Comment comment, String category, String type, String path);
    void getComment(String category, String type, String path);
    void getRefreshComment(String category, String type, String path);
    void getCommentByUser(String email);
    void updateComment(Comment comment);
    void deleteComment(String category, String type, String path, String commIdx, String uEmail);
    void setDBListener(CommentDBListener commentDBListener);
}
