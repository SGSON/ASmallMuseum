package xyz.asmallmuseum.android.presentation.CustomListenerInterfaces;

import java.util.List;

import xyz.asmallmuseum.android.domain.Comment;

public interface CommentLoadListener {
    void commentLoadListener(List<Comment> comments);
}
