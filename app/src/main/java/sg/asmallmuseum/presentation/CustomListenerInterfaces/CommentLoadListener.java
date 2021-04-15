package sg.asmallmuseum.presentation.CustomListenerInterfaces;

import android.content.Intent;

import java.util.List;

import sg.asmallmuseum.Domain.Comment;

public interface CommentLoadListener {
    void commentLoadListener(List<Comment> comments);
}
