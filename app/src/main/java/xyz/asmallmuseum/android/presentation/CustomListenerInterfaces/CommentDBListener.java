package xyz.asmallmuseum.android.presentation.CustomListenerInterfaces;

import android.content.Intent;

import java.util.List;

import xyz.asmallmuseum.android.Domain.Comment;

public interface CommentDBListener {
    void onCommentLoadComplete(List<Comment> comments);
    void onCommentByUserLoadComplete(List<Comment> comments);
}
