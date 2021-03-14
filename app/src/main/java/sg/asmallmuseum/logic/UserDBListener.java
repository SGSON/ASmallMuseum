package sg.asmallmuseum.logic;

import java.util.List;

import sg.asmallmuseum.Domain.User;

public interface UserDBListener {
    void onUserLoadComplete(User user, int request_code);
    void onAllUserLoadComplete(List<String> list);
}
