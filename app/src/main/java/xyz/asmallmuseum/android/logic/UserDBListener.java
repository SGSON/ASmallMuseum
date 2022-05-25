package xyz.asmallmuseum.android.logic;

import java.util.List;

import xyz.asmallmuseum.android.domain.User;

public interface UserDBListener {
    void onUserLoadComplete(User user, int request_code);
    void onAllUserLoadComplete(List<String> list);
    void onUserPostLoadComplete(List<String> posts, int request_code);
    void onPathDeleteComplete(boolean result, String field);
    void onPostExists(boolean result);
}
