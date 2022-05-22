package xyz.asmallmuseum.android.presentation.CustomListenerInterfaces;

import java.util.List;

import xyz.asmallmuseum.android.domain.Comment;

public interface CommentDBListener {
    void onCommentLoadComplete(List<Comment> comments);
    void onCommentByUserLoadComplete(List<Comment> comments);
}
