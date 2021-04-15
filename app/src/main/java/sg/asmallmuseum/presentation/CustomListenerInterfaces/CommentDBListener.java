package sg.asmallmuseum.presentation.CustomListenerInterfaces;

import android.content.Intent;

import java.util.List;

import sg.asmallmuseum.Domain.Comment;

public interface CommentDBListener {
    void onCommentLoadComplete(List<Comment> comments);
    void onCommentByUserLoadComplete(List<Comment> comments);
}
